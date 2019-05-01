import pytest

import handler

from cupping.exceptions import Http404

from helpers import (
        assert_200,
        assert_404,
        get_body_from_response,
)


@pytest.fixture()
def mock_handler(mocker):
    mock_handler = mocker.patch('handler.handle_session')
    mock_handler.return_value = {'test_passed': True}
    return mock_handler


def test_session_hanlder_get(mock_handler):
    event = {'httpMethod': 'GET'}
    response = handler.session(event, None)
    assert_200(response)
    body = get_body_from_response(response)

    assert body == {'test_passed': True}
    mock_handler.assert_called_once_with('GET', event)


def test_session_hanlder_post(mock_handler):
    event = {'httpMethod': 'POST'}
    response = handler.session(event, None)
    assert_200(response)
    body = get_body_from_response(response)
    assert body == {'test_passed': True}
    mock_handler.assert_called_once_with('POST', event)


def test_session_detail_hanlder_404(mocker):
    mock_handler = mocker.patch('handler.handle_session_detail')
    mock_handler.side_effect = Http404('Test 404 error')

    event = {'httpMethod': 'POST'}
    response = handler.session_detail(event, None)
    assert_404(response)
    body = get_body_from_response(response)
    assert body == {'errors': ['Test 404 error']}
    mock_handler.assert_called_once_with('POST', event)
