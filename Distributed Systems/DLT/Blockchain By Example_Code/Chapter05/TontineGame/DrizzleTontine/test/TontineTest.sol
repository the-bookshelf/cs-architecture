import "truffle/Assert.sol";
import "../contracts/tontine.sol";
import "truffle/DeployedAddresses.sol";

contract TontineTest {

uint public initialBalance = 10 ether;
Cplayer cplayer_;
Ctontine tontine;
 
function beforeEach() public {
cplayer_ = Cplayer(DeployedAddresses.Cplayer()); 
tontine = Ctontine(DeployedAddresses.Ctontine());
}

function testplayer() public{
 
cplayer_.AddPlayer("Player1",1234);
bool expected1=cplayer_.exist(this);
Assert.isTrue(expected1, "Player doesn't exist"); 

}

function testjoingame() public{
 
cplayer_.AddPlayer("Player1",1234);
uint expectedBalance = 2 ether;
tontine.join.value(2 ether)();
 
Assert.equal(expectedBalance, address(tontine).balance, "Contract balance should be 2 ether");  
}

}

