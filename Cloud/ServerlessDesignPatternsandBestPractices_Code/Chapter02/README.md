# Serverless REST API

This is an example project of a serverless REST API using the Serverless Framework. This project
corresponds to Chapter 2 of 
[Serverless Design Patterns and Best Practices](https://www.packtpub.com/application-development/serverless-design-patterns-and-best-practices)

[![CircleCI](https://circleci.com/gh/brianz/serverless-design-patterns.svg?style=svg)](https://circleci.com/gh/brianz/serverless-design-patterns)

# Quickstart for API backend

*Note*: the quickstart will not work until you've set up the environment. See *Setting up
environment* below

- Create an environment file in the `envs` directory
- Start up the container
- Install requirements
- Run tests
- Deploy code

```bash
$ mkdir envs && touch envs/dev
$ ENV=dev make shell
root@72370b69f644:/code#
root@72370b69f644:/code# make libs
root@72370b69f644:/code# make tests
root@72370b69f644:/code# make deploy
```

# Setting up environment

Your environment file needs to have the following key with values. 

```
AWS_REGION=us-west-2
AWS_SECRET_ACCESS_KEY=
AWS_ACCESS_KEY_ID=

VPC_ID=
SUBNET_ID_A=
SUBNET_ID_B=
SUBNET_ID_C=

CUPPING_DB_USERNAME=root
CUPPING_DB_PASSWORD=asfasfsad3123aa
CUPPING_DB_NAME=cupping_log
```

It's easiest to use your `VPC_ID` and `SUBNET_ID` value from your default VPC. Make 
sure you set the `AWS_REGION` to whatever region you'll be using.

To get your default VPC and Subnets:

- go to https://console.aws.amazon.com/vpc/home
- click on `Your VPCs` on the left navigation
- copy the VPC ID which will have the format `VPC-b346eab3`
- click on `Subnets` on the left navigation
- copy your Subnet IDs which will have the format `SUBNET-12456c55`
    - If you only have two Subnets, simply remove `SUBNET_ID_C` from the environment file and from
      `serverless.yml`

You can set the `DB` fields to anything you'd like. These settings will be used when creating the
RDS instance on the initial deployment.


# Setting up front-end

The front-end code is based on create-react-app.

You will need to set the BASE_URL in `src/constants.js` to be the base URL of your deployed API
from the steps above.

To buid the UI:

```
$ cd ui
$ yarn && yarn start
```

Setting up the front-end application using CloudFormation is a bit more work. All of the instructions can be found in Chapter 2 under the section, _Setting up static assets_.

You will need to have your own domain setup in Route53 if you follow along with those steps.

**NOTE**, there is no UI which allows you to create new cupping sessionf from the UI. In order to
creat new data you'll need to use the raw API. You can do this with CURL, Postman or any other HTTP
tool of choice. A CURL example is shown below. Simply change the URL and it should work for you:

```bash
curl -X POST \
  https://ffej1idill.execute-api.us-west-2.amazonaws.com/dev/session \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Friday AM session",
  "formName": "Modified COE",
  "cuppings": [
    {
      "name": "Guat Huehuetenango",
      "overallScore": "88.5",
      "scores": {
        "Aroma": 8.6,
        "Body": 8,
        "Flavor": 10
      },
      "notes": "This was pretty good",
      "descriptors": ["woody", "berries"]
    },
    {
      "name": "Ethiopia Yiracheffe",
      "overallScore": "90",
      "scores": {
        "Aroma": 8.6,
        "Body": 8,
        "Flavor": 10
      },
      "notes": "",
      "descriptors": ["blueberry"]
    }
  ]
}'
```

# Destroying the stack

From within the Docker container:

```bash
root@675d2d42e460:/code# cd serverless/
root@675d2d42e460:/code/serverless# sls remove -s $ENV  
```
