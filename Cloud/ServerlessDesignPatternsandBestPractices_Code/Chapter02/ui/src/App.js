import React, { Component } from 'react';
import './App.css';

import AppHeader from './AppHeader'
import MainContent from './MainContent'

import { Sidebar, Menu, Icon } from 'semantic-ui-react'

class App extends Component {

  constructor(props) {
    super(props);

    this.state = {
      showCuppings : true
      , showNewCupping : false
    };
  }

  showCuppings = (e, data) => {
    this.setState({
      showCuppings: true
      , showNewCupping: false
    })
  }

  showNewCupping = (e, data) => {
    this.setState({
      showCuppings: false
      , showNewCupping: true
    })
  }

  render() {
    return (
      <div className="main-container">
        <AppHeader />
        <Sidebar.Pushable >
          <Sidebar as={Menu} width='thin' visible={true} icon='labeled' vertical inverted>
            <Menu.Item name='list' onClick={this.showCuppings}>
              <Icon name='list' />
              List Cuppings
            </Menu.Item>
            <Menu.Item name='plus' onClick={this.showNewCupping}>
              <Icon name='plus' />
              New Cupping
            </Menu.Item>
          </Sidebar>
          <Sidebar.Pusher>
            <MainContent
                showCuppings={this.state.showCuppings}
                showNewCupping={this.state.showNewCupping}
            />
          </Sidebar.Pusher>
        </Sidebar.Pushable>
      </div>
    );
  }
}

export default App;
