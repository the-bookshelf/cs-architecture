import pytest

from decimal import Decimal

from cupping.db import (
        dbtransaction,
        commit_session,
        get_session,
)
from cupping.persistence import Session

from factories import SessionFactory


@pytest.fixture()
def session():
    return SessionFactory()


def test_session_create(session):
    assert session.id


def test_session_create_default_values(session):
    assert session.form_name
    assert session.account_id == None
    assert session.user_id == None
    assert session.cuppings == []


def test_session_create_all_args(valid_session_model, cupping_models):
    valid_session_model.cuppings = cupping_models
    valid_session_model.account_id = 123
    valid_session_model.user_id = 345
    s = Session.from_model(valid_session_model)

    assert s.id > 0
    assert s.account_id == 123
    assert s.user_id == 345

    assert len(s.cuppings) == 3
    for c in s.cuppings:
        assert c.session.id == s.id


_empty_strings = (
    '',
    '   ',
    '\n',
    '\t',
    None,
    False,
    0,
    [],
    (),
    {},
)

@pytest.mark.parametrize('empty_string', _empty_strings)
def test_session_create_name_requires_string(empty_string):
    with pytest.raises(ValueError) as e:
        SessionFactory(name=empty_string)

    assert 'name field must be a non-empty string' in str(e)


@pytest.mark.parametrize('empty_string', _empty_strings)
def test_session_create_form_name_requires_string(empty_string):
    with pytest.raises(ValueError) as e:
        SessionFactory(form_name=empty_string)

    assert 'form_name field must be a non-empty string' in str(e)


_invalid_ints = (
        'abc',
        '0abc',
        [],
        [123],
        0,
        -1,
)

@pytest.mark.parametrize('invalid_int', _invalid_ints)
def test_session_create_account_id_requires_int(invalid_int):
    with pytest.raises(ValueError) as e:
        SessionFactory(account_id=invalid_int)

    assert 'account_id field must be a positive integer value' in str(e)


@pytest.mark.parametrize('invalid_int', _invalid_ints)
def test_session_create_user_id_requires_int(invalid_int):
    with pytest.raises(ValueError) as e:
        SessionFactory(user_id=invalid_int)

    assert 'user_id field must be a positive integer value' in str(e)

def test_session_create_account_id_none_ok():
    s = SessionFactory(account_id=None)
    assert s.account_id == None

def test_session_create_user_id_none_ok():
    s = SessionFactory(user_id=None)
    assert s.user_id == None
