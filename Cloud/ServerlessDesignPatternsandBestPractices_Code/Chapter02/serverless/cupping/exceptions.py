class Http404(Exception):
    pass


class InvalidInputData(Exception):
    def __init__(self, errors):
        self.errors = errors
