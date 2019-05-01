import os
import boto3


TABLE_NAME = os.environ['DYNAMODB_RESULTS_TABLE_NAME']
AWS_REGION = os.environ['AWS_REGION']



class ClassiferResults(dict):

    def __init__(self, url, *args, **kwargs):
        super(ClassiferResults, self).__init__(*args, **kwargs)
        self.__db = boto3.resource('dynamodb', region_name=AWS_REGION)
        self.__table = self.__db.Table(TABLE_NAME)

        self.__url = url

        data = self.__table.get_item(Key={'url': url})
        record = data.get('Item', {})
        self.update(record)

    @property
    def exists(self):
        return not self.is_empty

    @property
    def is_empty(self):
        return self == {}

    def save(self):
        item = {k: v for k, v in self.iteritems()}
        return self.__table.put_item(Item=item)

    def upsert(self, **kwargs):
        kwargs['url'] = self.__url
        self.update(kwargs)
        self.save()
