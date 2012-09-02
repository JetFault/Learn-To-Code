var events = require('events');
var sys = require('util');

module.exports = eventEmit;

function eventEmit(name) {
	this.name = name;
	events.EventEmitter.call(this);
}

sys.inherits(eventEmit, events.EventEmitter);

// inherit events.EventEmitter
/*eventEmit.super_ = events.EventEmitter;
eventEmit.prototype = Object.create(events.EventEmitter.prototype, {
    constructor: {
        value: eventEmit,
        enumerable: false
    }
});
*/
eventEmit.prototype.emitTest = function() {
	var self = this;
	self.emit('test');
//	return self;
}
