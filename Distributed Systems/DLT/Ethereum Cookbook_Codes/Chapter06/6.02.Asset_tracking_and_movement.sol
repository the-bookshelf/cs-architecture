pragma solidity ^0.4.23;
import "./BasicERC721.sol";

contract ERC721Token {
    //...
}

// Mapping from token ID to approved address
mapping (uint256 => address) internal tokenApprovals;

// Mapping from owner to operator approvals
mapping (address => mapping (address => bool)) internal operatorApprovals;

function getApproved(uint256 _tokenId) public view 
    returns (address) {
    return tokenApprovals[_tokenId];
}

function isApprovedForAll(address _owner, address _operator)
    public view returns (bool) {
    return operatorApprovals[_owner][_operator];
}

// Transfer event
event Transfer(address indexed _from, 
    address indexed _to, uint256 _tokenId);
// Approval event for a specific token
event Approval(address indexed _owner, 
    address indexed _approved, uint256 _tokenId);
// Approval event for all tokens
event ApprovalForAll(address indexed _owner, 
    address indexed _operator, bool _approved);
    
function approve(address _to, uint256 _tokenId) public {
    address owner = ownerOf(_tokenId);
    require(_to != owner);
    require(msg.sender == owner || 
            isApprovedForAll(owner, msg.sender));

    if (getApproved(_tokenId) != address(0) || _to != address(0)) {
        tokenApprovals[_tokenId] = _to;
        emit Approval(owner, _to, _tokenId);
    }
}

function setApprovalForAll(address _to, bool _approved) public {
    require(_to != msg.sender);
    operatorApprovals[msg.sender][_to] = _approved;
    emit ApprovalForAll(msg.sender, _to, _approved);
}

function clearApproval(address _owner, uint256 _tokenId) internal {
    require(ownerOf(_tokenId) == _owner);
    if (tokenApprovals[_tokenId] != address(0)) {
        tokenApprovals[_tokenId] = address(0);
        emit Approval(_owner, address(0), _tokenId);
    }
}

modifier canTransfer(uint256 _tokenId) {
    require(isApprovedOrOwner(msg.sender, _tokenId));
    _;
}

function isApprovedOrOwner(address _spender, uint256 _tokenId)
    internal view returns (bool) {
    address owner = ownerOf(_tokenId);
    return (
        _spender == owner ||
        getApproved(_tokenId) == _spender ||
        isApprovedForAll(owner, _spender)
    );
}

function transferFrom(address _from, address _to, uint256 _tokenId)
    public canTransfer(_tokenId) {
    require(_from != address(0));
    require(_to != address(0));

    clearApproval(_from, _tokenId);
    removeTokenFrom(_from, _tokenId); // From BasicERC721.sol
    addTokenTo(_to, _tokenId); // From BasicERC721.sol
    emit Transfer(_from, _to, _tokenId);
 }
 
 function checkAndCallSafeTransfer(address _from, address _to, 
    uint256 _tokenId, bytes _data) internal returns (bool) {
    uint256 size;
    assembly { size := extcodesize(_to) }
    if (size == 0) {
        return true;
    }

    bytes4 retval = ERC721Receiver(_to)
        .onERC721Received(_from, _tokenId, _data);
    return (retval == ERC721_RECEIVED);
}

function safeTransferFrom(address _from, address _to, 
    uint256 _tokenId, bytes _data) 
    public canTransfer(_tokenId) {
    transferFrom(_from, _to, _tokenId);
    require(checkAndCallSafeTransfer(_from, _to, _tokenId, _data));
}


contract ERC721Receiver {
    bytes4 constant ERC721_RECEIVED = 0xf0b9e5ba;

    /**
     * @notice Handle the receipt of an NFT
     * @param _from The sending address
     * @param _tokenId The NFT identifier which is being transfered
     * @param _data Additional data with no specified format
     * @return `bytes4(keccak256("onERC721Received(...)"))`
     */
    function onERC721Received(address _from, uint256 _tokenId, 
        bytes _data) public returns(bytes4);
}



pragma solidity ^0.4.23;
import "./BasicERC721.sol";
import "./SafeMath.sol";

contract ERC721Token is BasicERC721 {
    using SafeMath for uint256;
    
    // Equals to `bytes4(keccak256("onERC721Received(address,uint256,bytes)"))`
    // which can be also obtained as`ERC721Receiver(0).onERC721Received.selector`
    bytes4 constant ERC721_RECEIVED = 0xf0b9e5ba;
    
    mapping (uint256 => address) internal tokenApprovals;
    mapping (address => mapping (address => bool))
    
    internal operatorApprovals;
    
    modifier canTransfer(uint256 _tokenId) {
        require(isApprovedOrOwner(msg.sender, _tokenId));
        _;
    }

    function approve(address _to, uint256 _tokenId) public {
        address owner = ownerOf(_tokenId);

        require(_to != owner);
        require(msg.sender == owner || isApprovedForAll(owner, msg.sender));
        
        if (getApproved(_tokenId) != address(0) || _to != address(0)) {
            tokenApprovals[_tokenId] = _to;
            emit Approval(owner, _to, _tokenId);
        }
    }

    function getApproved(uint256 _tokenId) public view returns (address) {
        return tokenApprovals[_tokenId];
    }

    function setApprovalForAll(address _to, bool _approved) public {
        require(_to != msg.sender);
        operatorApprovals[msg.sender][_to] = _approved;
        emit ApprovalForAll(msg.sender, _to, _approved);
    }
    
    function isApprovedForAll(address _owner, address _operator) public view returns (bool) {
        return operatorApprovals[_owner][_operator];
    }

    function transferFrom(address _from, address _to, uint256 _tokenId) public canTransfer(_tokenId) {
        require(_from != address(0));
        require(_to != address(0));

        clearApproval(_from, _tokenId);
        removeTokenFrom(_from, _tokenId);
        addTokenTo(_to, _tokenId);
        
        emit Transfer(_from, _to, _tokenId);
    }

    function safeTransferFrom(address _from, address _to, uint256 _tokenId, bytes _data) public canTransfer(_tokenId) {
        transferFrom(_from, _to, _tokenId);
        require(checkAndCallSafeTransfer(_from, _to, _tokenId, _data));
    }

    function isApprovedOrOwner(address _spender, uint256 _tokenId) internal view returns (bool) {
        address owner = ownerOf(_tokenId);
        return (_spender == owner || getApproved(_tokenId) == _spender || isApprovedForAll(owner, _spender));
    }

    function clearApproval(address _owner, uint256 _tokenId) internal {
        require(ownerOf(_tokenId) == _owner);
        if (tokenApprovals[_tokenId] != address(0)) {
            tokenApprovals[_tokenId] = address(0);
            emit Approval(_owner, address(0), _tokenId);
        }
    }

    function checkAndCallSafeTransfer(address _from, address _to, uint256 _tokenId, bytes _data) internal returns (bool) {
        uint256 size;
       
        assembly { 
            size := extcodesize(_to) 
        }
        
        if (size == 0) { 
            return true; 
        }
        
        bytes4 retval = ERC721Receiver(_to).onERC721Received(_from, _tokenId, _data);

        return (retval == ERC721_RECEIVED);
    }
}
