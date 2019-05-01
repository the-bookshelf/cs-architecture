'use strict';

const aws = require('aws-sdk');
const Jimp = require("jimp");


module.exports.uploadImage = (event, context, callback) => {
  var params = {
    FunctionName: 'fanout-dev-ResizeImage'
    , InvocationType: "Event"
  }

  var sizes = [128, 256, 1024];

  const s3Objects = event['Records'].map(function(r) {
    return r["s3"]
  })

  const lambda = new aws.Lambda({
    region: 'us-west-2'
  });

  for (var i=0; i<sizes.length; i++) {
    params['Payload'] = JSON.stringify({
      "size": sizes[i]
      , "s3Objects": s3Objects
    });

    lambda.invoke(params, function(error, data) {
      if (error) {
        callback(error)
      } else {
        callback(null, 'success')
      }
    });
  }

};


module.exports.resizeImage = (event, context, callback) => {

  const size = event.size;
  const S3 = new aws.S3();

  event.s3Objects.map(function(s3Object) {
    var bucket = s3Object.bucket.name;
    var key = s3Object.object.key;
    var parts = key.split('.');
    var name = parts[0];
    var suffix = parts[1];

    function uploadToS3(err, buffer) {
      const keyName = name + "-" + size + "." + suffix
      var params = {
        Body: buffer,
        Bucket: bucket + '-results',
        Key: keyName
      }

      S3.putObject(params, function(err, data) {
        if ( err ) {
          callback(err);
        } else {
          console.log('successfully uploaded resized image: ' + keyName)
          callback(null, "success");
        }
      })
    }

    S3.getObject({Bucket: bucket, Key: key}, function(err, data) {
      if ( err ) {
        console.log('Error reading S3 item: ' + bucket + ' ' + key);
      } else {
        Jimp.read(data.Body, function(err, buffer) {
          buffer
            .resize(size, Jimp.AUTO)
            .getBuffer( Jimp.MIME_JPEG, uploadToS3 )
        })
      }
    });

    callback(null, "success");

  });

};
