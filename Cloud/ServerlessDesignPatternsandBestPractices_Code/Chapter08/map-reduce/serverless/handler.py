import sys

from pathlib import Path

# Munge our sys path so libs can be found
CWD = Path(__file__).resolve().cwd() / 'lib'
sys.path.insert(0, str(CWD))

import mapreduce.driver
import mapreduce.mapper
import mapreduce.reducer


def driver(event, context):
    prefix = event or 'tiny'

    bucket_name = 'brianz-mapreduce-enron-emails'

    if prefix == 'large':
        mapreduce.driver.crawl(bucket_name, prefix='')
    else:
        mapreduce.driver.crawl(bucket_name, prefix='xan')


def mapper(event, context):
    mapreduce.mapper.map(event)


def reducer(event, context):
    mapreduce.reducer.reduce(event)


def final_reducer(event, context):
    mapreduce.reducer.final_reducer(event)
