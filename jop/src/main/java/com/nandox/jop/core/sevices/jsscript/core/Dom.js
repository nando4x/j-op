function libNxModule_Dom(libroot) { 
	var Dom = {};
	(function () {
		this.querySelector = function(element,query) {
			if ( element != null )
				return element.querySelector(query);
			return null;
		}
		this.querySelectorAll = function(element,query) {
			if ( element != null )
				return element.querySelectorAll(query);
			return null;
		}
		this.removeElement = function(element) {
			element.parentElement.removeChild(element);
		}
		this.addClass =  function(element,Class) {
			element.className = element.className.split(Class).join("") + " " + Class;
		}
		this.removeClass =  function(element,Class) {
			element.className = element.className.split(Class).join("");
		}
		this.toggleClass = function(element,Class) {
			if ( element != null && element.classList )
				element.classList.toggle(Class);
		}
		this.style = function(element, name, value) {
			if ( element.style && typeof element.style[name] !== 'undefined' ) {
				element.style[name] = value;
			}
		}
	}).apply( Dom );
	libroot.Dom = Dom;
};

if ( typeof libNxModuleInit !== 'function' ) {
	var libNxModuleInit = function(libroot) {
		var lst = Object.keys(window);
		var k = lst.filter(function(key){ return key.indexOf("libNxModule_") > -1;});
		for ( item in k )
			window[k[item]](libroot);
	}
}