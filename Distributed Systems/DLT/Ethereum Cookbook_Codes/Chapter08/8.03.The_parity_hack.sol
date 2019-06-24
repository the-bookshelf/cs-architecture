function initWallet(
    address[] _owners, 
    uint _required, 
    uint _daylimit) 
{
    initDaylimit(_daylimit); 
    initMultiowned(_owners, _required);
}

function() payable {
    if (msg.value > 0)
        Deposit(msg.sender, msg.value);
    else if (msg.data.length > 0)
        _walletLibrary.delegatecall(msg.data);
}

function kill(address _to) 
    onlymanyowners(sha3(msg.data)) 
    external {
    suicide(_to);
}

function execute(
    address _to, 
    uint256 _value, 
    bytes _data) {
    //...
}