var async = require('async');

var obj = [];

var id = 123;
var msg = 'hello';

obj.push({id:msg});

async.forEach(obj, function(item, cb) {
	console.log(item);
	cb(null);
}, 
function(err) {
	if(err)
		console.log(err);
});
