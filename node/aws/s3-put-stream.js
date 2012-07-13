var s3Creds = require('./aws-creds.js');

var fs = require('fs');
var assert = require('assert');
var s3 = require('./node_modules/aws2js').load('s3', s3Creds.access_id, s3Creds.secret_key);
var path = 'foo-stream.txt';
var stream = fs.ReadStream('./foo.txt');

var callbacks = {
	put: false,
	get: false,
	del: false
};

console.dir(s3Creds);

s3.setCredentials(s3Creds.access_id, s3Creds.secret_key);
s3.createBucket(s3Creds.bucket, false, false, function(error, result) {
  console.log("CREATEBUCKET");
  console.dir(error);
  console.dir(result);
});

s3.putStream(path, stream, false, {'content-length': 4, 'content-type': 'text/plain'}, function (err, res) {
  console.log("PUTSTREAM");
  if(err) {
    console.dir(err);
  }
  console.dir(res);

	s3.get(path, 'buffer', function (err, res) {
    console.log("GET");
    if(err) {
      console.dir(err);
    }
    console.dir(res);
		//s3.del(path, function (err) {
		//});
	});
});
