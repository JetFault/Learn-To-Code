var async = require('async');

var redis = require('redis');
var redisHost = '127.0.0.1';
var redisPort = '6379';
var redisClient = redis.createClient(redisPort, redisHost);
var CHANNEL = 'User';
var USERS = 'ListOfUsers';

redisClient.on('error', function(err) {
	console.log('Redis Publisher Error: ' + err);
});

var pubAttach = function(user, UID, msg_id) {

	async.series({
		hash: function(cb) {
			redisClient.hmset(msg_id, {'uid': UID, 'user': user}, cb);
		},
		msgid: function(cb) {
			redisClient.rpush(user, msg_id, cb);
		},
		pub: function(cb) {
			redisClient.publish(CHANNEL, user, cb);
		}},
		function(err, res) {
			if(err) {
				console.log('Error publishing: ' + err);
			} else if(res.pub == 0) {
				console.log('No one subscribed');
				redisClient.rpush(USERS, user, function(err) {
					if(err) {
						console.log('Error adding user to: ' + USERS + ' : ' + err);
					}
				});
			}						
		}
	);
};

setTimeout(function() {pubAttach('jerry', 'UID' + 2342, 'msgid' + 10);}, 500);
setTimeout(function() {pubAttach('jerry', 'UID' + 1, 'msgid' + 1);}, 1000);
setTimeout(function() {pubAttach('jerry', 'UID' + 2, 'msgid' + 2);}, 1500);
setTimeout(function() {pubAttach('jerry', 'UID' + 3, 'msgid' + 3);}, 2000);
setTimeout(function() {pubAttach('bob', 'UID' + 1, 'msgid' + 4);}, 2500);
setTimeout(function() {pubAttach('bob', 'UID' + 2, 'msgid' + 5);}, 3000);
setTimeout(function() {pubAttach('jerry', 'UID' + 4, 'msgid' + 6);}, 3100);
setTimeout(function() {pubAttach('bob', 'UID' + 3, 'msgid' + 7);}, 3100);
setTimeout(function() {pubAttach('kerry', 'UID' + 1, 'msgid' + 8);}, 4000);
setTimeout(function() {pubAttach('jerry', 'UID' + 6, 'msgid' + 9);}, 4010);
