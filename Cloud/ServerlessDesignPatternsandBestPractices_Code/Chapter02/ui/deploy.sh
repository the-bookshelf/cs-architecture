#!/bin/bash

aws s3 cp --recursive --acl public-read build/ s3://cupperslog-s3bucketforstaticcontent-nswgo5ega4r1
