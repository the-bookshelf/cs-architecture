#!/bin/bash

for NAME in Driver Mapper Reducer; do
GROUP_NAME="/aws/lambda/map-reduce-dev-$NAME"

aws logs describe-log-streams --log-group-name $GROUP_NAME --output text \
    | awk '{print $7}' \
    | while read x; \
        do aws logs delete-log-stream --log-group-name $GROUP_NAME --log-stream-name $x; \
      done

done
