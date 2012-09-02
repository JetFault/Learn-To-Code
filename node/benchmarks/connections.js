var http = require('http');

var REQUESTS;

if(process.env.REQUESTS) {
  REQUESTS = process.env.REQUESTS;
} else {
  REQUESTS = 1000;
}

console.log(REQUESTS + " requests sending");

var options = {
  host: "localhost",
  port: "3000",
  method: "GET"
};

var requests = 0;

var requests_passed = 0;
var requests_failed = 0;

var start_time;

function finish(end_time) {
  var duration = end_time - start_time;
  console.log(REQUESTS + " requests sent");
  console.log(requests_passed + " requests passed");
  console.log(requests_failed + " requests failed");
  console.log(duration + " milliseconds taken");
  console.log((duration / 1000) + " seconds taken");
}

function request(i) {
  requests++;
  options.path = "/" + i;

  http.request(options, function(res) {
    if(res.statusCode == 200) {
      requests_passed++;
    } else {
      console.dir("ERROR:" + res.statusCode);
      requests_failed++;
    }

    if(REQUESTS == (requests_failed + requests_passed) ) { 
      finish(new Date().getTime());
    }
  }).end();
  
}

start_time = new Date().getTime();

for(var i = 0; i < REQUESTS; i++) {
  request(i);
}

