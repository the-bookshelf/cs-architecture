import boto3
import base64

import sys
import os

key_id = os.envriron['KMS_KEY_ARN']
client = boto3.client('kms')

def encrypt():
    stuff = client.encrypt(KeyId=key_id, Plaintext='supersecret')
    binary_encrypted = stuff[u'CiphertextBlob']
    encrypted_password = base64.b64encode(binary_encrypted)

    print("Encrypted password:\n", encrypted_password.decode())


def decrypt():
    DB_PASSWORD = "AQICAHjgjPISkW/L824chyDIq2L43l5kDvqZM/+/CA8zfz94vQF2rARrvu2gDV9NtD/L7p0DAAAAaTBnBgkqhkiG9w0BBwagWjBYAgEAMFMGCSqGSIb3DQEHATAeBglghkgBZQMEAS4wEQQMmb9qYJVBCnFHDl/7AgEQgCY73OZuiIV6ygT0nmjdATy8a6xhZYKNjnVh8gIFzHYdD6QXqNJCmg=="

    print("Decrypt the secret password")
    binary_encrypted = base64.b64decode(DB_PASSWORD)
    results = client.decrypt(CiphertextBlob=binary_encrypted)
    b = results['Plaintext'].decode()
    print(b)


encrypt()
print()
decrypt()
