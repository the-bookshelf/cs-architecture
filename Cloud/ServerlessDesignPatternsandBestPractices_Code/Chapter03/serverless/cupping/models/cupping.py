from schematics.exceptions import ValidationError
from schematics.models import Model
from schematics.types import (
        BooleanType,
        DecimalType,
        DictType,
        IntType,
        ListType,
        StringType,
)


class ScoresType(DictType):
    def validate_nonempty(self, value):
        if not value:
            raise ValidationError('This field is required.')
        return value


class CuppingModel(Model):
    id = IntType()
    session_id = IntType()
    name = StringType(max_length=128, min_length=1, required=True)
    scores = ScoresType(DecimalType, required=True)
    overall_score = DecimalType(required=True, min_value=0, max_value=100,
            serialized_name='overallScore')
    descriptors = ListType(StringType)
    defects = ListType(StringType)
    notes = StringType()
    is_sample = BooleanType(default=False, serialized_name='isSample')

    @staticmethod
    def from_row(cupping):
        attrs = {k: v for k, v in cupping.__dict__.items() if not k.startswith('_')}
        return CuppingModel(attrs, strict=False)
