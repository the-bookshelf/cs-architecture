# GETH - MAC OSX
brew tap ethereum/ethereum
brew install ethereum
# Uncomment to install development version
# brew install ethereum --devel

# GETH - LINUX
sudo apt-get install software-properties-common
sudo add-apt-repository -y ppa:ethereum/ethereum
sudo apt-get update
sudo apt-get install ethereum

# Verify GETH installation
geth version

# PARITY
bash <(curl https://get.parity.io -kL)

# Install GETH from source
git clone https://github.com/ethereum/go-ethereum
cd go-ethereum
make geth