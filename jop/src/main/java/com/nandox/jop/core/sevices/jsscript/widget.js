function libNxModule_Widget(libroot) { 
	var Widget = {};
	(function () {
		this.accordion = {
			toggle: function(element,isOpen) {
				var block = libroot.Dom.parentSelector(element,'.wdg_wrapper');
				libroot.Services.postBlock(block.getAttribute('jop_id'),{open_flag: isOpen});
			}
		},
		this.combobox = {
			toggle: function(element,isOpen) {
				var block = libroot.Dom.parentSelector(element,'.wdg_wrapper');
				libroot.Services.postBlock(block.getAttribute('jop_id'),{open_flag: isOpen}).done(function(jopId){
					var block = libroot.getBlockElement(jopId);
					var li = libroot.Dom.querySelectorAll(block,"li");
					var c = libroot.Dom.querySelector(block,".wdg_list");
					if ( c != null ) {
						c.style.minWidth = block.offsetWidth-2;
					}
					for ( var ix=0; ix<li.length; ix++ ) {
						li[ix].onmouseover = function(e) {
							libroot.Dom.addClass(e.target,"st_active");
						}
						li[ix].onmouseout = function(e) {
							libroot.Dom.removeClass(e.target,"st_active");
						}
						li[ix].onclick = function(e) {
							e.stopPropagation();
							var block = libroot.Dom.parentSelector(e.target,'.wdg_wrapper');
							var i = libroot.Dom.querySelector(block,".wdg_input");
							var val = e.target.getAttribute('data-value');
							if ( val != null )
								i.value = val;
							else
								i.value = e.target.textContent;
							Widget.combobox.toggle(block,false);
						}
					}
				});
			}
		}
	}).apply( Widget );
	libroot.Widget = Widget;
};
