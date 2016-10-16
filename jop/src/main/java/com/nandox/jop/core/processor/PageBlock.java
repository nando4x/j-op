package com.nandox.jop.core.processor;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import org.jsoup.nodes.Element;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.ErrorsDefine;

/**
 * This is basic part of a page 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    PageBlock.java
 * 
 * @date      17 set 2016 - 17 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class PageBlock {
	/** Identification attribute: jop_id */
	public static final String JOP_ATTR_ID = "jop_id";
	/** Identification attribute: jop_id */
	public static final String JOP_RENDERED_ID = "jop_rendered";
	/** Identification bean: jop_bean */
	public static final String JOP_BEAN = "jop_bean";
	
	protected Element domEl;
	protected String id;
	protected List<PageBlock> child;
	protected boolean isChild;
	private List<PageBean> beans;
	private List<BlockAttribute> attrs;
	private Element clone;
	
	private String[] attr_list = {JOP_RENDERED_ID,"class"};
	/**
	 * Constructor: parse DOM element
	 * @param	  Context	Application context
	 * @param	  DomElement	HTML element of the block
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public PageBlock(WebAppContext Context, Element DomElement) throws DomException {
		this.domEl = DomElement;
		this.id = this.domEl.attr(JOP_ATTR_ID);
		this.beans = new ArrayList<PageBean>();
		this.attrs = new ArrayList<BlockAttribute>();
		Iterator<String> i = this.parse(Context).iterator();
		while ( i.hasNext() )
			this.beans.add(new SimplePageBean(Context,i.next()));
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * Rendering block.<br>
	 * First rendering child in depth and when child is finish or not present for self invoke the  
	 * @param	  Context	Application context
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public String Render(WebAppContext Context) {
		this.clone = this.domEl.clone();
		// rendering all child in recursive mode
		Iterator<PageBlock> cl = this.child.iterator();
		while ( cl.hasNext() ) {
			PageBlock c = cl.next();
			c.Render(Context);
			Element w = this.clone.getElementsByAttributeValue(JOP_ATTR_ID, c.id).first();
			w = w.wrap("<div>");
			w.html(c.clone.outerHtml());
			w.unwrap();
		}
		// Fire every own bean and insert into html
		Iterator<PageBean> bs = this.beans.iterator();
		while ( bs.hasNext() ) {
			PageBean b = bs.next();
			String v = (String)b.Fire(Context);
			this.clone.html(this.clone.html().replace(b.getBeanId(), v));
		}
		return this.clone.outerHtml();
	}
	// Parsing Dom Element to search and build beans and attributes
	//
	//
	private Set<String> parse(WebAppContext Context) throws DomException {
		// scan for bean: first child and them own
		Iterator<Element> elems = this.domEl.getAllElements().iterator();
		Set<String> lst = new HashSet<String>();
		while (elems.hasNext() ) {
			Element el = elems.next();
			if ( el.attr(JOP_ATTR_ID).isEmpty() ) {
        		Element p = el.parent(); 
    			while ( p != null ) {
    				if ( !p.attr(PageBlock.JOP_ATTR_ID).isEmpty() ) {
    					if ( p.attr(PageBlock.JOP_ATTR_ID).equals(this.id) ) {
    						// get bean id to join the same
    						lst.addAll(this.parseBean(el.ownText()));
    						break;
    					}
    				}
    				p = p.parent();
    			}
			} else if ( el.attr(JOP_ATTR_ID).equals(this.id) ) {
				// get own direct bean
				lst.addAll(this.parseBean(el.ownText()));
			}
		}
		// get attributes
		for ( int ix=0; ix<attr_list.length; ix++ ) {
			String a = this.domEl.attr(attr_list[ix]);
			if ( !a.isEmpty() ) {
				if ( a.trim().indexOf("{") >= 0 ) {
					if ( a.indexOf("}") > 0 ) {
						this.attrs.add(new BlockAttribute(Context,attr_list[ix],a));
					} else
						throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
				} else if (attr_list[ix].toLowerCase().startsWith("jop_") ) {
					throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
				}
			}
		}
		return lst;
	}
	// Parse page bean of the block to verify delimiter { } and than create one set of bean text  
	//
	//
	private Set<String> parseBean(String txt) throws DomException {
		Set<String> lst = new HashSet<String>();
		int inx_st = 0;
		// Search every bean
		inx_st = txt.indexOf(JOP_BEAN,inx_st);
		while ( inx_st >= 0 ) {
			// search and check end bean
			int inx_end = txt.indexOf("}",inx_st);
			if ( inx_end > inx_st ) {
				String bean = txt.substring(inx_st, inx_end+1);
				// check syntax
				if ( bean.indexOf(JOP_BEAN) == 0 && bean.indexOf("=") > 0 && bean.indexOf("{") > 0 ) {
					lst.add(bean);
				} else
					throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
			} else 
				throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
			inx_st = txt.indexOf(JOP_BEAN,inx_end);
		}
		return lst;
	}
}
