import sys

from pathlib import Path

# Munge our sys path so libs can be found
CWD = Path(__file__).resolve().cwd() / 'lib'
sys.path.insert(0, str(CWD))

import simplejson as json

from cupping.handlers.graphql import handle_graphql

CORS_HEADERS = {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Credentials': True
}

def graphql(event, context):
    http_method = event['httpMethod']

    response = handle_graphql(http_method, event)
    status_code = 200

    response = {
        'statusCode': status_code,
        'body': json.dumps(response),
        'headers': CORS_HEADERS,
    }


    return response
