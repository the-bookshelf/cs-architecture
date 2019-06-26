require 'json'

response = JSON.parse(ARGV[1])
response['body']['timestamp'] = Time.now.getutc
puts response.to_json
