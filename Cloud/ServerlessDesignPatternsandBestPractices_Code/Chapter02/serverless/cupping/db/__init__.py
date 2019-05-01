import contextlib

from contextlib import contextmanager

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.pool import NullPool

from sqlalchemy_utils import database_exists, create_database

from ..constants import (
        DB_ENGINE,
        DB_HOST,
        DB_NAME,
        DB_PASSWORD,
        DB_PORT,
        DB_USERNAME,
        SQLITE,
        POSTGRESQL,
)

__session_factory = None
__session = None
__engine = None
__is_test = False


def setup_db(*, is_test=False, **db_config):
    global __engine
    global __is_test

    if __engine:
        return

    __is_test = is_test

    connection_string = get_connection_string()
    connection_kwargs = db_config.get('connection_kwargs', {})

    # we always want to close connections
    connection_kwargs['poolclass'] = NullPool

    # TODO - debug stuff
    #if __is_test:
    connection_kwargs['echo'] = True

    session_kwargs = db_config.get('session_kwargs', {})

    __engine = create_engine(connection_string, **connection_kwargs)
    print('Connected to: %s' % (__engine.url, ))

    if not database_exists(__engine.url): # pragma: no cover
        print('Creating database: %s' % (__engine.url, ))
        create_database(__engine.url)

    create_tables()
    get_session(**session_kwargs)


def get_connection_string(**kwargs):
    """Return a connection string for sqlalchemy::

        dialect+driver://username:password@host:port/database

    """
    global DB_NAME

    if DB_ENGINE not in (SQLITE, POSTGRESQL):
        raise ValueError(
                'Invalid database engine specified: %s. Only sqlite' \
                ' and postgresql are supported' % (DB_ENGINE, ))

    if DB_ENGINE == SQLITE:
        # missing filename creates an in-memory db
        return 'sqlite://%s' % kwargs.get('filename', '')

    if __is_test and not DB_NAME.startswith('test_'):
        DB_NAME = 'test_%s' % (DB_NAME, )

    return 'postgresql://%s:%s@%s:%s/%s' % (
            DB_USERNAME,
            DB_PASSWORD,
            DB_HOST,
            DB_PORT,
            DB_NAME,
    )


def close_db(): # pragma: no cover
    if not __session:
        return
    try:
        __session.commit()
    except:
        __session.rollback()
    finally:
        __session.close()


def commit_session(_raise=False): # pragma: no cover
    try:
        __session.commit()
    except Exception as e:
        __session.rollback()
        if _raise:
            raise


def _get_metadata():
    from .mixins import Base
    return Base.metadata


def create_tables():
    assert __engine
    meta = _get_metadata()
    meta.create_all(__engine)

def _drop_tables(*, force=False):
    if not __is_test and not force:
        return
    assert __engine
    meta = _get_metadata()
    meta.drop_all(__engine)


def _clear_tables(*, force=False):
    if not __is_test and not force:
        return

    assert __engine

    meta = _get_metadata()
    with contextlib.closing(__engine.connect()) as con:
        trans = con.begin()
        for table in reversed(meta.sorted_tables):
            try:
                con.execute(table.delete())
            except:
                pass
        trans.commit()


def get_session(**kwargs):
    setup_db()

    assert __engine
    global __session
    global __session_factory

    if __session is not None:
        return __session

    if __session_factory is None: # pragma: no cover
        __session_factory = sessionmaker(bind=__engine, **kwargs)

    __session = __session_factory()
    return __session


def session_getter(func):
    """Decorator to get a session and inject it as the first argument in a function"""
    def wrapper(*args, **kwargs):
        session = get_session()
        return func(session, *args, **kwargs)
    return wrapper


@contextmanager
def dbtransaction(): # pragma: no cover
    s = get_session()
    yield s
    try:
        s.commit()
    except:
        s.rollback()
        raise
