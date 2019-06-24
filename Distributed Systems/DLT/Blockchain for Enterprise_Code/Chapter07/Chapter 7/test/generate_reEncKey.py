from npre import bbs98
pre = bbs98.PRE()
import base64
import sys

base64_privateKeyA = base64.b64decode(sys.argv[1])
base64_privateKeyB = base64.b64decode(sys.argv[2])

re_ab = pre.rekey(base64_privateKeyA, base64_privateKeyB)

print(base64.b64encode(re_ab))
