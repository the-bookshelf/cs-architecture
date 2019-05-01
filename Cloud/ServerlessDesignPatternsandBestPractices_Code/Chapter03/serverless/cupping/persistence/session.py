from ..db import dbtransaction
from ..db.mixins import Base
from .base import CuppingServiceBaseMixin
from .cupping import Cupping

from sqlalchemy.orm import relationship, validates

from sqlalchemy import (
                Boolean,
                Column,
                ForeignKey,
                Integer,
                Numeric,
                String,
)


class Session(CuppingServiceBaseMixin, Base):
    """A group of cuppings."""
    __tablename__ = 'sessions'

    name = Column(String(length=127))

    form_name = Column(String(length=127))

    account_id = Column(Integer, nullable=True)

    user_id = Column(Integer, nullable=True)

    cuppings = relationship('Cupping', order_by='Cupping.id', back_populates='session')

    def _validate_integer(self, key, value):
        if value is None:
            return None
        if not isinstance(value, int):
            raise ValueError('%s field must be a positive integer value' % (key, ))
        if value <= 0:
            raise ValueError('%s field must be a positive integer value' % (key, ))
        return value

    def _validate_string(self, key, value):
        if not isinstance(value, str) or not value.strip():
            raise ValueError('%s field must be a non-empty string' % (key, ))
        return value

    @validates('name')
    def validate_name(self, key, value):
        return self._validate_string(key, value)

    @validates('form_name')
    def validate_form_name(self, key, value):
        return self._validate_string(key, value)

    @validates('account_id')
    def validate_account_id(self, key, value):
        return self._validate_integer(key, value)

    @validates('user_id')
    def validate_user_id(self, key, value):
        return self._validate_integer(key, value)

    @classmethod
    def from_model(cls, model):
        model.validate()
        with dbtransaction():
            session = cls(
                    name=model.name,
                    form_name=model.form_name,
                    account_id=model.account_id,
                    user_id=model.user_id,
            )
            session.save()
            session.flush()


            cuppings = [
                    Cupping.from_model(c, session_id=session.id) \
                    for c in model.get('cuppings') or ()
            ]

            return session
