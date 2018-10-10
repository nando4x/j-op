function libNxModule_Event(libroot) { 
	var Event = {};
	(function () {
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
	}).apply( Event );
	libroot.Event = Event;
};

if ( typeof libNxModuleInit !== 'function' ) {
	var libNxModuleInit = function(libroot) {
		var lst = Object.keys(window);
		var k = lst.filter(function(key){ return key.indexOf("libNxModule_") > -1;});
		for ( item in k )
			window[k[item]](libroot);
	}
}