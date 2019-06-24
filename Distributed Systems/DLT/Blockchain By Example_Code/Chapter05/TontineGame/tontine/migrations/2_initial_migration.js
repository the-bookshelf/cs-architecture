 const Hello= artifacts.require("HelloWorld")
module.exports = function(deployer) {
	deployer.deploy(Hello);
}; 
