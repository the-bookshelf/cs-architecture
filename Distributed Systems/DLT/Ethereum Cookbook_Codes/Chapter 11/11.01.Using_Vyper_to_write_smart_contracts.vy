# virtualenv -p python3.6 --no-site-packages ~/vyper-venv
# source ~/vyper-venv/bin/activate

# git clone https://github.com/ethereum/vyper.git
# cd vyper
# make
# make test

# vyper examples/name_registry.vy

# vyper -f ['abi', 'json', 'bytecode'] fileName.vy


beneficiary: address
index: int128

# syntax
# <struct_name>: {
#     <value>: <data_type>,
#     <value>: <data_type>,
#     ...
# }

# example
voters: {
    isVoted: bool,
    delegate: address
}

# syntax
# <mapping_name>: <value_type>[<key_type>]

# example
isAllowed: address[bool]

userDetails: {
    name: bytes32,
    age: int128    
}[address]

@public
@payable
def function_name():
    ...


@public
@payable
def buy():
    self.buyer = msg.sender

@public
@payable
def __init__():
    self.value = msg.value
    self.owner = msg.sender

@public
@payable
def __default__():
    ...

Deposit: event({value: int128, from: indexed(address)})

@public
@payable
def invest():
    log.Deposit(msg.value, msg.sender)

@public
def doSomething():
    assert not self.isAllowed
    assert msg.sender == self.isOwner
    ...