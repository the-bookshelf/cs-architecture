import json
import graphene

from graphene_sqlalchemy import SQLAlchemyObjectType

from .decorators import decode_json

from ..models import SessionModel
from ..persistence.cupping import Cupping
from ..persistence.session import Session
from ..persistence.queries import (
        get_cuppings,
        get_sessions,
)


def create_session_from_kwargs(kwargs_dict):
    session_model = SessionModel(kwargs_dict)
    session_model.validate()
    return Session.from_model(session_model)


class CuppingObject(SQLAlchemyObjectType):
    class Meta:
        model = Cupping


class SessionObject(SQLAlchemyObjectType):
    class Meta:
        model = Session


class CuppingInput(graphene.InputObjectType):
    name = graphene.String(required=True)
    scores = graphene.types.json.JSONString()
    overall_score = graphene.Float(required=True)
    notes = graphene.String()
    descriptors = graphene.List(graphene.String)
    defects = graphene.List(graphene.String)
    is_sample = graphene.Boolean()


class CreateSessionMutation(graphene.Mutation):

    class Arguments:
        name = graphene.String()
        form_name = graphene.String()
        account_id = graphene.Int()
        user_id = graphene.Int()
        cuppings = graphene.List(CuppingInput)

    ok = graphene.Boolean()
    session = graphene.Field(SessionObject)

    def mutate(self, info, *args, **kwargs):
        session = create_session_from_kwargs(kwargs)
        return CreateSessionMutation(session=session, ok=True)


class Mutation(graphene.ObjectType):
    create_session = CreateSessionMutation.Field()


class Query(graphene.ObjectType):
    sessions = graphene.List(SessionObject, id=graphene.Int(), account_id=graphene.Int())
    cuppings = graphene.List(CuppingObject, session_id=graphene.Int())

    def resolve_cuppings(self, info, **filters):
        return get_cuppings(**filters)

    def resolve_sessions(self, info, **filters):
        return get_sessions(**filters)


# Global schema which will handle queries and mutations
schema = graphene.Schema(
        query=Query,
        mutation=Mutation,
        types=[CuppingObject, SessionObject],
)


@decode_json
def _handle_graphql(payload):
    query = payload['query']
    variables = payload.get('variables', {})
    result = schema.execute(query, variable_values=variables)
    success = True if not result.errors else False
    return success, result


def handle_graphql(http_method, payload):
    success, result = _handle_graphql(payload)
    if not success:
        errors = []
        for e in result.errors:
            try:
                e = json.loads(e.message)
            except:
                e = str(e)
            errors.append(e)
        return {'errors': errors}
    return result.data
