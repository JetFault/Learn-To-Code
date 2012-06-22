var redis = require('redis');
var redisClient = redis.createClient('5555', '127.0.0.1');

/**
 * Query Redis for emails still needing to be parsed
 * Should return error if no items exist
 * @param user_name
 * @param cb - Callback(err, 
 *		[ {uid: }, {msgid: } ],...
 * Note: Will return NO_EMAILS_TO_PARSE error when
 * no emails to parse
 */
var getUIDAttachList = function(user_name, cb) {
	var attachList = [];

	//Pops in Series
	var popAll = function(done_cb) {
		redisClient.lpop(user_name, function(err, msg_id) {
			if(err) {
				logger.error("Error popping msg_id from " + user_name + ": "+ err);
			} else {
				if(!msg_id) {
					done_cb();
				} else {
					redisClient.hgetall(msg_id, function(err, email) {
						if(err) {
							logger.error("Error getting hash for " + msg_id + ": " + err);
						} else {
							attachList.push(email);
						}
						popAll(done_cb);
					});
				}
			}
		});
	};

	popAll(function() {
		if(attachList.length === 0) {
			cb(NO_EMAILS_TO_PARSE);
		} else {
			cb(null, attachList);
		}
	});
};


getUIDAttachList('bob', function(err, attachList) {
	if(err)
		console.log(err);
	else {
		console.dir(attachList);
	}
});
