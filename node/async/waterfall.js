var async = require('async');

async.waterfall([
	function(cb) {
		var a = 1;
		var b = 2;
		cb(null, a, b);
	},
	function(cb) {
		var aa = a + 10;
		var bb = b + 10;
		cb(null, aa, bb);
	}
],
function(err, result) {
	console.log(err);
	console.log(result);
});

// an example using an object instead of an array
async.series([
    function(callback){
        setTimeout(function(){
            callback(null, 1);
        }, 200);
    },
    function(callback){
        setTimeout(function(){
            callback(null, 2);
        }, 100);
    }
],
function(err, results) {
	//console.log(results);
    // results is now equal to: {one: 1, two: 2}
});

async.waterfall([
    function(callback){
        callback(null, 'one', 'two');
    },
    function(arg1, arg2, callback){
        callback(null, 'three');
    },
    function(arg1, callback){
        // arg1 now equals 'three'
        callback(null, 'done');
    }
], function (err, result) {
	//console.log(result);
   // result now equals 'done'    
});
