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
	 * Post form data of one block: use service inject/postBlock that return XML list 
	 * @param	  jopId block identify
	 * @date      03 feb 2017 - 03 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception Jop.core.exception 
	 * @return	  response	  
	 */
	this.postBlock = function (jopId) {
		// get block and its child input
		var block = Jop.core.getBlockElement(jopId);
		var list = Jop.core.querySelectorAll(block,'input[name]');
		var request = JOP_ID_PARAMETER+"="+jopId;
		for ( var ix=0; list!=null&&ix<list.length; ix++ ) {
			request += "&"+list[ix].name+"="+list[ix].value; 
		}
		// define ajax callback to read XML response and block to refresh
		callback = function(response) {
			try {
				var num = response.getElementsByTagName('response')[0].attributes.num.value;
				for (var ix=0; ix<num; ix++) {
					var data = response.getElementsByTagName('block')[ix].lastChild.data;
					var id = response.getElementsByTagName('block')[ix].id;
					Jop.core.injectBlockElement(id, data);
				}
			} catch (e) {
				// TODO: manage response error
			}
		}
		ajax("POST","jopservices/inject/postblock",true,request,callback,null);
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
		callback = function(response) {
			var end = false;
			switch (xhr.status) {
				case 200: // response received
					if ( xhr.readyState == 4 ) {
						if ( successCallback != 'undefined' && successCallback != null ) {
							switch (xhr.getResponseHeader("Content-Type").toLowerCase()) {
								case "text/xml":
									successCallback(xhr.responseXML);
									break;
								default:
									successCallback(xhr.responseText);
									break;
							}
						} else
							Jop.core.debugger("ajax success callback undefined")
						end = true;
					}
					break;
				default:
					end = true;
					if ( errorCallback != 'undefined' && errorCallback != null )
						errorCallback(xhr.status,xhr.statusText);
					else
						Jop.core.debugger("ajax error callback undefined")
					break;
			}
			if ( end )
				xhr.onreadystatechange = function(){};
		} 
		try {
			// open channel
			xhr.open(method,url,async);
			try {
				// set header
				//xhr.setRequestHeader("Content-Type", "text/plain");
				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			} catch (e) {
				//TODO: manage error set header
				throw new Jop.core.exception()
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


