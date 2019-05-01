import React, { Component } from 'react'
import { Breadcrumb, Table } from 'semantic-ui-react'
import { BASE_URL } from './constants'


class CuppingTableRow extends Component {

  handleClick = (e) => {
    this.props.handleClick(this.props.session)
  }

  render() {
    return (
      <Table.Row>
        <Table.Cell>{this.props.session.id}</Table.Cell>
        <Table.Cell>
            <a onClick={this.handleClick}>{this.props.session.name}</a>
        </Table.Cell>
        <Table.Cell>{this.props.session.formName}</Table.Cell>
        <Table.Cell>{this.props.session.cuppings.length}</Table.Cell>
      </Table.Row>
    )
  }
}


const getSortedScores = (cupping) => {
    return Object.keys(cupping).sort()
}


class CuppingScoreRow extends Component {
  render() {
    const cupping = this.props.cupping
    const scores = getSortedScores(cupping.scores)
    return (
        <Table.Row>
          <Table.Cell>{cupping.name}</Table.Cell>
          { scores.map(name => <Table.Cell key={name}>{cupping.scores[name]}</Table.Cell>) }
          <Table.Cell>{cupping.overallScore}</Table.Cell>
        </Table.Row>
    )
  }
}

export default class CuppingTable extends Component {

  constructor(props) {
    super(props)
    this.state = {
      showSessionListing: true
      , showSessionDetail: false
      , session: null
      , sessions: null
      , sections: [
            { key: 'Cuppings', content: 'Cupping Sessions', link: false }
      ]
    }
  }

  componentDidMount() {
    fetch(`${BASE_URL}/session`)
      .then(response => {
        if (!response.ok) {
          throw Error("Network request failed")
        }
        return response
      })
      .then(d => d.json())
      .then(d => {
        this.setState({
          sessions: d.sessions
        })
      }, () => {
        this.setState({
          requestFailed: true
        })
      })
  }

  showSessionListing = (e) => {
    this.setState({
      showSessionListing: true
      , showSessionDetail: false
      , sections: [
            { key: 'Cuppings', content: 'Cupping Sessions', link: false }
      ]
    })
  }

  handleRowClicked = (session) => {
    let sections = [
        { key: 'Cuppings', content: 'Cupping Sessions', link: true, onClick: this.showSessionListing }
      , { key: 'SessionId', content: `Session id ${session.id}`, link: false }
    ]

    this.setState({
      showSessionListing: false
      , showSessionDetail: true
      , sections
      , session
    })
  }

  sessionDetail() {
    const session = this.state.session
    const scores = getSortedScores(session.cuppings[0].scores)
    return (
      <Table celled basic>
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell>Coffee</Table.HeaderCell>
            { scores.map(name =>
                <Table.HeaderCell key={name}>{name}</Table.HeaderCell>)
            }
            <Table.HeaderCell>Overall Score</Table.HeaderCell>
          </Table.Row>
        </Table.Header>

        <Table.Body>
          { session.cuppings.map(cupping =>
            <CuppingScoreRow
                cupping={cupping}
                key={cupping.id}
            />)
          }
        </Table.Body>
      </Table>
    )
  }

  sessionListing() {
    return (
      <Table celled basic>
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell>Session Id</Table.HeaderCell>
            <Table.HeaderCell>Session Name</Table.HeaderCell>
            <Table.HeaderCell>Cupping Form</Table.HeaderCell>
            <Table.HeaderCell>Number of coffees</Table.HeaderCell>
          </Table.Row>
        </Table.Header>

        <Table.Body>
            { this.state.sessions.map(session =>
              <CuppingTableRow
                  key={session.id.toString()}
                  session={session}
                  handleClick={this.handleRowClicked}
            />) }
        </Table.Body>
      </Table>
    )
  }

  render() {
    return (
      <div>
        <Breadcrumb icon='right angle' sections={this.state.sections} />
        { this.state.showSessionListing && this.state.sessions ? this.sessionListing() : null }
        { this.state.showSessionDetail ? this.sessionDetail() : null }
      </div>
    )
  }
}
