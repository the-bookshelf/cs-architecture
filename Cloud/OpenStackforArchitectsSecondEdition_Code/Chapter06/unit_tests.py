#!/usr/bin/env python

import unittest
import requests #http://docs.python-requests.org/en/latest
import json

KEYSTONE_URL = 'http://controller01:5000/v2.0/'
TENANT       = 'demo'
USERNAME     = 'demo'
PASSWORD     = 'secret'

class TestKeystone(unittest.TestCase):
    """Set of Keystone-specific tests"""

    def test_001_get_token(self):
        """Requirement 001: Authorization Token"""
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
            self.assertEqual(r.status_code, 200)
            token = r.json()['access']['token']['id']
            self.assertTrue(token)
        else:
            raise Exception(r.text)

if __name__ == '__main__':
    unittest.main()
