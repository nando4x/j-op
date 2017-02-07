/**
 * Base libraries 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    services.js
 * 
 * @date      17 nov 2016 - 17 nov 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

var constant = (function(){
	var xx = 10;
	return {
		init: xx
	}
})();

(function () {
	// Constant definition
	var i = 0;
	this.const = Object.freeze({
		"RESP_TYPE_BLOCK": i++,
		"RESP_TYPE_HTML": i++
	});
	// private variable and constant
	var JOP_ID_PARAMETER = "Jop.jopId";
	/**
	 * Post form data of one block
	 * @param	  jopId block identify
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception Jop.core.exception 
	 * @return	  response	  
	 */
	this.postBlock = function (jopId) {
		var block = Jop.core.getBlockElement(jopId);
		var list = Jop.core.querySelectorAll(block,'input[name]');
		var request = new FormData;
		request.append(JOP_ID_PARAMETER,jopId); 
		for ( var ix=0; ix<list.length; ix++ ) {
			request.append(list[ix].name,list[ix].value); 
		}
		ajax("post","jopservices/postblock",true,request,null,null);
	};

	// ajax generic low-level function
	//
	//
	function ajax(method,url,async,data,successCallback,errorCallback) {
		// create XML http request
		var xhr;
		if (window.XMLHttpRequest)
			xhr = new XMLHttpRequest();
		else if (window.ActiveXObject)
			xhr = window.ActiveXObject( "Microsoft.XMLHTTP" );
		// define callback
		callback = function() {
			switch (xhr.status) {
				case 200: // response received
					var type = xhr.getResponseHeader("Jop-Response-Type");
					if ( successCallback != 'undefined' && successCallback != null )
						successCallback(xhr.responseText,type);
					break;
				default:
					if ( errorCallback != 'undefined' && errorCallback != null )
						errorCallback(xhr.status,xhr.statusText);
					break;
			}
			xhr.onreadystatechange = function(){};
		} 
		try {
			// open channel
			xhr.open(method,url,async);
			try {
				// set header
				xhr.setRequestHeader("Content-Type", "text/plain");
			} catch (e) { //TODO: manage error set header 
			}
			try {
				// send data and manage response, immediately if sync
				xhr.send(data);
				if (!async) {
					callback();
				} else if ( xhr.readyState === 4 ) {
					// (IE6 & IE7) if it's in cache and has been
					// retrieved directly we need to fire the callback
					window.setTimeout( callback );
				} else {
					xhr.onreadystatechange = callback;
				}
			} catch (e) { //TODO: manage error send 
			}
		} catch (e) { //TODO: manage error open	
		}
	}
	    
	}).apply( Jop.core.services );  


