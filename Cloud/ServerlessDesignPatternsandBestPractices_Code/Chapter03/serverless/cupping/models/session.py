from schematics.models import Model
from schematics.types import (
        IntType,
        ListType,
        ModelType,
        StringType,
)

from .cupping import CuppingModel


class SessionModel(Model):
    id = IntType()
    name = StringType(max_length=127, required=True)
    form_name = StringType(max_length=127, required=True, serialized_name='formName')
    account_id = IntType(serialized_name='accountId')
    user_id = IntType(serialized_name='userId')

    cuppings = ListType(ModelType(CuppingModel))

    @staticmethod
    def from_row(session):
        session_attrs = {k: v for k, v in session.__dict__.items() if not k.startswith('_')}
        session_attrs['cuppings'] = [CuppingModel.from_row(c) for c in session.cuppings]
        return SessionModel(session_attrs, strict=False)
