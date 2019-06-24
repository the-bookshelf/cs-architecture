        <p>
              <strong>exist</strong>:{" "}
              <ContractData
                contract="Cplayer"
                method="exist"
                methodArgs={[accounts[0]]}
              />
              {" "}
              <ContractData
                contract="Cplayer"
                method="exist"
                hideIndicator              />
            </p>

           <strong>My Details:  </strong><ContractData contract="Cplayer" method="findplayer" methodArgs={[this.props.accounts[0]]} />



            <header className="App-header">
            <h1 className="App-title">Tutorial Token</h1>
            <p>
              <strong>exist</strong>:{" "}
              <ContractData
                contract="Ctontine"
                method="exist"
                methodArgs={[accounts[0]]}
              />
                   {" "}
              <ContractData
                contract="Ctontine"
                method="indices"
                hideIndicator              />
            </p>
            <p>
              <strong>isequal</strong>:{" "}
              <ContractData
                contract="Ctontine"
                method="isequal"
                methodArgs={["1", "1"]}//{[accounts[0]]}
              />
            </p>
            <h3>AddPlayer</h3>
          </header>
          <div className="App-intro">
            <ContractForm
              contract="Cplayer"
              method="AddPlayer"
              labels={["name", "number", { from: accounts[0] }]}
            />
          </div>

            <div className="App-intro">
            <ContractForm
              contract="Cplayer"
              method="AddPlayer"
              labels={["name", "number", { from: accounts[0] }]}
            />
          </div>

                 <div className="App-intro">
            <ContractForm
              contract="Ctontine"
              method="join"
              labels={[{ from: accounts[0],value:1 ether }]}
            />
          </div>