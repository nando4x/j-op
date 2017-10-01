package com.nandox.jop.widget;

import java.io.InputStream;
import java.util.Iterator;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.DomException;
import com.nandox.jop.core.processor.PageBlock;

/**
 * Widget page block.<p>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    WidgetBlock.java
 * 
 * @date      17 set 2016 - 17 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class WidgetBlock extends PageBlock {
	/** */
	protected static final String ATTR_TYPE = "type";
	protected static final String ATTR_TYPE_ALT = "jop_wdg";
	protected static final String BASE_TMPL_PATH = "templates/";
	
	/**
	 * Constructor: parse DOM element
	 * @param	  Context	Application context
	 * @param	  PageId	page identificator
	 * @param	  DomElement	HTML element of the block
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public WidgetBlock(WebAppContext Context, String PageId, Element DomElement) throws DomException {
		super(Context, PageId, DomElement);
		this.buildFromTemplate(BASE_TMPL_PATH);
		this.parse(Context);
	}

	/**
	 * Build widget from a template.<p>
	 * Read the template html file compile it and place on DOM of this block
	 * @param	  TmplPath relative path of template file
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  
	 */	
	protected void buildFromTemplate(String TmplPath) {
		// Read template into new element
		String type = WidgetBlock.computeWidgetName(this.domEl);
		
		InputStream i = this.getClass().getResourceAsStream(TmplPath+type+".tmpl");
		StringBuffer jb = new StringBuffer();
		int len;
		try {
			while ((len = i.available()) > 0 ) {
				byte buff[] = new byte[len]; 
				i.read(buff);
				jb.append(new String(buff));
			}
			Element tmpl = new Element(Tag.valueOf("div"),"");
			tmpl.html(jb.toString());
			// compile template widget tag (wdg_*)
			this.compileTemplate(tmpl);
			// add or merge html attributes (exclude type and jop_*)
			Iterator<Attribute> attrs = this.domEl.attributes().iterator();
			// replace this element with template and assign the id
			Iterator<Element> elems = tmpl.select("[jop_id]").iterator();
			if ( elems.hasNext() )
				elems.next().attr("jop_id",this.id);
			this.domEl.html(tmpl.html());
			this.domEl = (Element)this.domEl.unwrap().parent();
			this.domEl = this.domEl.select("[jop_id="+this.id+"]").iterator().next();
			
			// add or merge html attributes (exclude type and jop_*)
			this.compileAttributes(attrs);
		} catch (Exception e) {
			//TODO: exception unknown widget type
			e = null;
		}
	}
	/**
	 * Compile template.<p>
	 * Read template's widget tag and compile it with same tag of block DOM
	 * @param	  Tmpl template DOM element
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  
	 */	
	protected void compileTemplate(Element Tmpl) {
		Iterator<Element> elems = Tmpl.getAllElements().iterator();
		while (elems.hasNext() ) {
			Element e = elems.next();
			// if found replace template widget with same tag of the element 
			if ( e.tagName().toLowerCase().startsWith("wdg_") ) {
				if ( !this.domEl.getElementsByTag(e.tagName()).isEmpty() ) {
					e.html(this.domEl.getElementsByTag(e.tagName()).get(0).html());
				}
				e.unwrap();
			}
		}
	}
	/**
	 * Compile attributes.<p>
	 * Merge attributes (excluding jop_*) between DOM and template 
	 * @param	  Attrs attributes to change with relative new value
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  
	 */	
	protected void compileAttributes(Iterator<Attribute> Attrs) {
		while ( Attrs.hasNext() ) {
			Attribute attr = Attrs.next();
			if ( !attr.getKey().equalsIgnoreCase(ATTR_TYPE) && !attr.getKey().equalsIgnoreCase(ATTR_TYPE_ALT) && !attr.getKey().toLowerCase().startsWith("jop_") ) {
				if ( this.domEl.hasAttr(attr.getKey()) )
					this.domEl.attr(attr.getKey(), this.domEl.attr(attr.getKey()) + " " +attr.getValue());
				else
					this.domEl.attr(attr.getKey(), attr.getValue());
			}
		}
	}
	
	/**
	 * Compute widget name from DOM attributes.<p>
	 * Use attributes type of block jwdg 
	 * @param	  Dom widget element
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  
	 */	
	public static String computeWidgetName(Element Dom) {
		String type = Dom.attr(ATTR_TYPE);
		if( type == null || type.isEmpty() ) {
			type = Dom.attr(ATTR_TYPE_ALT);
		}
		return type;
	}
}
