#!/usr/bin/env python
# -*- coding: utf-8 -*-
import boto3
import json

from pprint import pprint as pp
from websocket import WebSocketApp


URL = "wss://ws-feed.gdax.com"
kinesis = boto3.client('kinesis')


def on_message(_, msg):
    json_msg = json.loads(msg)
    if 'time' in json_msg:
        print('Publishing...')
        response = kinesis.put_record(
                StreamName='brianz-gdax-bz-kinesis-stream',
                PartitionKey=json_msg['time'],
                Data=msg + '|||',
        )
        pp(response)
    else:
        pp(json_msg)


def on_open(socket):
    """Callback executed at socket opening.

    Keyword argument:
    socket -- The websocket itself
    """

    products = ["BTC-USD", "ETH-USD"]
    channels = [
        {
            "name": "ticker",
            "product_ids": products,
        },
    ]

    params = {
        "type": "subscribe",
        "channels": channels,
    }
    socket.send(json.dumps(params))


def main():
    """Main function."""
    ws = WebSocketApp(URL, on_open=on_open, on_message=on_message)
    ws.run_forever()


if __name__ == '__main__':
    main()
