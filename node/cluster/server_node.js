var cluster = require('cluster');
var numCPUs = require('os').cpus().length;
var logger = require('./tracelog.js').makeLogger('logs/server.log');

var workers = [];

if (cluster.isMaster) {
  
  //Kill all children when parent dies
  process.on('SIGTERM', function() {
    logger.info("Received SIGTERM kill all children");
    for(var i = 0; i < workers.length; i++) {
      workers[i].kill('SIGTERM');
    }
    process.exit(1);
  });

  // Fork workers.
  logger.info("Initializing " + numCPUs + " workers");
  for (var i = 0; i < numCPUs; i++) {
    var worker = cluster.fork();
    workers.push(worker);
  }

  /* Handle too many deaths per interval */
  //Check how many deaths every X millseconds
  var deathInterval = 11000;
  //How many deaths per interval to crash
  var dpiThreshold = 2;

  var deathTimeStart = new Date().getTime();
  var numDeaths = 0;


  cluster.on('death', function(worker) {
    logger.warn('Worker ' + worker.pid + ' died');

    //Handle too many deaths per interval
    var deathTimeNow = new Date().getTime();

    var timeDiff = deathTimeNow - deathTimeStart;
    if(timeDiff < deathInterval) {
      numDeaths++;
      if(numDeaths >= dpiThreshold) {
        logger.error("Threshold passed of workers dying per interval");
        //Have time to write to logs
        setTimeout(function(){process.kill(process.pid, 'SIGTERM');}, 500);
      }
    } else {
      if(numDeaths > 0) {
        logger.warn("Workers crashed " + numDeaths + " times in " + 
          timeDiff + " milliseconds");
      }
      deathTimeStart = new Date().getTime();
      numDeaths = 0;
      numDeaths++;
    }

    if(numDeaths < dpiThreshold) {
      for(var i=0; i < workers.length; i++) {
        var work = workers[i];
        if(worker.pid == work.pid) workers.splice(i);
      }
      logger.info('Forking new worker');
      var new_worker = cluster.fork();
      workers.push(new_worker);
    }

  });

} else {
  require('./app.js');
}
