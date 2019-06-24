function balanceOf(address _owner) external view returns (uint256);

mapping (address => uint256) internal ownedTokensCount;

function balanceOf(address _owner) public view returns (uint256) {
    require(_owner != address(0));
    return ownedTokensCount[_owner];
}

function ownerOf(uint256 _tokenId) external view returns (address);

mapping (uint256 => address) internal tokenOwner;

function ownerOf(uint256 _tokenId) public view returns (address) {
    address owner = tokenOwner[_tokenId];
    require(owner != address(0));
    return owner;
}

function exists(uint256 _tokenId) public view returns (bool) {
    address owner = tokenOwner[_tokenId];
    return owner != address(0);
}

modifier onlyOwnerOf(uint256 _tokenId) {
    require(ownerOf(_tokenId) == msg.sender);
    _;
}

pragma solidity ^0.4.23;

import "./SafeMath.sol";

contract BasicERC721 {
    using SafeMath for uint256;

    // Mapping from token ID to owner
    mapping (uint256 => address) internal tokenOwner;

    // Mapping from owner to number of owned token
    mapping (address => uint256) internal ownedTokensCount;

    /**
     * @dev Guarantees msg.sender is owner of the given token
     * @param _tokenId uint256 ID of the token
     */
    modifier onlyOwnerOf(uint256 _tokenId) {
        require(ownerOf(_tokenId) == msg.sender);
        _;
    }

    /**
     * @dev Gets the balance of the specified address
     * @param _owner address to query the balance of
     * @return uint256 represents the amount owned
     */
    function balanceOf(address _owner) public view 
        returns (uint256) {
        require(_owner != address(0));
        return ownedTokensCount[_owner];
    }

    /**
     * @dev Gets the owner of the specified token ID
     * @param _tokenId uint256 ID of the token to query the owner
     * @return owner address currently marked as the owner
     */
    function ownerOf(uint256 _tokenId) public view 
        returns (address) {
        address owner = tokenOwner[_tokenId];
        require(owner != address(0));
        return owner;
    }

    /**
     * @dev Returns whether the specified token exists
     * @param _tokenId uint256 ID of the token to query
     * @return whether the token exists
     */
    function exists(uint256 _tokenId) public view returns (bool) {
        address owner = tokenOwner[_tokenId];
        return owner != address(0);
    }

  /**
   * @dev Function to add a token ID
   * @param _to address of the new owner
   * @param _tokenId uint256 ID of the token to be added
   */
    function addTokenTo(address _to, uint256 _tokenId) internal {
        require(tokenOwner[_tokenId] == address(0));
        tokenOwner[_tokenId] = _to;
        ownedTokensCount[_to] = ownedTokensCount[_to].add(1);
    }

  /**
   * @dev Function to remove a token ID
   * @param _from address of the previous owner
   * @param _tokenId uint256 ID of the token to be removed
   */
    function removeTokenFrom(address _from, uint256 _tokenId) internal {
        require(ownerOf(_tokenId) == _from);
        ownedTokensCount[_from] = ownedTokensCount[_from].sub(1);
        tokenOwner[_tokenId] = address(0);
    }
}


string internal name_;
string internal symbol_;

constructor(string _name, string _symbol) public {
    name_ = _name;
    symbol_ = _symbol;
}

function name() public view returns (string) {
    return name_;
}

function symbol() public view returns (string) {
    return symbol_;
}

mapping(address => uint256[]) internal ownedTokens;

mapping(uint256 => uint256) internal ownedTokensIndex;

function addTokenTo(address _to, uint256 _tokenId) internal {
    // call the previous addToken method here
    uint256 length = ownedTokens[_to].length;
    ownedTokens[_to].push(_tokenId);
    ownedTokensIndex[_tokenId] = length;
}

function removeTokenFrom(address _from, uint256 _tokenId) internal {
    // call the previous removeToken method here
    uint256 tokenIndex = ownedTokensIndex[_tokenId];
    uint256 lastTokenIndex = ownedTokens[_from].length.sub(1);
    uint256 lastToken = ownedTokens[_from][lastTokenIndex];

    ownedTokens[_from][tokenIndex] = lastToken;
    ownedTokens[_from][lastTokenIndex] = 0;

    ownedTokens[_from].length--;
    ownedTokensIndex[_tokenId] = 0;
    ownedTokensIndex[lastToken] = tokenIndex;
}

