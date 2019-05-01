import json
import os
import os.path

from base64 import b64decode
from datetime import datetime

from lambda_arch.aws import (
        get_matching_s3_keys,
        read_body_from_s3,
        write_to_dynamo_table,
        write_to_s3,
)

DT_FORMAT = '%Y-%m-%dT%H:%M:%S.%fZ'


def _get_bucket_and_key_from_event(event):
    record = event['Records'][0]
    s3 = record['s3']
    bucket = s3['bucket']['name']
    key = s3['object']['key']
    return (bucket, key)


def single(event, context):
    """Process a single message from a kinesis stream and write it to DynamoDB"""
    record = event['Records'][0]
    data = record['kinesis']['data']

    # Append on a delimiter since we need to unpack messages which are concatenated together when
    # receiving multiple messages from Firehose.
    data = json.loads(b64decode(data).decode().rstrip('|||'))

    # Create our hash key
    data['productTrade'] = '{product_id}|{time}|{trade_id}'.format(**data)

    write_to_dynamo_table(os.environ['TABLE_NAME'], data)


def minute(event, context):
    """Process an S3 object uploaded to S3 from Kinesis Firehose.

    The data format from Firehose is all of the messages from the `single` function above,
    concatenated together. In order to read thse messages, we need to decode them and split the
    string by our own delimiter.

    """
    bucket, key = _get_bucket_and_key_from_event(event)
    data = read_body_from_s3(bucket, key).decode()

    product_prices = {}

    lines = [json.loads(l) for l in data.split('|||') if l]
    times = []

    for line in lines:
        # Only keep track of buys for the average price, since sells could be sell orders which are
        # never executed.
        if line.get('side') != 'buy':
            continue

        product_id = line['product_id']
        price = float(line['price'])

        times.append(line['time'])
        if product_id not in product_prices:
            product_prices[product_id] = {'prices': [price]}
        else:
            product_prices[product_id]['prices'].append(price)

    if not product_prices:
        return

    # Calculate the average for each product
    for key in product_prices:
        prices = product_prices[key]['prices']
        product_prices[key]['average'] = sum(prices) * 1.0 / len(prices)

    # Determine the most recent timestamp from the list of buys so we can determine the key to
    # write.
    times.sort()
    latest_time = times[-1]
    latest_dt = datetime.strptime(latest_time, DT_FORMAT)

    destination_bucket = os.environ['DESTINATION_BUCKET']
    new_key = latest_dt.strftime('%Y/%m/%d/%H/%M-minute.json')
    new_payload = json.dumps(product_prices, indent=2)

    print('Uploading to', destination_bucket, new_key)

    write_to_s3(destination_bucket, new_key, new_payload)


def _aggregate_prices(event, period='hour'):
    """Aggregate average prices for a particular time slice"""
    bucket, key = _get_bucket_and_key_from_event(event)
    key_root = os.path.dirname(key)

    product_prices = {}

    for key in get_matching_s3_keys(bucket, prefix=key_root + '/', suffix='-minute.json'):
        data = read_body_from_s3(bucket, key).decode()
        minute_prices = json.loads(data)

        for product_id, payload in minute_prices.items():
            prices = payload['prices']
            if product_id not in product_prices:
                product_prices[product_id] = {'prices': prices}
            else:
                product_prices[product_id]['prices'].extend(prices)

    for key in product_prices:
        prices = product_prices[key]['prices']
        average_price = sum(prices) * 1.0 / len(prices)
        product_prices[key]['average'] = average_price

    new_key = '%s-%s.json' % (key_root, period)
    new_payload = json.dumps(product_prices, indent=2)

    print('Uploading to', bucket, new_key)

    write_to_s3(bucket, new_key, new_payload)


def hourly(event, context):
    _aggregate_prices(event, period='hour')


def daily(event, context):
    _aggregate_prices(event, period='day')
