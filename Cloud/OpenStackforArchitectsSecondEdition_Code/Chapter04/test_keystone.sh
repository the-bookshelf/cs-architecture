curl -i \
  -H "Content-Type: application/json" \
  -d '
{ "auth": {
    "tenantId": "demo",
    "passwordCredentials": {
      "userId": "demo",
      "password": "secret"
    }
  }
}' \
http://controller01:5000/v2/auth/tokens ; echo
