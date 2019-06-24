// npm install -E openzeppelin-solidity

import 'openzeppelin-solidity/contracts/ownership/Ownable.sol';

contract NewContract is Ownable {
  // ...
}

import "github.com/OpenZeppelin/openzeppelin-solidity/contracts/ownership/Ownable.sol";

contract NewContract is Ownable {
  // ...
}

import "openzeppelin-solidity/contracts/access/SignatureBouncer.sol";
import "openzeppelin-solidity/contracts/access/Whitelist.sol";
import "openzeppelin-solidity/contracts/access/RBAC/..";
import "openzeppelin-solidity/contracts/crowdsale/...";
import "openzeppelin-solidity/contracts/introspection/...";
import "openzeppelin-solidity/contracts/lifecycle/...";
import "openzeppelin-solidity/contracts/math/...";
import "openzeppelin-solidity/contracts/ownership/...";
import "openzeppelin-solidity/contracts/payment/...";
import "openzeppelin-solidity/contracts/token/ERC20/...";
import "openzeppelin-solidity/contracts/token/ERC721/...";