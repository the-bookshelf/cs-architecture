import simplejson as json


def decode_json(fn):
    def _decode_json_from_payload(payload):
        try:
            json_payload = json.loads(payload['body'])
        except (TypeError, json.JSONDecodeError) as e:
            json_payload = None
        return fn(json_payload)
    return _decode_json_from_payload
