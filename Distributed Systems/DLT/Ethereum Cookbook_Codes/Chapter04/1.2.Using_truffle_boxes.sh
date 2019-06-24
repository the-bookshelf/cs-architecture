# Various truffle boxes
truffle unbox metacoin
truffle unbox react
truffle unbox webpack
truffle unbox react-uport


# Serves the application on localhost:3000
npm run start
# To run the smart contract test scripts
truffle test
# To run the jest based application tests
npm run test
# To build the application for production
npm run build


# Config while building own truffle box
# {
#     ignore: [ 'README.md' ],
#     hooks: {
#         'post-unpack': 'npm install'
#     },
#     commands: {
#         "Compile contracts": "truffle compile",
#         "Migrate contracts": "truffle migrate",
#         "Test Contracts": "truffle test",
#         "Test DApp": "npm test",
#         "Start development server": "npm run start",
#         "Build": "npm run build"
#     }
# }