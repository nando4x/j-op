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
					xhr.onreadystatechange = function(){};
					var type = xhr.getResponseHeader("Jop-Response-Type");
					successCallback(xhr.responseText,type);
					break;
				default:
					errorCallback(xhr.status,xhr.statusText);
					break;
			}
		} 
		try {
			// open channel
			xhr.open(method,url,async);
			try {
				// set header
				xhr.setRequestHeader("Content-Type", "text/plain");
			} catch (e) { //TODO: manage error set header }
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
			} catch (e) { //TODO: manage error send }
		} catch (e) { //TODO: manage error open	}
	}
	    
	}).apply( Jop.core.services );  


/**
 * Remoting through XHR
 */
xhr:{
  /**
   * The default HTTP method to use
   */
  httpMethod:"POST",

  /**
   * The ActiveX objects to use when we want to do an XMLHttpRequest call.
   * TODO: We arrived at this by trial and error. Other toolkits use
   * different strings, maybe there is an officially correct version?
   */
  XMLHTTP:["Msxml2.XMLHTTP.6.0", "Msxml2.XMLHTTP.5.0", "Msxml2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP", "Microsoft.XMLHTTP"],

  /**
   * Setup a batch for transfer through XHR
   * @param {Object} batch The batch to alter for XHR transmit
   */
  send:function(batch) {
    if (batch.isPoll) {
      batch.map.partialResponse = dwr.engine._partialResponseYes;
    }

    // Do proxies or IE force us to use early closing mode?
    if (batch.isPoll && dwr.engine._pollWithXhr == "true") {
      batch.map.partialResponse = dwr.engine._partialResponseNo;
    }
    if (batch.isPoll && dwr.engine.isIE) {
      batch.map.partialResponse = dwr.engine._partialResponseNo;
    }

    if (window.XMLHttpRequest) {
      batch.req = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
      batch.req = dwr.engine.util.newActiveXObject(dwr.engine.transport.xhr.XMLHTTP);
    }

    // Proceed using XMLHttpRequest
    if (batch.async == true) {
      batch.req.onreadystatechange = function() {
        if (typeof dwr != 'undefined') {
          dwr.engine.transport.xhr.stateChange(batch);
        }
      };
    }

    httpMethod = dwr.engine.transport.xhr.httpMethod;

    // Workaround for Safari 1.x POST bug
    var indexSafari = navigator.userAgent.indexOf("Safari/");
    if (indexSafari >= 0) {
      var version = navigator.userAgent.substring(indexSafari + 7);
      if (parseInt(version, 10) < 400) {
        if (dwr.engine._allowGetForSafariButMakeForgeryEasier == "true") {
          httpMethod = "GET";
        }
        else {
          dwr.engine._handleWarning(batch, {
            name: "dwr.engine.oldSafari",
            message: "Safari GET support disabled. See getahead.org/dwr/server/servlet and allowGetForSafariButMakeForgeryEasier."
          });
        }
      }
    }

    batch.mode = batch.isPoll ? dwr.engine._ModePlainPoll : dwr.engine._ModePlainCall;
    var request = dwr.engine.batch.constructRequest(batch, httpMethod);

    try {
      batch.req.open(httpMethod, request.url, batch.async);
      try {
        for (var prop in batch.headers) {
          var value = batch.headers[prop];
          if (typeof value == "string") {
            batch.req.setRequestHeader(prop, value);
          }
        }
        if (!batch.headers["Content-Type"]) {
          batch.req.setRequestHeader("Content-Type", "text/plain");
        }
      }
      catch (ex) {
        dwr.engine._handleWarning(batch, ex);
      }

      batch.req.send(request.body);
      if (batch.async == false) {
        dwr.engine.transport.xhr.stateChange(batch);
      }
    }
    catch (ex) {
      dwr.engine._handleError(batch, ex);
    }

    if (batch.isPoll && batch.map.partialResponse == dwr.engine._partialResponseYes) {
      dwr.engine.transport.xhr.checkCometPoll();
    }

    // This is only of any use in sync mode to return the reply data
    return batch.reply;
  },

  /**
   * Called by XMLHttpRequest to indicate that something has happened
   * @private
   * @param {Object} batch The current remote operation
   */
  stateChange:function(batch) {
    var toEval;

    if (batch.completed) {
      dwr.engine._debug("Error: _stateChange() with batch.completed");
      return;
    }

    // Try to get the response HTTP status if applicable
    var req = batch.req;
    var status = 0;
    try {
      if (req.readyState >= 2) {
        status = req.status; // causes Mozilla to except on page moves
      }
    }
    catch(ignore) {}

    // If we couldn't get the status we bail out, unless the request is
    // complete, which means error (handled further below)
    if (status == 0 && req.readyState < 4) {
      return;
    }

    // If the status is 200, we are now online. 
    // Future improvement per Mike W. - A solution where we only use the callbacks/handlers of the poll call to trigger 
    // the retry handling would be ideal.  We would need something like a new internal callback that reports 
    // progress back to the caller, and the design should be compatible with getting it to work with iframes as well.   
    if (status == 200 && !dwr.engine._pollOnline) {
      dwr.engine._handlePollStatusChange(true);    
    }  

    // The rest of this function only deals with request completion
    if (req.readyState != 4) {
      return;
    }

    if (dwr.engine._unloading && !dwr.engine.isJaxerServer) {
      dwr.engine._debug("Ignoring reply from server as page is unloading.");
      return;
    }

    try {
      var reply = req.responseText;
      reply = dwr.engine._replyRewriteHandler(reply);

      if (status != 200) {
        dwr.engine._handleError(batch, { name:"dwr.engine.http." + status, message:req.statusText });
      }
      else if (reply == null || reply == "") {
        dwr.engine._handleError(batch, { name:"dwr.engine.missingData", message:"No data received from server" });
      }
      else {                     
        var contentType = req.getResponseHeader("Content-Type");
        if (dwr.engine.isJaxerServer) {
          // HACK! Jaxer does something b0rken with Content-Type
          contentType = "text/javascript";
        }
        if (!contentType.match(/^text\/plain/) && !contentType.match(/^text\/javascript/)) {
          if (contentType.match(/^text\/html/) && typeof batch.textHtmlHandler == "function") {
            batch.textHtmlHandler({ status:status, responseText:reply, contentType:contentType });
          }
          else {
            dwr.engine._handleWarning(batch, { name:"dwr.engine.invalidMimeType", message:"Invalid content type: '" + contentType + "'" });
          }
        }
        else {
         // Comet replies might have already partially executed
         if (batch.isPoll && batch.map.partialResponse == dwr.engine._partialResponseYes) {
            dwr.engine.transport.xhr.processCometResponse(reply, batch);
          }
          else {
            if (reply.search("//#DWR") == -1) {
              dwr.engine._handleWarning(batch, { name:"dwr.engine.invalidReply", message:"Invalid reply from server" });
            }
            else {
              toEval = reply;
            }
          }
        }
      }
    }
    catch (ex) {
      dwr.engine._handleWarning(batch, ex);
    }

    // Outside of the try/catch so errors propagate normally:
    dwr.engine._receivedBatch = batch;
    if (toEval != null) toEval = toEval.replace(dwr.engine._scriptTagProtection, "");
    dwr.engine._eval(toEval);
    dwr.engine._receivedBatch = null;
    dwr.engine.transport.complete(batch);
  },

  /**
   * Check for reverse Ajax activity
   * @private
   */       
  checkCometPoll:function() {
    var req = dwr.engine._pollBatch && dwr.engine._pollBatch.req;
    if (req) {         
      var text = req.responseText;
      if (text != null) {
        dwr.engine.transport.xhr.processCometResponse(text, dwr.engine._pollBatch);
      }          
    }      
    // If the poll resources are still there, come back again
    if (dwr.engine._pollBatch) {
      setTimeout(dwr.engine.transport.xhr.checkCometPoll, dwr.engine._pollCometInterval);
    }
  },

  /**
   * Some more text might have come in, test and execute the new stuff.
   * This method could also be called by the iframe transport
   * @private
   * @param {Object} response from xhr.responseText
   * @param {Object} batch The batch that the XHR object pertains to
   */
  processCometResponse:function(response, batch) {
    if (batch.charsProcessed == response.length) return;
    if (response.length == 0) {
      batch.charsProcessed = 0;
      return;
    }

    var firstStartTag = response.indexOf("//#DWR-START#", batch.charsProcessed);
    if (firstStartTag == -1) {
      // dwr.engine._debug("No start tag (search from " + batch.charsProcessed + "). skipping '" + response.substring(batch.charsProcessed) + "'");
      batch.charsProcessed = response.length;
      return;
    }
    // if (firstStartTag > 0) {
    //   dwr.engine._debug("Start tag not at start (search from " + batch.charsProcessed + "). skipping '" + response.substring(batch.charsProcessed, firstStartTag) + "'");
    // }

    var lastEndTag = response.lastIndexOf("//#DWR-END#");
    if (lastEndTag == -1) {
      // dwr.engine._debug("No end tag. unchanged charsProcessed=" + batch.charsProcessed);
      return;
    }

    // Skip the end tag too for next time, remembering CR and LF
    if (response.charCodeAt(lastEndTag + 11) == 13 && response.charCodeAt(lastEndTag + 12) == 10) {
     batch.charsProcessed = lastEndTag + 13;
    }
    else {
      batch.charsProcessed = lastEndTag + 11;
    }

    var exec = response.substring(firstStartTag + 13, lastEndTag);

    try {
      dwr.engine._receivedBatch = batch;
      dwr.engine._eval(exec);
      dwr.engine._receivedBatch = null;
    }
    catch (ex) {
      // This is one of these annoying points where we might be executing
      // while the window is being destroyed. If dwr == null, bail out.
      if (dwr != null) {
        dwr.engine._handleError(batch, ex);
      }
    }
  },

  /**
   * Aborts ongoing request (for timeouts etc)
   */
  abort:function(batch) {
    if (batch.req) {
      batch.req.abort();
    }
  },
  
  /**
   * Tidy-up when an XHR call is done
   * @param {Object} batch
   */
  remove:function(batch) {
    // XHR tidyup: avoid IE handles increase
    if (batch.req) {
      delete batch.req;
    }
  }
}

