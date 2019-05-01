#!/usr/bin/env python

from kombu import Connection, Exchange, Queue

def process_message(body, message):
    print body
    message.ack()

nova_exchange = Exchange('nova', 'topic', durable=False)
nova_queue    = Queue('listener', exchange = nova_exchange, routing_key='#')
conn = Connection('amqp://amqp:secret@192.168.0.10//')
consumer = conn.Consumer(nova_queue, callbacks=[process_message])
consumer.consume()

while True:
    conn.drain_events()
