import re

from sqlalchemy.ext.declarative import (
        declarative_base,
        declared_attr,
)
from sqlalchemy import (
        Column,
        Integer,
        MetaData,
        Index,
)

from . import (
        commit_session,
        get_session,
)

index_convention = {
    "ix": "ix_%(table_name)s_%(column_0_label)s",
    "uq": "uq_%(table_name)s_%(column_0_name)s",
    "fk": "fk_%(table_name)s_%(column_0_name)s_%(referred_table_name)s",
    "pk": "pk_%(table_name)s"
}
metadata = MetaData(naming_convention=index_convention)
Base = declarative_base(metadata=metadata)


def class_name_to_underscores(name):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()


class CuppingServiceModelMixin:

    @declared_attr
    def __tablename__(cls):
        return class_name_to_underscores(cls.__name__)

    def save(self, *, commit=False):
        session = get_session()
        session.add(self)
        if commit:
            commit_session()

    @classmethod
    def flush(self):
        get_session().flush()

    @classmethod
    def _get_index(cls, name, *columns, **kwargs):
        cols = [getattr(cls, c) for c in columns]
        return Index(name, *cols, **kwargs)

    @classmethod
    def generate_index(cls, *columns, **kwargs):
        d = {
                'table_name': cls.__tablename__,
                'column_0_label': '_'.join(columns),
        }
        name = index_convention['ix'] % d
        return cls._get_index(name, *columns, **kwargs)

    @classmethod
    def generate_unique_index(cls, *columns, **kwargs):
        d = {
                'table_name': cls.__tablename__,
                'column_0_name': '_'.join(columns),
        }
        name = index_convention['uq'] % d
        if not kwargs.get('unique'):
            kwargs['unique'] = True
        return cls._get_index(name, *columns, **kwargs)
