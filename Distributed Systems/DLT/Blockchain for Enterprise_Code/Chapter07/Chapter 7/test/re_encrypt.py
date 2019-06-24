from npre import bbs98
pre = bbs98.PRE()
import base64
import sys

reEncryptKey = base64.b64decode(sys.argv[1])
encrypted_message = base64.b64decode(sys.argv[2])
re_encrypted_message = pre.reencrypt(reEncryptKey, encrypted_message)

print(base64.b64encode(re_encrypted_message))
