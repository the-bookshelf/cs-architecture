 
var Ctontine = artifacts.require("Ctontine");
var Cplayer = artifacts.require("Cplayer");
module.exports = function(deployer) {
 


    deployer.deploy(Cplayer).then(function() {
        return deployer.deploy(Ctontine, Cplayer.address);
    }).then(function() { })

};
