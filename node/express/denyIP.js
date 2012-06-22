var app = require('express').createServer();

app.all('*', function(req,res, next) {
	var ip = req.connection.remoteAddress;
	console.log(ip)

	var match = /192\.168\.1\.*/;

	if( ip.match(match) ) {
		next();
	} else {
		res.send('IP denied', 401);
	}
});

app.get('/', function(req, res){
  res.send('hello world');
});

app.listen(3000);
