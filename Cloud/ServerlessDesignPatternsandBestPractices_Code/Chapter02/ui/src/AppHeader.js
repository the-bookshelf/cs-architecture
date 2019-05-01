import React, { Component } from 'react'
import { Grid, Header } from 'semantic-ui-react'

import './css/header.css'


export default class AppHeader extends Component {
  render() {
    return (
      <Grid>
        <Grid.Row className="app-header-wrapper">
          <Grid.Column textAlign="center" width={16} className="app-header">
            <Header as='h2' textAlign='center' inverted color="grey">
              Serverless Coffee Cupping
            </Header>
          </Grid.Column>
        </Grid.Row>
      </Grid>
    )
  }
}
