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
	this.querySelector = function(element,query) {
		return element.querySelector(query);
	}
	this.querySelectorAll = function(element,query) {
		return element.querySelectorAll(query);
	}
	// also introduce a new sub-namespace
	//this.tools = {};
	    
}).apply( Jop.core );  