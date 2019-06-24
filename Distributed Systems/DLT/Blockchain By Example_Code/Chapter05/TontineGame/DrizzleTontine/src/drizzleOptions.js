 
import Cplayer from './../build/contracts/Cplayer.json'
import Ctontine from './../build/contracts/Ctontine.json'

const drizzleOptions = {
  web3: {
    block: false,
    fallback: {
      type: 'ws',
      url: 'ws://127.0.0.1:7545'
    }
  },
  contracts: [
   
    Cplayer,
    Ctontine
  ],
  events: {
    //Cplayer: ['StorageSet'],
    Ctontine:['NewActivePlayerEv','eliminatedPlayerEv'],
  },
  polls: {
    accounts: 1500
  }
}

export default drizzleOptions
