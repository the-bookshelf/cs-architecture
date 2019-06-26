function (requestData, logger) {
  var path = require('path'),
      parserPath = path.join(process.cwd(), 'src/RemotingProtocolParser/nodejs/lib/remotingProtocolParser'),
      r = require(parserPath).tcpReader(requestData);

  // The reader is stateful and tracks where we are with an offset variable,
  // which is updated every time we do a read operation. Since the length of
  // the headers is variable, we have to execute all the read operations before
  // we get the correct offset for the content. The contentLength isn't the length
  // of the entire message; it's the length of the actual content section.
  logger.debug('Preamble: %s', r.readPreamble());
  logger.debug('MajorVersion: %s', r.readMajorVersion());
  logger.debug('MinorVersion: %s', r.readMinorVersion());
  logger.debug('Operation: %s', r.readOperation());
  logger.debug('ContentDelimiter: %s', r.readContentDelimiter());
  logger.debug('ContentLength: %s', r.readContentLength());
  logger.debug('Headers: %s', JSON.stringify(r.readHeaders()));

  var expectedLength = r.offset + r.contentLength + 1;
  logger.info('Expected length: %s, actual length: %s', expectedLength, requestData.length);
  return requestData.length >= expectedLength;
}
