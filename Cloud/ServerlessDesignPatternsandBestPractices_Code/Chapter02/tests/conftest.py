import os
import sys
import pytest
import random

from pathlib import Path

CWD = Path(__file__).resolve().parent
code_dir = CWD / '../serverless'
lib_dir = CWD / '../serverless/lib'

sys.path.append(str(code_dir))
sys.path.append(str(lib_dir))

ENV = os.environ['ENV']
os.environ.update({
    'CUPPING_DB_PASSWORD': '',
    'CUPPING_DB_USERNAME': 'postgres',
    'CUPPING_DB_HOST': 'db',
})

if os.environ.get('CIRCLECI'):
    os.environ['CUPPING_DB_HOST'] = 'localhost'
    os.environ['CUPPING_DB_NAME'] = 'cupping_test'

from cupping.models import (
    CuppingModel,
    SessionModel,
)
from cupping.persistence import (
    Cupping,
    Session,
)
from cupping.db import (
    _clear_tables,
    _drop_tables,
    close_db,
    get_session,
    commit_session,
    setup_db,
)


def pytest_configure(config):
    """Called at the start of the entire test run"""
    setup_db(is_test=True)


def pytest_unconfigure(config):
    """Called at the end of a test run"""
    close_db()
    _drop_tables()


def pytest_runtest_setup(item):
    """Called at teh start of single test"""
    _clear_tables()


def pytest_runtest_teardown(item, nextitem):
    """Called at the end of each test"""
    get_session().rollback()


@pytest.fixture()
def empty_session_model():
    return SessionModel()


@pytest.fixture()
def valid_session_model():
    return SessionModel({
        'name': 'Test Session',
        'form_name': 'SCAA',
    })


@pytest.fixture()
def cupping_model():
    return CuppingModel({
        'name': random.choice(('Guat', 'Kochere', 'Costa Rica', 'Brazilian')),
        'session_id': random.randint(1, 10000),
        'scores': {
            'Aroma': random.randint(1, 10),
            'Flavor': random.randint(1, 10),
        },
        'overall_score': round(random.randint(50, 99) + random.random(), 1),
    })


@pytest.fixture()
def cupping_models():
    return [cupping_model() for i in range(3)]


@pytest.fixture()
def cuppings_dicts():
    return [
        {
            'name': 'Huehue',
            'scores': {
                'Aroma': 8.6,
                'Flavor': 5.5
            },
            'overallScore': 75,
            'defects': ['stank', 'pu'],
            'descriptors': ['honey', 'berry', 'mungy'],
            'notes': 'Pretty good with elements of stank',
            'isSample': False,
        },
        {
            'name': 'Kochere',
            'scores': {
                'Aroma': 5.6,
                'Flavor': 8.4
            },
            'overallScore': 85,
            'defects': [],
            'descriptors': [],
            'notes': '',
            'isSample': False,
        },
    ]


@pytest.fixture()
def session_dict(cuppings_dicts):
    return {
        'name': 'Test cupping',
        'formName': 'SCAA',
        'accountId': 123,
        'userId': 456,
        'cuppings': cuppings_dicts,
    }
