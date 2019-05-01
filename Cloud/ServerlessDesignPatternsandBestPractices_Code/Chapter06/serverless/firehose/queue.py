import boto3
import json
import os
import urllib2

from decimal import Decimal


TWITTER_STREAM_QUEUE_NAME = os.environ['TWITTER_STREAM_QUEUE_NAME']

from .storage import ClassiferResults

_sqs_client = None
_s3_client = None
_sqs_url = None


def get_sqs_client():
    global _sqs_client
    if _sqs_client is None:
        _sqs_client = boto3.client('sqs')
    return _sqs_client


def get_queue_url():
    global _sqs_url
    if _sqs_url is None:
        client = get_sqs_client()
        response = client.get_queue_url(QueueName=TWITTER_STREAM_QUEUE_NAME)
        _sqs_url = response['QueueUrl']
    return _sqs_url


def publish_tweet(payload):
    msg = json.dumps(payload)
    client = get_sqs_client()
    sqs_url = get_queue_url()

    return client.send_message(
                QueueUrl=sqs_url,
                MessageBody=msg)


def classify_photos():
    rekognition = boto3.client('rekognition')
    sqs = get_sqs_client()
    sqs_url = get_queue_url()

    while True:
        response = sqs.receive_message(
            QueueUrl=sqs_url,
            MaxNumberOfMessages=10,
        )
        messages = response.get('Messages')
        if not messages:
            break

        for msg in messages:
            receipt = msg['ReceiptHandle']
            body = json.loads(msg['Body'])

            url = body['url']

            # first check if we already have this image
            classifier_store = ClassiferResults(url=url)
            if classifier_store.exists:
                print 'Deleting queue item due to duplicate image'
                sqs.delete_message(QueueUrl=sqs_url, ReceiptHandle=receipt)
                continue

            print 'Opening url', url
            image_response = urllib2.urlopen(url)

            print 'Performing rekognition labeling'
            results = rekognition.detect_labels(Image={'Bytes': image_response.read()})

            scores = [{
                'Confidence': Decimal(l['Confidence']),
                'Name': l['Name'],
            } for l in results['Labels']]

            classifier_store.upsert(
                    text=body['text'],
                    hashtags=body['hashtags'],
                    scores=scores,
                    labels=[l['Name'] for l in results['Labels']],
            )

            sqs.delete_message(QueueUrl=sqs_url, ReceiptHandle=receipt)
