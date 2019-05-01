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

# Find the link to the Nova API in the service catalog:
for service in r.json()['access']['serviceCatalog']:
    if service['type'] == 'compute':
        # There may be more than one endpoint, but we'll take the first
        nova_endpoint = service['endpoints'][0]['publicURL']
    if service['type'] == 'network':
        neutron_endpoint = service['endpoints'][0]['publicURL']

keypair_ref = None
# Get the list of available keypairs:
keypairs = requests.get("%s/os-keypairs"% nova_endpoint, headers=headers)
# take the first one
keypair_ref = keypairs.json()['keypairs'][0]['keypair']['name']


image_ref = None
# Get the list of available images:
images = requests.get("%s/images"% nova_endpoint, headers=headers)
# Select the "cirros" image:
for image in images.json()['images']:
    if image['name'] == 'cirros':
        image_ref = image['id']


flavor_ref = None
# Get the list of available flavors:
flavors = requests.get("%s/flavors"% nova_endpoint, headers=headers)
# Select the "m1.small" flavor
for flavor in flavors.json()['flavors']:
    if flavor['name'] == 'm1.small':
        flavor_ref = flavor['id']

network_ref = None
# Get the list of available networks:
networks = requests.get("%s/v2.0/networks"% neutron_endpoint, headers=headers)
# Select the "management" network
for network in networks.json()['networks']:
    if network['name'] == 'management':
        network_ref = network['id']

# Launch the instance:
server = requests.post("%s/servers"% (nova_endpoint,), json={
        "server": {
            "name": "new_server",
            "imageRef": image_ref,
            "flavorRef": flavor_ref,
            "key_name": keypair_ref,
            "networks": [
                { "uuid": network_ref }
                ]
            }
        }, headers=headers)
server_ref = server.json()['server']['id']

