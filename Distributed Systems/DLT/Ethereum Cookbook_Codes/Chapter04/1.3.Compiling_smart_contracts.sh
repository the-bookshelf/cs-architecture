# Compile only the contracts that have been modified since last compile
truffle compile

# Compile all contracts
truffle compile --all

# Compiler output
# {
#     "contractName": "",
#     "abi": [],
#     "bytecode": "",
#     "deployedBytecode": "",
#     "sourceMap": "",
#     "deployedSourceMap": "",
#     "source": "",
#     "sourcePath": "",
#     "ast": {},
#     "legacyAST": {},
#     "compiler": {
#         "name": "",
#         "version": ""
#     },
#     "networks": {},
#     "schemaVersion": "",
#     "updatedAt": ""
# }

import "<path_to_the_contract.sol>";

import "packageName/contract.sol";