import pytest

from decimal import Decimal
from schematics.exceptions import DataError

from cupping.handlers.helpers  import prettify_schematics_errors
from cupping.models import CuppingModel


def test_cupping_invalid_cupping_score():
    with pytest.raises(DataError) as e:
        CuppingModel({
            'name': 'Tester',
            'session_id': 10,
            'scores': {
                'Aroma': 'abc',
                'Flavor': '5',
            },
            'overall_score': 88.5,
        })

    errors = prettify_schematics_errors(e)
    assert errors == {
           'scores': {
               'Aroma': "Number 'abc' failed to convert to a decimal."
            }
    }


def test_cupping_overall_score_min_value():
    c = CuppingModel({
        'name': 'Tester',
        'session_id': 10,
        'scores': {'aroma': 5},
        'overall_score': '-0.1',
    })
    with pytest.raises(DataError) as e:
        c.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
           'overallScore': 'Value should be greater than or equal to 0.'
    }


def test_cupping_overall_score_max_value():
    c = CuppingModel({
        'name': 'Tester',
        'session_id': 10,
        'scores': {'aroma': 5},
        'overall_score': '100.1',
    })
    with pytest.raises(DataError) as e:
        c.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
           'overallScore': 'Value should be less than or equal to 100.'
    }


def test_cupping_scores_required():
    c = CuppingModel({
        'name': 'Tester',
        'session_id': 10,
        'overall_score': '100',
    })
    with pytest.raises(DataError) as e:
        c.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
           'scores': 'This field is required.'
    }


def test_cupping_scores_empty():
    c = CuppingModel({
        'name': 'Tester',
        'session_id': 10,
        'scores': {},
        'overall_score': '100',
    })
    with pytest.raises(DataError) as e:
        c.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
           'scores': 'This field is required.'
    }


def test_cupping_invalid_overall_score():
    with pytest.raises(DataError) as e:
        CuppingModel({
            'name': 'Tester',
            'session_id': 10,
            'overall_score': 'abc',
        })

    errors = prettify_schematics_errors(e)
    assert errors == {
           'overallScore': "Number 'abc' failed to convert to a decimal."
    }


def test_cupping_name_required():
    c = CuppingModel({
        'session_id': 10,
        'overall_score': '100',
        'scores': {
            'Aroma': 12,
        },

    })
    with pytest.raises(DataError) as e:
        c.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
           'name': 'This field is required.'
    }


def test_cupping_name_min_length():
    c = CuppingModel({
        'name': '',
        'session_id': 10,
        'overall_score': '100',
        'scores': {
            'Aroma': 12,
        },

    })
    with pytest.raises(DataError) as e:
        c.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
           'name': 'String value is too short.'
    }


def test_cupping_name_max_length():
    c = CuppingModel({
        'name': 'a' * 129,
        'session_id': 10,
        'overall_score': '100',
        'scores': {
            'Aroma': 12,
        },

    })
    with pytest.raises(DataError) as e:
        c.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
           'name': 'String value is too long.'
    }
