package com.nandox.jop.core.processor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.nandox.jop.core.ErrorsDefine;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.attribute.JopAttribute;
/**
 * Class of Page application.<br>
 * One page application is composite by PageBlock, every block can contains PageExpression  
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    PageApp.java
 * 
 * @date      17 set 2016 - 17 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class PageApp {
	protected static final String DOMPARSER_JOP_SELECTOR = PageApp.getAttributeSelector();
	private String id;
	private int hash;
	private Document dom;
	private Map<String,PageBlock> blocks;
	private WebAppContext appCtx;
	private int auto_id_index;
	/**
	 * Constructor: parse page content into DOM
	 * @param	  Context	Application context
	 * @param	  PageId	page identificator
	 * @param	  ContentPage	html content page
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception DomException if some syntax error
	 */	
	public PageApp(WebAppContext Context, String PageId, String ContentPage) throws ParseException {
		this.id = PageId;
		this.appCtx = Context;
		this.dom = Jsoup.parse(ContentPage);
		this.blocks = new HashMap<String,PageBlock>();
		this.parse();
		this.hash = ContentPage.hashCode();
	}
	/**
	 * @return the blocks
	 */
	public Map<String, PageBlock> getBlocks() {
		return blocks;
	}
	/**
	 * @return the hash
	 */
	public int getHash() {
		return hash;
	}
	/**
	 * Rendering the page: read every bean value and insert it into dom.<br>
	 * This method render only parent block 
	 * @param	  Context	Application context
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  HTML of page
	 */	
	public String render(WebAppContext Context) {
		Iterator<PageBlock> i = this.blocks.values().iterator();
		Document d = this.dom.clone();
		while ( i.hasNext() ) {
			PageBlock pb = i.next();
			if ( !pb.isChild ) {
				Element e = d.getElementsByAttributeValue(BlockAttribute.JOP_ATTR_ID, pb.id).first();
				e.replaceWith(pb.renderAsNode(Context));
			}
		}
		return d.html();
	}
	/**
	 * Performe action submit: search from own form and if present invoke in write mode with data
	 * @param	  Context	Application context
	 * @param	  Data	Map with name of tag and data
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public void action(WebAppContext Context, Map<String,String[]> Data) {
		// Scan every block
		Iterator<PageBlock> i = this.blocks.values().iterator();
		while (i.hasNext() ) {
			PageBlock b = i.next();
			b.action(Context, Data);
		}
	}
	/**
	 * Return the page block corrispond to id
	 * @param	  BlockId	page block identifier
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public PageBlock getPageBlock(String BlockId) {
		return this.blocks.get(BlockId);
	}
	// Parsing page content to search and build every block 
	//
	//
	private void parse() throws ParseException {
		// Search every jop block into dom and create it
        Iterator<Element> elems = this.dom.select(DOMPARSER_JOP_SELECTOR).iterator();
        this.auto_id_index=0;
    	while ( elems.hasNext() ) {
    		Element el = elems.next();
    		String id = el.attr(BlockAttribute.JOP_ATTR_ID);
    		// generate auto id if empty
    		if ( id.isEmpty() ) {
        		auto_id_index++;
    			id = ""+auto_id_index;
    			el.attr(BlockAttribute.JOP_ATTR_ID,id);
    		}
			// check for double jop id
    		if ( this.blocks.containsKey(id) ) {
    			throw new ParseException(ErrorsDefine.formatDOM(ErrorsDefine.JOP_ID_DOUBLE,el));
    		} else {
    			// create block and check syntax error
    			try {
    				this.blocks.put(id, new PageBlock(this.appCtx,this.id,el));
    			} catch (Exception e) {
    				throw new ParseException(ErrorsDefine.formatDOM(e.getMessage(),el));
    			}
    		}
    	}
		// Scan blocks for own child and attach them
    	PageBlock b[] = this.blocks.values().toArray(new PageBlock[0]);
    	for ( int ix=0; ix<b.length; ix++ ) {
    		ArrayList<PageBlock> child = new ArrayList<PageBlock>();
    		elems = b[ix].domEl.select(DOMPARSER_JOP_SELECTOR).iterator();
    		while ( elems.hasNext() ) {
        		Element el = elems.next();
        		Element p = el.parent(); 
    			while ( p != null ) {
    				if ( !p.attr(BlockAttribute.JOP_ATTR_ID).isEmpty() ) {
    					if ( p.attr(BlockAttribute.JOP_ATTR_ID).equals(b[ix].id) ) {
    						child.add(this.blocks.get(el.attr(BlockAttribute.JOP_ATTR_ID)));
    						this.blocks.get(el.attr(BlockAttribute.JOP_ATTR_ID)).isChild = true;
    					}
    					break;
    				}
    				p = p.parent();
    			}
    		}
    		b[ix].child = child;
    	}
	}
	// Generate css selector with all possible jop attributes
	//
	//
	static private String getAttributeSelector() {
		String sel = "["+JopAttribute.JOP_ATTR_ID+"]";
		String lst[] = JopAttribute.Util.getNameList();
		for ( int ix=0; ix<lst.length; ix++ ) {
			sel += ",["+lst[ix]+"]";
		}
		return sel;
	}
}
