import React, { Component } from 'react'
import { Icon, Label, Menu, Table } from 'semantic-ui-react'


let sessions =  [
        {
            "id": 1,
            "name": "Cupping session from now",
            "formName": "custom-form",
            "accountId": null,
            "userId": null,
            "cuppings": [
                {
                    "session_id": 1,
                    "name": "Guat Huehuetenango",
                    "scores": {
                        "Body": 8,
                        "Aroma": 8.6,
                        "Flavor": 10
                    },
                    "overallScore": 88.5,
                    "descriptors": [
                        "woody",
                        "berries"
                    ],
                    "defects": null,
                    "notes": "This was pretty good",
                    "isSample": false
                },
                {
                    "session_id": 1,
                    "name": "Ethiopia Yiracheffe",
                    "scores": {
                        "Body": 8,
                        "Aroma": 8.6,
                        "Flavor": 10
                    },
                    "overallScore": 90,
                    "descriptors": [
                        "blueberry"
                    ],
                    "defects": null,
                    "notes": "",
                    "isSample": false
                }
            ]
        },
        {
            "id": 2,
            "name": "Morning evaluation",
            "formName": "Modified SCAA",
            "accountId": null,
            "userId": null,
            "cuppings": [
                {
                    "session_id": 2,
                    "name": "Fazenda Santa Ines Yellow Bourbon",
                    "scores": {
                        "Body": 6,
                        "Aroma": 7,
                        "Flavor": 8
                    },
                    "overallScore": 81.5,
                    "descriptors": [
                        "sweet",
                        "citrus"
                    ],
                    "defects": null,
                    "notes": "Sweet and clean with citric acidity and a creamy mouthfeel; orange and coffee cherry flavor",
                    "isSample": false
                },
                {
                    "session_id": 2,
                    "name": "Café Vida",
                    "scores": {
                        "Body": 8,
                        "Aroma": 8,
                        "Flavor": 9
                    },
                    "overallScore": 85,
                    "descriptors": [
                        "tart",
                        "toffee",
                        "lemon"
                    ],
                    "defects": null,
                    "notes": "Mild, sweet and clean with tart acidity; toffee, lemon and a peanut aftertaste",
                    "isSample": false
                },
                {
                    "session_id": 2,
                    "name": "Finca Montana Grande - Pacas ",
                    "scores": {
                        "Body": 8,
                        "Aroma": 8,
                        "Flavor": 9
                    },
                    "overallScore": 82,
                    "descriptors": [
                        "cocoa",
                        "toffee",
                        "lemon",
                        "peanut"
                    ],
                    "defects": null,
                    "notes": "Sweet with citric acidity and a smooth mouthfeel; toffee, cocoa, sweet lemon and a peanut aftertaste.",
                    "isSample": false
                }
            ]
        }
]


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


export default class CuppingTable extends Component {

  handleClick = (session) => {
    this.props.handleLoadSession(session)
  }

  render() {
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
            { sessions.map(session =>
              <CuppingTableRow
                  session={session}
                  handleClick={this.handleClick}
            />) }
        </Table.Body>
      </Table>
    )
  }
}
