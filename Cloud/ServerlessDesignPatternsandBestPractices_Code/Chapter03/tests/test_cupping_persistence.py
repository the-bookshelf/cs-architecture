import pytest

from decimal import Decimal

from cupping.db import (
        dbtransaction,
        commit_session,
        get_session,
)
from cupping.models import CuppingModel
from cupping.persistence import Cupping

from factories import SessionFactory


@pytest.fixture()
def cupping_model_with_session(cupping_model):
    session = SessionFactory()
    cupping_model.session_id = session.id
    return cupping_model


def test_cupping_repr(cupping_model_with_session):
    c = Cupping()
    assert str(c)


def test_cupping_create(cupping_model_with_session):
    cupping_model_with_session.descriptors = ['berry', 'fruit']
    cupping_model_with_session.defects = ['sour', '123']
    cupping_model_with_session.notes = 'pretty good'
    cupping_model_with_session.is_sample = True
    cupping_model_with_session.name = 'Test Coffee'

    cupping = Cupping.from_model(cupping_model_with_session)
    commit_session(_raise=True)

    assert cupping.id
    assert cupping.session_id >= 1
    assert cupping.scores == cupping_model_with_session.scores
    assert cupping.overall_score == cupping_model_with_session.overall_score
    assert cupping.descriptors == ['berry', 'fruit']
    assert cupping.defects == ['sour', '123']
    assert cupping.notes == 'pretty good'
    assert cupping.is_sample == True
    assert cupping.name == 'Test Coffee'


def test_cupping_create_default_values(cupping_model_with_session):
    cupping = Cupping.from_model(cupping_model_with_session)
    commit_session(_raise=True)

    assert cupping.name
    assert cupping.defects == None
    assert cupping.descriptors == None
    assert cupping.is_sample == False


def test_cupping_create_scores_requires_dict(cupping_model_with_session):
    cupping_model_with_session.scores = [{'Aroma': 8, 'Flavor': 6.5}]
    with pytest.raises(ValueError) as e:
        Cupping.from_model(cupping_model_with_session)

    assert 'Scores must be a mapping of name to numeric value' in str(e)


def test_cupping_create_scores_requires_numeric_values(cupping_model_with_session):
    cupping_model_with_session.scores = {'Aroma': 8, 'Flavor': 'abc'}
    with pytest.raises(ValueError) as e:
        Cupping.from_model(cupping_model_with_session)

    assert 'Scores must be a mapping of name to numeric value' in str(e)


@pytest.mark.parametrize("falsey_value", ([], (), {}, None, 0, set()))
def test_cupping_create_scores_falsey(falsey_value, cupping_model_with_session):
    cupping_model_with_session.scores = falsey_value

    cupping = Cupping.from_model(cupping_model_with_session)
    commit_session(_raise=True)
    assert cupping.scores == None


def test_cupping_create_descriptors_requires_list(cupping_model_with_session):
    cupping_model_with_session.descriptors = 'yummy'
    with pytest.raises(ValueError) as e:
        Cupping.from_model(cupping_model_with_session)

    assert 'descriptors must be a list of strings' in str(e)


_non_strings = (
    ('abc', 123),
    (123.4, ),
    ([], 'abc'),
    ('one', 'two', ['three'],),
    ('one', None),
)

@pytest.mark.parametrize('non_strings', _non_strings)
def test_cupping_create_descriptors_requires_list_of_strings(non_strings, cupping_model_with_session):
    cupping_model_with_session.descriptors = non_strings
    with pytest.raises(ValueError) as e:
        Cupping.from_model(cupping_model_with_session)

    assert 'descriptors must be a list of strings' in str(e)


@pytest.mark.parametrize('non_strings', _non_strings)
def test_cupping_create_defects_requires_list_of_strings(non_strings, cupping_model_with_session):
    cupping_model_with_session.defects = non_strings
    with pytest.raises(ValueError) as e:
        Cupping.from_model(cupping_model_with_session)

    assert 'defects must be a list of strings' in str(e)
