import boto3
import botocore
import csv
import glob
import os
import json
import tempfile

from .constants import SNS_MAPPER_ARN

_clients = {}


def _get_client(service):
    global _clients
    client = _clients.get(service)
    if not client:
        client = boto3.client(service)
        _clients[service] = client

    return client


def publish_to_sns(payload):
    client = _get_client('sns')
    client.publish(
            TargetArn=SNS_MAPPER_ARN,
            Message=json.dumps({'default': json.dumps(payload)}),
            MessageStructure='json',
    )


def download_from_s3(bucket, key, s3=None):
    for fn in glob.glob('/tmp/aws-*'):
        os.remove(fn)

    s3 = s3 or boto3.resource('s3')
    tmp = tempfile.NamedTemporaryFile(
            mode='w+t',
            prefix='aws-',
            dir='/tmp',
            delete=False)
    tmp.close()
    s3.Bucket(bucket).download_file(key, tmp.name)
    return tmp.name


def s3_file_exists(bucket, key):
    s3 = boto3.resource('s3')

    try:
        s3.Object(bucket, key).load()
    except botocore.exceptions.ClientError as e:
        if e.response['Error']['Code'] == "404":
            return False
        else:
            print(e.response)
            return False

    return True


def read_from_s3(bucket, key):
    client = _get_client('s3')
    return client.get_object(Bucket=bucket, Key=key)


def write_to_s3(bucket, key, payload, **kwargs):
    tmp = tempfile.NamedTemporaryFile(mode='w+t', prefix='aws-', dir='/tmp')
    json.dump(payload, tmp)
    tmp.flush()
    filename = tmp.name

    s3 = boto3.resource('s3')
    s3.meta.client.upload_file(filename, bucket, key, ExtraArgs=kwargs)

    tmp.close()


def write_csv_to_s3(bucket, key, payload, **kwargs):
    tmp = tempfile.NamedTemporaryFile(mode='w+t', prefix='aws-', dir='/tmp')
    w = csv.writer(tmp)
    w.writerows(payload.items())
    tmp.flush()

    filename = tmp.name

    s3 = boto3.resource('s3')
    s3.meta.client.upload_file(filename, bucket, key, ExtraArgs=kwargs)

    tmp.close()


def list_s3_bucket(name, prefix=None, suffix=None):
    s3 = boto3.resource('s3')
    bucket = s3.Bucket(name=name)
    results = bucket.objects.filter(Prefix=prefix)
    return [(r.bucket_name, r.key) for r in results]
