import pytest

from cupping.helpers import (
        utcnow,
)


def test_utcnow():
    assert utcnow()
