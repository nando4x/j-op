function ModuleInit_Dom(libroot) { 
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
			this.toggleClass = function(element,cls) {
				if ( element != null && element.classList )
					element.classList.toggle(cls);
			}
			this.style = function(element, name, value) {
				if ( element.style && typeof element.style[name] !== 'undefined' ) {
					element.style[name] = value;
				}
			}
		}).apply( Dom );
		libroot.Dom = Dom;
};