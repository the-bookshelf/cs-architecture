import json
import pytest

from cupping.handlers.graphql import schema, handle_graphql
from factories import (
        CuppingFactory,
        SessionFactory,
)

from pprint import pprint as pp

# Getting sessions

def _build_gql_payload(query, **variables):
    return {'body': json.dumps({'query': query, 'variables': variables})}


def test_get_all_sessions():
    SessionFactory.create_batch(3)
    query = '''
    query allSessions {
          sessions {
            id
            name
            formName
            created
          }
    }
    '''
    result = handle_graphql('GET', _build_gql_payload(query))
    sessions = result['sessions']
    assert len(sessions) == 3


def test_get_single_session():
    sessions = SessionFactory.create_batch(3)
    session = sessions[-1]
    query = """
    query allSessions($id: Int!) {
          sessions(id: $id) {
            id
            name
            formName
            created
          }
    }
    """
    result = handle_graphql('GET', _build_gql_payload(query, id=session.id))
    sessions = result['sessions']
    assert len(sessions) == 1
    assert sessions[0]['id'] == str(session.id)


def test_get_session_by_account_id():
    sessions = SessionFactory.create_batch(3, account_id=5)
    session = SessionFactory(account_id=888)
    query = """
    query allSessions($accountId: Int!) {
          sessions(accountId: $accountId) {
            id
            accountId
            cuppings {
                id
                name
                overallScore
            }
          }
    }
    """
    result = handle_graphql('GET', _build_gql_payload(query, accountId=888))
    sessions = result['sessions']
    assert len(sessions) == 1
    s = sessions[0]
    assert s['id'] == str(session.id)
    assert s['accountId'] == 888


def test_query_error():
    """Raise an error by not passing in a required param"""
    query = """
    query allSessions($accountId: Int!) {
          sessions(accountId: $accountId) {
            id
            accountId
            cuppings {
                id
                name
                overallScore
            }
          }
    }
    """
    result = handle_graphql('GET', _build_gql_payload(query))
    expected_error = ['Variable "$accountId" of required type "Int!" was not provided.']
    assert result.get('errors') == expected_error


def test_get_invalid_session():
    query = """
    query allSessions($accountId: Int!) {
          sessions(accountId: $accountId) {
            id
            accountId
          }
    }
    """
    result = handle_graphql('GET', _build_gql_payload(query, accountId=888))
    sessions = result['sessions']
    assert len(sessions) == 0


def test_get_all_sessions_with_cuppings():
    num_sessions = 3
    sessions = SessionFactory.create_batch(num_sessions)

    expected_cupping_lens = {}
    for i, session in enumerate(sessions):
        # create a variable number of cuppings per session, 3, 2, 1
        CuppingFactory.create_batch(num_sessions - i, session_id=session.id)
        expected_cupping_lens[session.id] = num_sessions - i

    query = """
    query allSessions($withCuppings: Boolean = true) {
          sessions {
            id
            name
            formName
            cuppings @include(if: $withCuppings) {
                id
                name
                scores
            }
          }
    }
    """
    result = handle_graphql('GET', _build_gql_payload(query, withCuppings=True))
    sessions = result['sessions']
    assert len(sessions) == 3

    for s in sessions:
        assert len(s['cuppings']) == expected_cupping_lens[int(s['id'])]


# Getting cuppings

def test_get_all_cuppings():
    session = SessionFactory()
    CuppingFactory.create_batch(3, session_id=session.id)
    query = """
    query allCuppings {
        cuppings {
            id
            name
            scores
            sessionId
        }
    }
    """
    result = handle_graphql('GET', _build_gql_payload(query))
    cuppings = result['cuppings']
    assert len(cuppings) == 3
    assert [c['sessionId'] for c in cuppings] == [session.id] * 3
    assert len(set((c['id'] for c in cuppings))) == 3


