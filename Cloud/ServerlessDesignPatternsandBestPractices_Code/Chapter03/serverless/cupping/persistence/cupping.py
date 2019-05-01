from .base import CuppingServiceBaseMixin
from ..db.mixins import Base

from sqlalchemy.orm import relationship, validates
from sqlalchemy.dialects.postgresql import JSONB

from sqlalchemy import (
                Boolean,
                Column,
                ForeignKey,
                Integer,
                Numeric,
                String,
)


class Cupping(CuppingServiceBaseMixin, Base):
    """An individual cupping for one object (roast)."""
    __tablename__ = 'cuppings'

    #: the `Session` this cupping is a part of
    session_id = Column(Integer, ForeignKey('sessions.id'), nullable=False)

    #: The name of this coffee that was cupped
    name = Column(String(length=127))

    # `Cupping.session` refers to a `Session` instance, and on the other side, `Session.cuppings`
    # refers to a list of `Cupping` instances.
    session = relationship('Session', back_populates='cuppings')

    #: This is the real juicy bit.  `scores` is a list of key/value, where key is column name
    #: with a matching numeric score.  Note, we index on the keys to supporty querying.
    scores = Column(JSONB, nullable=False)

    #: Final score for this coffee
    overall_score = Column(Numeric(precision=4, scale=1))

    #: A list of descriptors
    descriptors = Column(JSONB, nullable=True)

    #: A list of defects
    defects = Column(JSONB, nullable=True)

    #: General notes
    notes = Column(String(length=255))

    #: tells whether or not the object is a sample
    is_sample = Column(Boolean, default=False)

    def __repr__(self):
        return '<Cupping(id=%s, name=%s, session_id=%s, overall_score=%s)>' % (
                self.id or 'unsaved', self.name, self.session_id, self.overall_score)

    def _validate_list_or_tuple(self, key, value):
        if not value:
            return None
        if not isinstance(value, (list, tuple)):
            raise ValueError('%s must be a list of strings' % (key, ))

        for _string in value:
            try:
                assert isinstance(_string, str)
            except AssertionError:
                raise ValueError('%s must be a list of strings' % (key, ))

        return [str(v).strip() for v in value]

    @validates('scores')
    def validate_scores(self, key, value):
        """Validate scores and cast to float.

        Validates that scores are None if falsey, or a dictionary. When a valid dictionary arrives,
        cast to floats since Decimal objects cannot be serialized to json.

        """
        if not value:
            return None
        if not isinstance(value, dict):
            raise ValueError('Scores must be a mapping of name to numeric value')

        try:
            return {k: float(v) for k, v in value.items()}
        except ValueError:
            raise ValueError('Scores must be a mapping of name to numeric value')

    @validates('descriptors')
    def validate_descriptors(self, key, value):
        return self._validate_list_or_tuple(key, value)

    @validates('defects')
    def validate_defects(self, key, value):
        return self._validate_list_or_tuple(key, value)

    @classmethod
    def from_model(cls, model, session_id=None):
        session_id = session_id or model.session_id
        cupping = cls(
                session_id=session_id,
                name=model.name,
                scores=model.scores,
                overall_score=model.overall_score,
                descriptors=model.descriptors,
                defects=model.defects,
                notes=model.notes,
                is_sample=model.is_sample,
        )
        cupping.save()
        return cupping