uint256[] internal allTokens;

mapping(uint256 => uint256) internal allTokensIndex;

function totalSupply() public view returns (uint256) {
    return allTokens.length;
}

pragma solidity ^0.4.23;
import "./BasicERC721.sol";

contract ERC721Token is BasicERC721 {
    // Token name
    string internal name_;

    // Token symbol
    string internal symbol_;

    // Mapping from owner to list of owned token IDs
    mapping(address => uint256[]) internal ownedTokens;

    // Mapping from token ID to index of the owner tokens list
    mapping(uint256 => uint256) internal ownedTokensIndex;

    // Array with all token ids
    uint256[] internal allTokens;

    // Mapping from token id to position in the allTokens array
    mapping(uint256 => uint256) internal allTokensIndex;

    constructor(string _name, string _symbol) public {
        name_ = _name;
        symbol_ = _symbol;
    }

    /**
    * @dev Gets the token name
    * @return string representing the token name
    */
    function name() public view returns (string) {
        return name_;
    }

    /**
    * @dev Gets the token symbol
    * @return string representing the token symbol
    */
    function symbol() public view returns (string) {
        return symbol_;
    }

    /**
    * @dev Gets the token ID at a given index
    * @param _owner address owning the tokens list
    * @param _index uint256 representing the index
    * @return uint256 token ID at the given index of the tokens
    */
    function tokenOfOwnerByIndex(address _owner, uint256 _index)
        public view returns (uint256) {
        require(_index < balanceOf(_owner));
        return ownedTokens[_owner][_index];
    }

    /**
    * @dev Gets the total amount of tokens
    * @return uint256 representing the total amount
    */
    function totalSupply() public view returns (uint256) {
        return allTokens.length;
    }

    /**
    * @dev Gets the token ID at a given index
    * @param _index uint256 representing the index
    * @return uint256 token ID at the given index
    */
    function tokenByIndex(uint256 _index) public view 
        returns (uint256) {
        require(_index < totalSupply());
        return allTokens[_index];
    }

    /**
    * @dev Function to add a token ID
    * @param _to address representing the new owner
    * @param _tokenId uint256 ID of the token to be added
    */
    function addTokenTo(address _to, uint256 _tokenId) internal {
        super.addTokenTo(_to, _tokenId);
        uint256 length = ownedTokens[_to].length;
        ownedTokens[_to].push(_tokenId);
        ownedTokensIndex[_tokenId] = length;
    }

    /**
    * @dev Function to remove a token ID
    * @param _from address of the previous owner
    * @param _tokenId uint256 ID of the token to be removed
    */
    function removeTokenFrom(address _from, uint256 _tokenId) 
        internal {
        super.removeTokenFrom(_from, _tokenId);

        uint256 tokenIndex = ownedTokensIndex[_tokenId];
        uint256 lastTokenIndex = ownedTokens[_from].length.sub(1);
        uint256 lastToken = ownedTokens[_from][lastTokenIndex];

        ownedTokens[_from][tokenIndex] = lastToken;
        ownedTokens[_from][lastTokenIndex] = 0;
        ownedTokens[_from].length--;
        ownedTokensIndex[_tokenId] = 0;
        ownedTokensIndex[lastToken] = tokenIndex;
    }
}

mapping(uint256 => string) internal tokenURIs;

/**
 * @dev Returns an URI for a given token ID
 * @dev Throws if the token ID does not exist. May return an empty string.
 * @param _tokenId uint256 ID of the token to query
 */
function tokenURI(uint256 _tokenId) public view returns (string) {
    require(exists(_tokenId));
    return tokenURIs[_tokenId];
}

/**
 * @dev Internal function to set the token URI for a given token
 * @dev Reverts if the token ID does not exist
 * @param _tokenId uint256 ID of the token to set its URI
 * @param _uri string URI to assign
 */
function _setTokenURI(uint256 _tokenId, string _uri) internal {
    require(exists(_tokenId));
    tokenURIs[_tokenId] = _uri;
}
