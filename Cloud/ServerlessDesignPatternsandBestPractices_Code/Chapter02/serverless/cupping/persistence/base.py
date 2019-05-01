from sqlalchemy import (
        Column,
        Integer,
        DateTime,
        text,
)

from ..db.mixins import (
        CuppingServiceModelMixin,
)

from ..helpers import utcnow


class CuppingServiceBaseMixin(CuppingServiceModelMixin):
    id =  Column(Integer, primary_key=True)

    created = Column(DateTime,
            nullable=False,
            server_default=text('NOW()'),
    )
    updated = Column(DateTime,
            nullable=False,
            server_default=text('NOW()'),
            onupdate=utcnow,
            #server_onupdate=text('NOW()'),
    )
