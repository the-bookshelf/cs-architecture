# Get Block number usign JSON-RPC and infura
curl -X POST -H "Content-Type: application/json" --data '{"jsonrpc": "2.0", "id": 1, "method": "eth_blockNumber", "params": []}' "https://mainnet.infura.io/<your-api-key>"

# {"jsonrpc":"2.0","id":1,"result":"0x512bab"}
# web3 = new Web3(new Web3.providers.HttpProvider ("https://mainnet.infura.io/<your_api_key>"));