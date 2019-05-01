import base64
import json
import os
import sys
import boto3
import time

from pathlib import Path

CWD = Path(__file__).resolve().cwd() / 'lib'
sys.path.insert(0, str(CWD))

from raven_python_lambda import RavenLambdaWrapper

import structlog
structlog.configure(
        processors=[structlog.processors.JSONRenderer()]
)
log = structlog.get_logger()


@RavenLambdaWrapper()
def divide(event, context):
    params = event.get('queryStringParameters') or {}
    numerator = int(params.get('numerator', 10))
    denominator = int(params.get('denominator', 2))

    log.msg('start', **params)

    body = {
        "message": "Results of %s / %s = %s" % (
            numerator,
            denominator,
            numerator // denominator,
        )
    }

    log.msg('finish',
            numerator=numerator,
            denominator=denominator,
            quotient=numerator // denominator)

    response = {
        "statusCode": 200,
        "body": json.dumps(body)
    }

    return response


state_variable = None


def process(event, context):
    global state_variable

    if not state_variable:
        print("Initializging state_variable")
        state_variable = 0

    print("Decrypt the secret password")
    client = boto3.client('kms')
    binary_encrypted = base64.b64decode(os.environ['DB_PASSWORD'])
    results = client.decrypt(CiphertextBlob=binary_encrypted)
    db_password = results['Plaintext'].decode()

    state_variable += 1
    return {
        'statusCode': 200,
        'body': json.dumps({
            'state_variable': state_variable,
            'db_password': db_password,
        }),
    }
