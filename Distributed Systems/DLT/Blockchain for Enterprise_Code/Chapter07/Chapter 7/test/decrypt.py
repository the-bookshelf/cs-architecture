from npre import bbs98
pre = bbs98.PRE()
import base64
import sys

privateKey = base64.b64decode(sys.argv[1])
encrypted_message = base64.b64decode(sys.argv[2])

decrypted_message = pre.decrypt(privateKey, encrypted_message)


print(decrypted_message)
