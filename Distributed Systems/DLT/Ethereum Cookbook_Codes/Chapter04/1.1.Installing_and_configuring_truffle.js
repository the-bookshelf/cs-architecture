// To install and create a project in Truffle
// npm install -g truffle
// mkdir truffle_project
// cd truffle_project
// truffle init

// Contents of truffle.js file
module.exports = {
    networks: {
        development: {
            host: "localhost",
            port: 8545,
            network_id: "*" // Match any network id
        }
    }
};