package com.nandox.jop.core.processor;

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
	/** Identification attribute: jop_id*/
	public static final String JOP_ATTR_ID = "jop_id";
	
	protected Element domEl;
	protected String id;
	protected List<PageBlock> child;
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
	private void parse() {
		
	}
}
