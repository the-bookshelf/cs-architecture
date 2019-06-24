sudo add-apt-repository -y ppa:ethereum/ethereum

sudo apt-get update
sudo apt-get install ethereum-swarm

swarm version

# Password will be asked and returns the address
geth account new

swarm --bzzaccount <account_address>

swarm --bzzaccount <account_address> --maxpeers 0

geth --exec "admin.nodeInfo.enode" attach ipc:/path/to/bzzd.ipc

geth --exec='admin.addPeer(<enode_address>)' attach ipc:/path/to/bzzd.ipc

swarm up ../filename.txt
# will return a hash value

# http://localhost:8500/bzz:/<hash_value>/

swarm --recursive up ./path/to/directory

swarm down bzz:/<hash_value>

swarm down --recursive bzz:/<hash>

curl -H "Content-Type: text/plain" --data "data_for_uploading" http://localhost:8500/bzz:/

// Linux
sudo apt-get install fuse
sudo modprobe fuse
sudo chown <username>:<groupname> /etc/fuse.conf
sudo chown <username>:<groupname> /dev/fuse

// MacOS
brew update
brew install caskroom/cask/brew-cask
brew cask install osxfuse

swarm fs mount --ipcpath <path-to-bzzd.ipc> <manifest-hash> <mount-point>