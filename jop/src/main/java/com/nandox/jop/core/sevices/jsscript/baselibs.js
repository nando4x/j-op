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

/*
 *  Create namespaces
 */
var Jop = Jop || {};
Jop.core = Jop.core || {};
Jop.core.services = Jop.core.services || {};

(function () {
	
	// Constant definition
	var i = 0;
	this.const = Object.freeze({
		"DEBUG": true
	});
	/**
	 * General Exception
	 * @param	  code exception code
	 * @param	  msg exception message
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	this.excepion = function(code,msg){
		var code = code;
		var msg = msg;
	};	
	/**
	 * Return the block DOM element of jopId
	 * @param	  jopId block identifier
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  block DOM element
	 */
	this.getBlockElement = function(jopId){
		return this.querySelector(document,'[jop_id="'+jopId+'"');
	}
	/**
	 * Return the block DOM element of jopId
	 * @param	  jopId block identifier
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  block DOM element
	 */
	this.injectBlockElement = function(jopId,html){
		var block = this.querySelector(document,'[jop_id="'+jopId+'"');
		if ( block != null )
			block.outerHTML = html;
		else 
			this.debugger("not found block with id:%1",jopId);
	}
	/**
	 * General single element CSS query selector 
	 * @param	  element element to search inside
	 * @param	  query CSS query
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  block DOM element
	 */
	this.querySelector = function(element,query) {
		return element.querySelector(query);
	}
	/**
	 * General all element CSS query selector 
	 * @param	  element element to search inside
	 * @param	  query CSS query
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  block DOM element
	 */
	this.querySelectorAll = function(element,query) {
		return element.querySelectorAll(query);
	}
	/**
	 * Console formatted message if DEBUG it's true.
	 * Es. Jop.core.debbuger("test %1 and %2",12,"hello")
	 * 	   "test 12 and hello" 
	 * @param	  msg formatted message
	 * @param	  ... variable arguments
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  
	 */
	this.debugger = function(msg) {
		if ( this.const.DEBUG ) {
			var s = new String();
			for ( var ix=1; ix<arguments.length; ix++) {
				msg = msg.replace("%"+ix,arguments[ix]);
			}
			if ( arguments.callee.caller != null )
				msg = "("+arguments.callee.caller.name + ") " + msg;
			console.log(msg);
		}
	}
	// also introduce a new sub-namespace
	//this.tools = {};
	    
}).apply( Jop.core );  