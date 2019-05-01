import factory

from factory.fuzzy import (
        FuzzyAttribute,
        FuzzyChoice,
        FuzzyDecimal,
        FuzzyInteger,
)

from cupping.db import commit_session
from cupping.persistence import (
        Cupping,
        Session,
)


_SCORE_CHOICES = (
        ('Aroma', FuzzyDecimal(1, 10, precision=0)),
        ('Flavor', FuzzyDecimal(1, 10, precision=0)),
        ('Acidity', FuzzyDecimal(1, 10, precision=0)),
        # ('Body': FuzzyInteger(1, 10)),
        # ('Uniformity': FuzzyInteger(1, 10)),
        # ('Overall': FuzzyInteger(1, 10)),
)

def _generate_scores():
    return {k: v.fuzz() for (k, v) in _SCORE_CHOICES}


class BaseFactory(factory.Factory):

    @classmethod
    def _create(cls, model_class, *args, **kwargs):
        inst = model_class(*args, **kwargs)
        inst.save()
        commit_session()
        return inst

    # @classmethod
    # def create_batch(cls, size, **kwargs):
    #     objects = super().create_batch(size, **kwargs)
    #     return objects

class SessionFactory(BaseFactory):
    class Meta:
        model = Session

    name = factory.Sequence(lambda n: u'Cupping session %d' % n)
    form_name = FuzzyChoice(('SCAA', 'COE'))


class CuppingFactory(BaseFactory):
    class Meta:
        model = Cupping

    name = factory.Sequence(lambda n: u'Coffee %d' % n)
    scores = FuzzyAttribute(_generate_scores)
    overall_score = FuzzyDecimal(60, 100, precision=1)
