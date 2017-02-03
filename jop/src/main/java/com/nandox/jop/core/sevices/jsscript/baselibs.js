/**
 * Base libraries 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    baselibs.js
 * 
 * @date      17 nov 2016 - 17 nov 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */


Function.prototype.setScope = function(obj) {
	var method = this;
	var arg = [];
	for(var i = 1; i<arguments.length; i++) arg.push(arguments[i]);
		temp = function() {
		return method.apply(obj, arg);
	};
	return temp;
}

Function.prototype.inherits = function(superclass) {
	var temp = function() {};
	temp.prototype = superclass.prototype;
	this.prototype = new temp();
} 

Array.prototype.exists = function(value) {
	for(var i = 0; i<this.length; i++) if(this[i] == value) return true;
	return false;
}

Array.prototype.remove = function(value) {
	for(var i = 0; i<this.length; i++) if(this[i] == value) this.splice(i,1);
}


var class1 = (function() {
	function class1(arg) {
		this.arg = arg;
	}
	class1.description = "class1";
})();