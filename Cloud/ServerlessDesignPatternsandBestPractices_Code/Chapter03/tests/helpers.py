import functools
import simplejson as json


def _assert_status_code(code, response):
    assert response['statusCode'] == code


assert_200 = functools.partial(_assert_status_code, 200)
assert_404 = functools.partial(_assert_status_code, 404)
assert_500 = functools.partial(_assert_status_code, 500)

def get_body_from_response(response):
    return json.loads(response['body'])
