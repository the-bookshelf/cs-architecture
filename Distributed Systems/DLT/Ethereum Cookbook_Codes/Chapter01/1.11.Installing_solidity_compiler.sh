# UBUNTU
sudo add-apt-repository ppa:ethereum/ethereum
sudo apt-get update
sudo apt-get install solc

# MAC OS
brew update
brew tap ethereum/ethereum
brew install solidity

# Verify installation
solc --version

# Compile and print binary
solc --bin SampleContract.sol

# Compile and write output to files
solc -o outDirectory --bin --ast --asm --abi --opcodes SampleContract.sol

# If you need a JavaScript based compiler
# npm install -g solc
# solcjs --help