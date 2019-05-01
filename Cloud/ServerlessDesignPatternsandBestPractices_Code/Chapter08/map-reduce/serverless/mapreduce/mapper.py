import csv
import itertools
import json
import os
import sys
import time

import email.parser

# Make sure we can read big csv files
csv.field_size_limit(sys.maxsize)

from .aws import (
        download_from_s3,
        write_csv_to_s3,
)


def _csv_lines_from_filepath(filepath, delete=True):
    with open(filepath, 'rt') as fh:
        reader = csv.DictReader(fh, fieldnames=('file', 'message'))
        for row in reader:
            yield row

    if delete:
        os.remove(filepath)


def map(event):
    message = json.loads(event['Records'][0]['Sns']['Message'])

    total_jobs = message['total_jobs']
    run_id = message['run_id']
    job_id = message['job_id']

    counts = {}

    bucket = 'brianz-dev-mapreduce-results'
    bucket = os.environ['REDUCE_RESULTS_BUCKET']

    tmp_file = download_from_s3(message['bucket'], message['key'])

    parser = email.parser.Parser()

    for line in _csv_lines_from_filepath(tmp_file):
        msg = line['message']
        eml = parser.parsestr(msg, headersonly=True)
        _from = eml['From']
        _tos = eml.get('To')

        if not _tos:
            continue

        _tos = (t.strip() for t in _tos.split(','))

        for from_to in itertools.product([_from], _tos):
            if from_to not in counts:
                counts[from_to] = 1
            else:
                counts[from_to] += 1

    if not counts:
        return

    metadata = {
            'job_id': str(job_id),
            'run_id': str(run_id),
            'total_jobs': str(total_jobs),
    }

    key = 'run-%s/mapper-%s-done.csv' % (run_id, job_id)
    write_csv_to_s3(bucket, key, counts, Metadata=metadata)
