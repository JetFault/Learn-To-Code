var redis = require('redis');
var redisHost = '127.0.0.1';
var redisPort = '6379';
var subscriber = redis.createClient(redisPort, redisHost);
var redisClient = redis.createClient(redisPort, redisHost);
var CHANNEL = 'User';
var USERS = 'ListOfUsers';

var parseUser = function(user) {
  //Do Something to parse
};

redisClient.on('connect', function() {
	var popAll = function(done_cb) {
		redisClient.lpop(USERS, function(err, user) {
			if(err) {
				console.log(err);
			} else if(!user) {
				done_cb();
			} else {
				console.log('Parsing unfinished user: ' + user);
				parseUser(user);
				popAll(done_cb);
			}
		});
	};

	popAll(function() {
		console.log("Parsed all unfinished users");
	});

});

redisClient.on('error', function(err) {
	console.log('Redis Reader Error: ' + err);
});

subscriber.on('error', function(err) {
	console.log('Redis Subscriber Error: ' + err);
});

subscriber.on('subscribe', function(channel, count) {
	console.log("Subscribed to channel: " + channel);
});

subscriber.on('message', function(channel, message) {
	console.log(channel + " -msg- " + message);
	parseUser(message);
});

//Subscribe
subscriber.subscribe(CHANNEL);
