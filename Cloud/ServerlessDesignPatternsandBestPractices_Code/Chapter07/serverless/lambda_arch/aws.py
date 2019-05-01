import boto3
import os

_clients = {}


def _get_client(service, **kwargs):
    global _clients
    client = _clients.get(service)
    if not client:
        client = boto3.client(service)
        _clients[service] = client

    return client


def write_to_dynamo_table(table_name, data):
    db = boto3.resource('dynamodb', region_name=os.environ['AWS_REGION'])
    table = db.Table(table_name)
    return table.put_item(Item=data)


def read_from_s3(bucket, key):
    client = _get_client('s3')
    return client.get_object(Bucket=bucket, Key=key)


def read_body_from_s3(bucket, key):
    obj = read_from_s3(bucket, key)
    return obj['Body'].read()


def write_to_s3(bucket, key, payload, **kwargs):
    client = _get_client('s3')
    return client.put_object(
            Body=payload.encode(),
            Bucket=bucket,
            Key=key,
            **kwargs,
    )


def get_matching_s3_objects(bucket, prefix='', suffix=''):
    s3 = _get_client('s3')
    kwargs = {'Bucket': bucket}

    # If the prefix is a single string (not a tuple of strings), we can
    # do the filtering directly in the S3 API.
    if isinstance(prefix, str):
        kwargs['Prefix'] = prefix

    while True:
        # The S3 API response is a large blob of metadata.
        # 'Contents' contains information about the listed objects.
        resp = s3.list_objects_v2(**kwargs)

        try:
            contents = resp['Contents']
        except KeyError:
            return

        for obj in contents:
            key = obj['Key']
            if key.startswith(prefix) and key.endswith(suffix):
                yield obj

        # The S3 API is paginated, returning up to 1000 keys at a time.
        # Pass the continuation token into the next response, until we
        # reach the final page (when this field is missing).
        try:
            kwargs['ContinuationToken'] = resp['NextContinuationToken']
        except KeyError:
            break


def get_matching_s3_keys(bucket, prefix='', suffix=''):
    for obj in get_matching_s3_objects(bucket, prefix, suffix):
        yield obj['Key']
