// npm install --save drizzle

import { Drizzle, generateStore } from 'drizzle';
import TokenContract from './../build/contracts/TokenContract.json'


const options = {
    contracts,
    events: {
        contractName: [
            eventName
        ]
    },
    polls: {
        accounts: interval,
        blocks: interval
    },
    web3: {
        fallback: {
            type,
            url
        }
    }
}

import TokenContract from './../build/contracts/TokenContract.json'

const options = { 
    contracts: [ TokenContract ]
}

const drizzleStore = generateStore(this.props.options)
const drizzle = new Drizzle(this.props.options, drizzleStore)

var state = drizzle.store.getState()

// Continue if Drizzle is initialized.
if (state.drizzleStatus.initialized) {

     // Call which is cached and synchronized. 
     // We'll receive the store key for recall.
     const key = drizzle.contracts.TokenContract
                        .methods.getOwner.cacheCall()

    // Use the dataKey to display data from the store.
    return state.contracts.TokenContract
                .methods.getOwner[key].value
}

// Display a loading message if Drizzle isn't initialized.
return 'Loading...'



var state = drizzle.store.getState()

// Continue if Drizzle is initialized.
if (state.drizzleStatus.initialized) {

     // Declare this transaction to be observed. 
     // We'll receive the stackId for reference.
     const stackId = drizzle.contracts.TokenContract
                            .methods.transferTokens
                            .cacheSend("0x2", 100, {
                                from: '0x1...'
                            })

    // Use the dataKey to display the transaction status.
    if (state.transactionStack[stackId]) {
        const txHash = state.transactionStack[stackId]
        return state.transactions[txHash].status
     }
}

// Display a loading message if Drizzle isn't initialized.
return 'Loading...'


// {
//     accounts,
//     accountBalances: {}
//     contracts: {
//         contractName: {
//             initialized,
//             synced,
//             events,
//             callerFunctionName: { }
//         }
//     },
//     transactions: { txHash: { } },
//     transactionStack
//     drizzleStatus: { }
//     web3: { }
// }

// npm install --save drizzle-react-components