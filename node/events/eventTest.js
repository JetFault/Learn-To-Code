var eventCatch = require('./eventCatch');

var eventCatcher = new eventCatch();

eventCatcher.throwEvents();

eventCatcher.swap();

eventCatcher.eventEmitA.emitTest();
