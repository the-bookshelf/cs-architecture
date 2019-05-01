import os
import tweepy

from .queue import publish_tweet

consumer_key = os.environ['TWITTER_CONSUMER_KEY']
consumer_secret = os.environ['TWITTER_CONSUMER_SECRET']

access_token = os.environ['TWITTER_ACCESS_TOKEN']
access_token_secret = os.environ['TWITTER_ACCESS_SECRET']


class PhotoStreamListener(tweepy.StreamListener):

    def _get_media_urls(self, media):
        if not media:
            return []

        return [m['media_url_https'] for m in media if m['type'] == 'photo']

    def _get_hashtags(self, container):
        hashtags = [h['text'] for h in container.get('hashtags', ())]
        return [h for h in hashtags if '#' + h in self.tags]

    def on_status(self, tweet):
        container = tweet._json

        entities = container.get('entities', {}).get('media')
        extended_entities = container.get('extended_entities', {}).get('media')
        extended_tweet = container.get('extended_tweet', {}).get('entities', {}).get('media')

        all_urls = set()
        for media in (entities, extended_entities, extended_tweet):
            urls = self._get_media_urls(media)
            all_urls.update(set(urls))

        hashtags = self._get_hashtags(container.get('entities', {}))

        if all_urls:
            for url in all_urls:
                publish_tweet({
                    'text': tweet.text,
                    'url': url,
                    'hashtags': hashtags,
                })

    @staticmethod
    def start(tags=None):
        tags = tags or ['#dog', '#dogs', '#puppy', '#cat', '#kitty', '#lolcat', '#kitten']

        auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
        auth.set_access_token(access_token, access_token_secret)

        api = tweepy.API(auth)

        listener = PhotoStreamListener()
        listener.tags = tags
        stream = tweepy.Stream(auth=api.auth, listener=listener)
        try:
            stream.filter(track=tags)
        except Exception as e:
            print 'Shutting down'
            print e
