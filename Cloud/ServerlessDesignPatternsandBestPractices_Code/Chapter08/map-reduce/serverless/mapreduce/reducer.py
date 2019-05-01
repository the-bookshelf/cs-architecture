import csv
import json
import time

from .aws import (
        download_from_s3,
        list_s3_bucket,
        read_from_s3,
        s3_file_exists,
        write_to_s3,
        write_csv_to_s3,
)


def _get_final_results_key(run_id):
    return 'run-%s/FinalResults.csv' % (run_id, )


def _get_batch_job_prefix(run_id):
    return 'run-%s/mapper-' % (run_id, )


def _get_job_metadata(event):
    s3_record = event['Records'][0]['s3']
    bucket = s3_record['bucket']['name']
    key = s3_record['object']['key']

    s3_obj = read_from_s3(bucket, key)
    job_metadata = s3_obj['Metadata']

    run_id = job_metadata['run_id']
    total_jobs = int(job_metadata['total_jobs'])
    return (bucket, run_id, total_jobs)


def reduce(event):
    bucket, run_id, total_jobs = _get_job_metadata(event)

    # count up all of the final done files and make sure they equal the total number of mapper jobs
    prefix = _get_batch_job_prefix(run_id)
    final_files = [
            (bucket, key) for (_, key) in \
            list_s3_bucket(bucket, prefix) \
            if key.endswith('-done.csv')
    ]
    if len(final_files) != total_jobs:
        print(
            'Reducers are still running...skipping. Expected %d done files but found %s' % (
                total_jobs, len(final_files),
            )
        )
        return

    # Let's put a lock file here so we can claim that we're finishing up the final reduce step
    final_results_key = _get_final_results_key(run_id)
    if s3_file_exists(bucket, final_results_key):
        print('Skipping final reduce step')
        return

    # write blank file to lock the final reduce step
    write_to_s3(bucket, final_results_key, {})

    print('Starting final reduce phase')

    s3_mapper_files = list_s3_bucket(bucket, prefix)

    final_results = {}

    for (bucket, key) in s3_mapper_files:
        print('reading', key)

        tmp_fn = download_from_s3(bucket, key)

        with open(tmp_fn, 'r') as csv_fh:
            reader = csv.DictReader(csv_fh, fieldnames=('key', 'count'))
            for line in reader:
                key = line['key']
                count = int(line['count'])

                if key in final_results:
                    final_results[key] += count
                else:
                    final_results[key] = count

    print('Finished at: %s: %s' % (time.time(), time.asctime(), ))
    print('Final results length:', len(final_results))
    write_csv_to_s3(bucket, final_results_key, final_results)
