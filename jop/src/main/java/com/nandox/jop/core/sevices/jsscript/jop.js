var Jop = Jop || {};

(function () {
	var _lib = this;
	// Constant definition
	var i = 0;
	this.CONST = Object.freeze({
		"DEBUG": true,
		"SPINNERENABLE": ${initpar:jop.browser.spinnerenable}		
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
	 * Return the block DOM element of jopId
	 * @param	  jopId block identifier
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  block DOM element
	 */
	this.getBlockElement = function(jopId){
		return this.Dom.querySelector(document,'[jop_id="'+jopId+'"');
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
		var block = this.Dom.querySelector(document,'[jop_id="'+jopId+'"]');
		if ( block != null ) {
			block.outerHTML = html;
			// refresh block and register form
			block = this.Dom.querySelector(document,'[jop_id="'+jopId+'"]');
			if ( block == null ) 
				return; // block is been deleted
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
		function formSubmitHandler(event) {
			event.preventDefault();
			if ( typeof _lib.Services.postBlock == 'function' )
				_lib.Services.postBlock(this.getAttribute('jop_id'));
		}
		function _do(el) {
			var act = el.getAttribute('action');
			
			if ( act == null || act.length <= 0 ) {
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
		tmp.innerHTML = "<div class='background'></div>"+
						"<div class='dialog'>"+
						"<div class='header'><span class='title'>"+title+"</span><span class='icon_close' onclick='document.getElementById(\"jop_alertdialog\").parentElement.removeChild(document.getElementById(\"jop_alertdialog\"));'/></div>"+
						"<div class='content'>"+msg+"</div>"+
						"</div>";
		document.body.appendChild(tmp);
	}
	/**
	 * Show spinner on specific block
	 * @param	  jopId block identifier
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  
	 */
	this.turnBlockSpinner = function(jopId,enable) {
		var el = this.getBlockElement(jopId);
		if ( 
			(this.CONST.SPINNERENABLE && el.getAttribute('jop_force_spinner') != 'false')
													||
			(!this.CONST.SPINNERENABLE && el.getAttribute('jop_force_spinner') == 'true')
			) {
			if ( el != null ) {
				(enable?this.Dom.addClass(el,"jop_spinner"):this.Dom.removeClass(el,"jop_spinner"));
			}
		}
	}

	/**** init process ****/
	//
	// add event to process queue 
	window.addEventListener(this.EVENT.DOMLOADED, function (e) {
	    while ( queue.scripts.length > 0 ) {
	    	var scr = queue.scripts.shift();
			eval.call(window,scr);
	    }
	});
	
	// init library modules
	window.libNxModuleInit(this);
	
	// attach event for document ready
	this.Event.ready(function(){
		_lib.registForm(null);
	});
}).apply( Jop );
