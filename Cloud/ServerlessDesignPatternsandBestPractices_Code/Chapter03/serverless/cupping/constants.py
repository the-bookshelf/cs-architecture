import os
import boto3
from base64 import b64decode

POSTGRESQL = 'postgresql'
SQLITE = 'sqlite'

DB_ENGINE = os.environ.get('CUPPING_DB_ENGINE', POSTGRESQL)
DB_HOST = os.environ.get('CUPPING_DB_HOST', 'localhost')
DB_NAME = os.environ.get('CUPPING_DB_NAME', 'cupping')
DB_PASSWORD = os.environ.get('CUPPING_DB_PASSWORD', '')
DB_PORT = os.environ.get('CUPPING_DB_PORT', 5432)
DB_USERNAME = os.environ.get('CUPPING_DB_USERNAME', 'postgres')

# try:
#     encrypted_var = b64decode(os.environ.get('SECRET_VAR'))
#     if encrypted_var:
#         variable = boto3.client('kms').decrypt(
#                 CiphertextBlob=encrypted_var)['Plaintext']
#         print(variable)
# except Exception as e:
#     print(e)
