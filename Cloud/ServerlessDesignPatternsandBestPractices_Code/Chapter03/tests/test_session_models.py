import pytest

from decimal import Decimal
from schematics.exceptions import DataError

from cupping.handlers.helpers  import prettify_schematics_errors


def test_session_create_name_required(empty_session_model):
    empty_session_model.form_name = 'my form'

    with pytest.raises(DataError) as e:
        empty_session_model.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
            'name': 'This field is required.',
    }


def test_session_create_form_name_required(empty_session_model):
    empty_session_model.name = 'Test cupping'

    with pytest.raises(DataError) as e:
        empty_session_model.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
            'formName': 'This field is required.',
    }


def test_session_create_account_id_requires_int(valid_session_model):
    valid_session_model.account_id = 'a123'

    with pytest.raises(DataError) as e:
        valid_session_model.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
            'accountId': "Value 'a123' is not int.",
    }


def test_session_create_user_id_requires_int(valid_session_model):
    valid_session_model.user_id = 'a123'

    with pytest.raises(DataError) as e:
        valid_session_model.validate()

    errors = prettify_schematics_errors(e)
    assert errors == {
            'userId': "Value 'a123' is not int.",
    }


def test_session_valid_no_cuppings(valid_session_model):
    assert not valid_session_model.validate()
    assert not valid_session_model.cuppings


def test_session_valid_cuppings(valid_session_model, cupping_models):
    valid_session_model.cuppings = cupping_models
    assert not valid_session_model.validate()

    for c in valid_session_model.cuppings:
        for value in c.scores.values():
            assert isinstance(value, Decimal)
