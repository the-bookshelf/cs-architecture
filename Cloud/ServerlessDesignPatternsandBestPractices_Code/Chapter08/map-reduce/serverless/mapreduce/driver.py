import time
import uuid

from .aws import (
        list_s3_bucket,
        publish_to_sns,
)


def crawl(bucket_name, prefix=''):
    """Entrypoint for a map-reduce job.

    The function is responsible for crawling a particular S3 bucket and publishing map jobs
    asyncrhonously using SNS where the mapping is 1-to-1, file-to-sns.

    It's presumed that lambda mapper functions are hooked up to the SNS topic. These Lambda mappers
    will each work on a particular file.

    """
    print('Starting at: %s: %s' % (time.time(), time.asctime(), ))

    # Unique identifer for the entire map-reduce run
    run_id = str(uuid.uuid4())
    mapper_data = [
            {
                'bucket': bucket,
                'job_id': str(uuid.uuid4()),
                'key': key,
                'run_id': run_id,
            } for (bucket, key) in list_s3_bucket(bucket_name, prefix)
    ]

    # Let's add in the total number of jobs which will be kicked off.
    num_mappers = len(mapper_data)

    for i, mapper_dict in enumerate(mapper_data):
        mapper_dict['total_jobs'] = num_mappers
        mapper_dict['job_id'] = i
        publish_to_sns(mapper_dict)
