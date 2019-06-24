import React, { Component } from 'react'
import { AccountData, ContractData, ContractForm } from 'drizzle-react-components'
import PropTypes from 'prop-types'
import logo from '../../logo.png'
 
class Home extends Component {


 constructor(props, context) {
    super(props)
  this.contracts = context.drizzle.contracts
 
  }

  render() {
 
    return (
      <main className="container">
        <div className="pure-g">
          <div className="pure-u-1-1 header">
            <img src={logo} alt="drizzle-logo" />
            <h1>Tontine Game</h1>
            <p>Examples of how to get started with Drizzle in various situations.</p>
          </div>
 

          <div className="pure-u-1-1">
            <h2>Active Account</h2>
            <strong>My details:  </strong>
            <AccountData accountIndex="0" units="ether" precision="3" />


          </div>

          <div className="pure-u-1-1">

            <h2>Cplayer Contract</h2>  
            <ContractData contract="Cplayer" method="findplayer" methodArgs={[this.props.accounts[0]]} />

<h3>Register</h3>
            <p>Before start playing, players should register themselves using AddPlayer from.</p>
      
            <ContractForm contract="Cplayer" method="AddPlayer" />

            <br/><br/>
          </div>

 


          <div className="pure-u-1-1">
            <h2>Ctontine</h2>           
           <strong>Last Ping:  </strong><ContractData contract="Ctontine" method="ping_time" methodArgs={[this.props.accounts[0]]} />
<br/>
 
           <strong>Your Game pension:  </strong><ContractData contract="Ctontine" method="Tpension"   methodArgs={[this.props.accounts[0]]} />

 <h3>join game</h3>
            <p>Press the button below to join the game (only the first time)</p>


            <ContractForm contract="Ctontine" method="join" methodArgs={[{value: this.context.drizzle.web3.utils.toWei('2','ether'),from: this.props.accounts[0]}]} />
            <br/> <br/>

          <strong>Ping game:  </strong>   
                    <p>Keep pinging the contract to avoid being eliminated (ping interval is 1 day)</p>
 <br/><ContractForm contract="Ctontine" method="ping" methodArgs={[{from: this.props.accounts[0],data:1}]} />
   <h3>Eliminate an opponent</h3>
                       <p>use this form to eliminate your opponent</p>

            <ContractForm contract="Ctontine" method="eliminate"  labels={['Opponent Address']} />
<br/>

 <h3>Claim your reward</h3>

            <ContractForm contract="Ctontine" method="claimReward" />
<br/>

          </div>


        </div>
        <br/>
  <h2>First Active players</h2><ContractData contract="Ctontine" method="active_players" methodArgs={"0"} />
  <h2>First Eliminated players</h2><ContractData contract="Ctontine" method="eliminated_players" methodArgs={"0"} />

      </main>
    )
  }
}
Home.contextTypes = {
  drizzle: PropTypes.object
}
export default Home
