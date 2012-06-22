var eventEmit = require('./eventEmit');

module.exports = eventCatch;

function eventCatch() {
	this.eventEmitA = new eventEmit("a");
	//this.eventEmitA.on('test', function() {
//		console.log("SDF");
//	});
	this.eventEmitB = new eventEmit("b");
	this.attachHandler();
	console.log('constructor catch');
}

var eventHandler = function() {
	if(this === self.eventEmitA) {
		console.log("A called this event");	
	}
	else if(this === self.eventEmitB) {	
		console.log("B called this event");
	}

};


eventCatch.prototype.attachHandler = function()  {
	var self = this;

	var eventHandler = function() {
		if(this === self.eventEmitA) {
			console.log("ObjA called this event. Name: " + this.name);	
		}
		else if(this === self.eventEmitB) {	
			console.log("ObjB called this event. Name: " + this.name);
		}

	};

	this.eventEmitA.on('test', eventHandler);
	this.eventEmitB.on('test', eventHandler);
};


eventCatch.prototype.throwEvents = function() {
	var self = this;
	self.eventEmitA.emitTest();
	self.eventEmitB.emitTest();
};

eventCatch.prototype.swap = function() {
	var self = this;
	var tmp = self.eventEmitA;
	self.eventEmitA = self.eventEmitB;
	self.eventEmitB = tmp;
}

