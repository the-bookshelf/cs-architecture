from npre import bbs98
pre = bbs98.PRE()
import base64
import sys

publicKey = base64.b64decode(sys.argv[1])
encrypted_message = pre.encrypt(publicKey, sys.argv[2])

print(base64.b64encode(encrypted_message))