def test_query_cuppings_by_session_id():
    # create one session and 3 cuppings which should not be returned
    s = SessionFactory(account_id=12345)
    CuppingFactory.create_batch(3, session_id=s.id)

    # create a single session with a single cupping. This is the only thing which should be
    # returned from our query.
    session = SessionFactory(account_id=12345)
    cupping = CuppingFactory(session_id=session.id)

    query = """
    query Cuppings($session_id: Int!) {
        cuppings(sessionId: $session_id) {
            id
            name
            scores
            sessionId
        }
    }
    """
    result = handle_graphql('GET', _build_gql_payload(query, session_id=session.id))
    cuppings = result['cuppings']
    assert len(cuppings) == 1
    assert cuppings[0]['id'] == str(cupping.id)
    assert cuppings[0]['sessionId'] == session.id


@pytest.fixture()
def graphql_cupping_mutation(session_dict):
    # Transform cuppings array from dict to GraphQL mutation
    cuppings_mutation = []
    for cupping in session_dict['cuppings']:
        cupping['scores'] = json.dumps(cupping['scores']).replace('"', '\\"')

        _quoted_descriptors = ['"%s"' % d for d in cupping['descriptors']]
        cupping['descriptors'] = ' '.join(_quoted_descriptors)

        _quoted_defects = ['"%s"' % d for d in cupping['defects']]
        cupping['defects'] = ' '.join(_quoted_defects)

        cupping['isSample'] = 'true' if cupping['isSample'] else 'false'

        cuppings_mutation.append("""
            {
                name: "%(name)s"
                overallScore: %(overallScore)s
                scores: "%(scores)s"
                notes: "%(notes)s"
                isSample: %(isSample)s
                defects: [ %(defects)s ]
                descriptors : [ %(descriptors)s ]
            }
            """ % cupping
        )

    return ','.join(cuppings_mutation)


def test_create_session(graphql_cupping_mutation):
    query = """
    mutation SessionCreator {
        createSession (
            name: "GraphQL Test"
            formName: "GraphQL Form"
            cuppings: [
                %s
            ]
        ) {
            ok
            session {
                id
                name
                formName
                cuppings {
                    sessionId
                    name
                    overallScore
                    scores
                    defects
                }
            }
        }
    }
    """ % (graphql_cupping_mutation, )

    result = handle_graphql('GET', _build_gql_payload(query))

    data = result['createSession']
    assert data['ok'] == True
    session = data['session']
    assert session['name'] == "GraphQL Test"
    assert session['formName'] == "GraphQL Form"
    cuppings = session['cuppings']
    assert len(cuppings) == 2
    assert cuppings[0]['sessionId'] == int(session['id'])
    assert cuppings[1]['sessionId'] == int(session['id'])


def test_create_session_error(graphql_cupping_mutation):
    query = """
    mutation SessionCreator {
        createSession (
            cuppings: [
                %s
            ]
        ) {
            ok
        }
    }
    """ % (graphql_cupping_mutation, )

    result = handle_graphql('GET', _build_gql_payload(query))

    errors = result.get('errors')

    expected_errors = [
        {
            'formName': ['This field is required.'],
            'name': ['This field is required.']
        }
    ]
    assert errors == expected_errors


def test_query():
    query = """
          query IntrospectionQuery {
            __schema {
              queryType { name }
              mutationType { name }
              subscriptionType { name }
              types {
                ...FullType
              }
              directives {
                name
                description
                locations
                args {
                  ...InputValue
                }
              }
            }
          }

          fragment FullType on __Type {
            kind
            name
            description
            fields(includeDeprecated: true) {
              name
              description
              args {
                ...InputValue
              }
              type {
                ...TypeRef
              }
              isDeprecated
              deprecationReason
            }
            inputFields {
              ...InputValue
            }
            interfaces {
              ...TypeRef
            }
            enumValues(includeDeprecated: true) {
              name
              description
              isDeprecated
              deprecationReason
            }
            possibleTypes {
              ...TypeRef
            }
          }

          fragment InputValue on __InputValue {
            name
            description
            type { ...TypeRef }
            defaultValue
          }

          fragment TypeRef on __Type {
            kind
            name
            ofType {
              kind
              name
              ofType {
                kind
                name
                ofType {
                  kind
                  name
                  ofType {
                    kind
                    name
                    ofType {
                      kind
                      name
                      ofType {
                        kind
                        name
                        ofType {
                          kind
                          name
                        }
                      }
                    }
                  }
                }
              }
            }
          }
    """
    result = schema.execute(query)
    assert not result.errors
    pp(result.data)
