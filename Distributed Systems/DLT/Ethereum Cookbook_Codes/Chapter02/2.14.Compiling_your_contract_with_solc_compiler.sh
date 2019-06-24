# SAMPLE CONTRACT FILE - HelloWorld.sol
# pragma solidity ^0.4.23;
# contract HelloWorld {
#     string public greeting = "Hello World";
#     event greetingChanged(address by, string greeting);
#     function changeGreeting(string _newGreeting) public {
#         greeting = _newGreeting;
#         emit greetingChanged(msg.sender, _newGreeting);
#     }
# }

# Help document
solc --help

# Sample compilation
solc --bin --abi HelloWorld.sol

solc -o <path_to_output_directory> --bin --abi HelloWorld.soltory> --bin --abi HelloWorld.sol

solc github.com/OpenZeppelin/zeppelin-solidity=/usr/local/zeppelin =/usr/local/others contract.sol

solc --optimize --bin HelloWorld.sol


# --ast       AST of all source files.
# --asm       EVM assembly of the contracts.
# --opcodes   Opcodes of the contracts.
# --hashes    Function signature hashes of the contracts.
# --userdoc   Natspec user documentation of all contracts.
# --formal    Translated source suitable for formal analysis.