import json

import os
import urllib2
import sys

from pprint import pprint as pp

CWD = os.path.dirname(os.path.realpath(__file__))
sys.path.insert(0, os.path.join(CWD, "lib"))


from firehose.queue import classify_photos
from firehose.stream_listener import PhotoStreamListener


def firehose(event, context):
    PhotoStreamListener.start()


def classify(event, context):
    classify_photos()
