verifyTx(
    rawTransaction, // Raw transaction bytes - bytes
    transactionIndex, // Index of the transaction in Block - int256
    merkleSibling, // Merkle proof sibiling hashes - int256[]
    blockHash // hash of the transaction block - int256
) returns (uint256)

relayTx(
 rawTransaction, // Raw transaction bytes - bytes
 transactionIndex, // Index of the transaction in Block - int256
 merkleSibling, // Merkle proof sibiling hashes - int256[]
 blockHash, // hash of the transaction block - int256
 contractAddress // Address of the target contract
) returns (int256)

// Target contract function
// Will be invoked by relayTx
processTransaction(
 bytes rawTransaction, 
 uint256 transactionHash
) returns (int256)

// Get header
getBlockHeader(
    blockHash // Hash of the block
) returns (bytes)

// Set header
storeBlockHeader(
    blockHeader // raw block header - bytes
) returns (int256)

// Set multiple headers at once
bulkStoreHeader(
    bytesOfHeaders, // raw block headers one after another - bytes
    numberOfHeaders // number of headers
) returns (int256)


getFeeAmount(blockHash) returns (int256)

// Returns the hash of given block height
getBlockHash(blockHeight) returns (int256)

// Returns the difference between the chainwork
// of latest and 10th prior block
getAverageChainWork()

// Returns the hash of latest block
getBlockchainHead() returns (int256)

// Returns the block height of latest block
getLastBlockHeight() returns (int256)

// Store single block header and store its fee
storeBlockWithFee(blockHeader, fee) returns (int256)

// Sets fee and the recipient for the given block hash
changeFeeRecipient(blockHash, fee, recipient) returns (int256)

// Get the fee recipient for the given block hash
getFeeRecipient(blockHash) returns (int256)

// Get the amount of fee for changeFeeRecipient
getChangeRecipientFee() returns (int256)


var btcRelayAddr = "0x...";
var btcRelayAbi = [...<BTCRelay_ABI>...];

var btcRelay = web3.eth.contract(btcRelayAbi).at(btcRelayAddr);

btcRelay.verifyTx.call(
    transactionBytes,
    transactionIndex,
    merkleSibling,
    transactionBlockHash,
    { from: '0x..', ...}
);