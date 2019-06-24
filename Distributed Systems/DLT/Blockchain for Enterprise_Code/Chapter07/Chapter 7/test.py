# Import bbs98 from NuCypher PRE
from npre import bbs98
# Initialize the re-encryption object
pre = bbs98.PRE()

# `sk` means “secret key”, and `pk` means “public key”

# Alice's Private key
sk_a = pre.gen_priv(dtype=bytes)
# Alice's Public Key
pk_a = pre.priv2pub(sk_a)

# Bob's Private Key
sk_b = pre.gen_priv(dtype=bytes)
# Bob's Public Key
pk_b = pre.priv2pub(sk_b)

# Print Alice's Private Key as Hex String
print(sk_a.hex()[2:])
# Print Bob's Private Key as Hex String
print(sk_b.hex()[2:])

# Encrypt Message using Alice's Public Key
emsg = pre.encrypt(pk_a, "Hello World")

# Generate Re-Encrypt Key using Private key of sender and receiver
re_ab = pre.rekey(sk_a, sk_b)
# Re-Encrypt Message using Re-Encrypt key
emsg_b = pre.reencrypt(re_ab, emsg)

# Decrypt the message using Bob's Private Key
dmsg = pre.decrypt(sk_b, emsg_b)
# Print Decrypted Message
print(dmsg.decode("utf-8"))
