require 'json'

response = JSON.parse(ARGV[1])
headers = response['headers']
current_value = headers['x-rate-limit-remaining'].to_i

if File.exists?('rate-limit.txt')
  current_value = File.read('rate-limit.txt').to_i
end

if current_value <= 0
  response['statusCode'] = 429
  response['body'] = {
    'errors' => [{
      'code' => 88, 'message' => 'Rate limit exceeded'
    }]
  }
  response['headers']['x-rate-limit-remaining'] = 0
else
  File.write('rate-limit.txt', current_value - 25)
  headers['x-rate-limit-remaining'] = current_value - 25
end

puts response.to_json
