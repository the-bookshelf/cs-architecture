from ..db import dbtransaction, session_getter

from .cupping import Cupping
from .session import Session


@session_getter
def get_cuppings(session, **kwargs):
    return session.query(Cupping).filter_by(**kwargs)


@session_getter
def get_session_by_id(session, _id, **kwargs):
    kwargs['id'] = _id
    return session.query(Session).filter_by(**kwargs).join(Session.cuppings).first()


@session_getter
def get_sessions(session, **kwargs):
    return session.query(Session).filter_by(**kwargs)
