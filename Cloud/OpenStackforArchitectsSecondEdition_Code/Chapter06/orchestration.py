#!/usr/bin/env python

import requests #http://docs.python-requests.org/en/latest
import json

KEYSTONE_URL = 'http://controller01:5000/v2.0/'
TENANT       = 'demo'
USERNAME     = 'demo'
PASSWORD     = 'secret'

r = requests.post("%s/tokens"% (KEYSTONE_URL,), json={
        "auth": {
            "tenantName": TENANT,
            "passwordCredentials": {
                "username": USERNAME,
                "password": PASSWORD,
                }
            }
        })

if r.ok:
    token = r.json()['access']['token']['id']
else:
    raise Exception(r.text)

# Place the token in the X-Auth-Token header
headers = {'X-Auth-Token': token}

# Find the link to the Heat API in the service catalog:
for service in r.json()['access']['serviceCatalog']:
    if service['type'] == 'orchestration':
        # There may be more than one endpoint, but we'll take the first
        heat_endpoint = service['endpoints'][0]['publicURL']

stack = requests.post("%s/stacks"% (heat_endpoint,), json={
        "stack_name": "new_server",
        "template": {
            "heat_template_version": "2013-05-23",
            "resources": {
                "new_server_port": {
                    "type": "OS::Neutron::Port",
                    "properties": {
                        "network": "management"
                        }
                    },
                "new_server": {
                    "type": "OS::Nova::Server",
                    "properties": {
                        "key_name": "demo",
                        "flavor": "m1.small",
                        "image": "cirros",
                        "networks": [ {
                                "port": {
                                    "get_resource": "new_server_port"
                                    }
                                } ]
                        }
                    }
                }
            }
        }, headers=headers)

