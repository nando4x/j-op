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
	this.CONST = Object.freeze({
		"DEBUG": true
	});
	this.EVENT = Object.freeze({
		"DOMLOADED": 'jop_domloaded'
	});
	
	// private variables
	var queue = { 
					forms: [],
					scripts: []
	}
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
	this.exception = function(code,msg){
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
	 * Inject html to the block DOM element of jopId.<br>
	 * Execute script before (with attribute jop_before) inject and normal script after inject immediately or queued and execute on jop_domloaded event 
	 * @param	  jopId block identifier
	 * @param	  html html data to inject
	 * @param	  queued if true not execute immediate but on jop_domloaded
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  block DOM element
	 */
	this.injectBlockElement = function(jopId,html,queued){
		// TODO: execute scripts like jquery ajax and plus script before (if has attribute jop_before)
		// execute script before
		var tmp = document.createElement('div');
		tmp.innerHTML = html;
		var lst = tmp.querySelectorAll('script[jop_before="true"]');
		for ( var ix=0; lst!=null&&ix<lst.length; ix++ ) {
			eval.call(window,lst[ix].text);
		}
		// inject into block
		var block = this.querySelector(document,'[jop_id="'+jopId+'"]');
		if ( block != null ) {
			block.outerHTML = html;
			// refresh block and register form
			block = this.querySelector(document,'[jop_id="'+jopId+'"]');
			this.registForm(block);
			// execute script after immediately or queued 
			var lst = block.querySelectorAll('script:not([jop_before="true"])');
			for ( var ix=0; lst!=null&&ix<lst.length; ix++ ) {
				if ( queued )
					queue.scripts.push(lst[ix].text);
				else
					eval.call(window,lst[ix].text);
			}
		} else 
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
		if ( element != null )
			return element.querySelector(query);
		return null;
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
		if ( element != null )
			return element.querySelectorAll(query);
		return null;
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
		if ( this.CONST.DEBUG ) {
			var s = new String();
			for ( var ix=1; ix<arguments.length; ix++) {
				msg = msg.replace("%"+ix,arguments[ix]);
			}
			if ( arguments.callee.caller != null )
				msg = "("+arguments.callee.caller.name + ") " + msg;
			console.log(msg);
		}
	}
	/**
	 * Document ready function: execute callback when document is ready 
	 * @param	  callback function to call
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  
	 */
	this.ready = function(callback) {
		 if (document.readyState != 'loading') {
			 callback();
		 } else if (document.addEventListener) {
		    document.addEventListener('DOMContentLoaded', callback);
		 } else {
		    document.attachEvent('onreadystatechange', function() {
			    if (document.readyState != 'loading')
			    	callback();
				    
	    	});
	 	}	
	}
	/**
	 * Register new form submissions.
	 * Select all jop block forms without action and add event onsubmit to post block 
	 * @param	  element	element DOM to scan. if null scan all document
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  
	 */
	this.registForm = function(element) {
		function _do(el) {
			var act = el.getAttribute('action');
			if ( act == null || act.length > 0 ) {
				var id = el.getAttribute('jop_id');
				// check if already present in queue otherwise add block id in queue and add event to block 
				if ( queue.forms.indexOf(id) < 0 )
					queue.forms.push(id);
				el.addEventListener('submit',formSubmitHandler);
			}
		}
		var el = document;
		if ( element != undefined && element != null ) {
			el = element;
			if ( el.tagName == 'FORM' )
				_do(el);
		}
		// scan forms and check if action is defined
		var forms = el.querySelectorAll('form[jop_id]');
		for ( var ix=0; forms!=null && ix<forms.length; ix++ ) {
			_do(forms[ix]);
		}
	}
	// form submission event handler
	//
	//
	function formSubmitHandler(event) {
		event.preventDefault();
		if ( typeof Jop.core.services.postBlock == 'function' )
			Jop.core.services.postBlock(this.getAttribute('jop_id'));
	}
	/**
	 * Trigger an event
	 * @param	  event	event name to trigger
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  
	 */
	this.trigger = function(event) {
		if ( typeof window.CustomEvent == 'function' )
			var evt = new CustomEvent(event);
		else { // IE case
			var evt = document.createEvent('Event');
			evt.initEvent(event, true, true);
		}
	    window.dispatchEvent(evt);
	}
	/**
	 * Show alert dialog box
	 * @param	  title	dialogbox title
	 * @param	  msg	message to show
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  
	 */
	this.showAlert = function(title,msg) {
		var tmp = document.createElement('div');
		tmp.id = "jop_alertdialog";
		tmp.innerHTML = "<div class='header'><span class='icon_close' onclick='this.parentElement.parentElement.parentElement.removeChild(this.parentElement.parentElement);'/></div>"
						+"<p>"+msg+"</p>";
	}
	// init process
	//
	//
	window.addEventListener(this.EVENT.DOMLOADED, function (e) {
	    while ( queue.scripts.length > 0 ) {
	    	var scr = queue.scripts.shift();
			eval.call(window,scr);
	    }
	});
	// also introduce a new sub-namespace
	//this.tools = {};
	
}).apply( Jop.core );

// Attach event for document ready
//
//
Jop.core.ready(function(){
	Jop.core.registForm(null);
});