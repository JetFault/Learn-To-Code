var http = require('http');

var requests_received = 0;

http.createServer(function(req, res) {

  requests_received++;
 // process.stdout.write("Requests received " + requests_received + "\r");

  res.statusCode = 200;
  res.end();

}).listen(3000);
