package com.nandox.jop.core.processor;

import java.util.Iterator;
import java.util.List;
import org.jsoup.nodes.Element;

/**
 * Descrizione classe
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
	/** Identification bean: jop_bean */
	public static final String JOP_BEAN = "jop_bean";
	
	protected Element domEl;
	protected String id;
	protected List<PageBlock> child;
	protected List<PageBean> beans;
	private Element clone;
	
	/**
	 * Constructor: parse DOM element
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public PageBlock(Element DomElement) {
		this.domEl = DomElement;
		this.clone = DomElement.clone();
		this.id = this.domEl.attr(JOP_ATTR_ID);
		this.parse();
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	//
	//
	//
	private void parse() throws DomException {
		Iterator<Element> elems = this.domEl.getAllElements().iterator();
		while (elems.hasNext() ) {
			Element el = elems.next();
			if ( !el.attr(JOP_ATTR_ID).isEmpty() ) {
        		Element p = el.parent(); 
    			while ( p != null ) {
    				if ( !p.attr(PageBlock.JOP_ATTR_ID).isEmpty() ) {
    					if ( p.attr(PageBlock.JOP_ATTR_ID).equals(this.id) ) {
    						// get bean
    						this.parseBean(el);
    						break;
    					}
    				}
    				p = p.parent();
    			}
			}
		}
	}
	//
	//
	//
	private void parseBean(Element elem) throws DomException {
		String txt = elem.ownText();
		int inx_st = 0;
		inx_st = txt.indexOf(JOP_BEAN+"=",inx_st);
		while ( inx_st >= 0 ) {
			
		}
	}
}
