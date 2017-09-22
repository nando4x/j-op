package com.nandox.jop.core.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nandox.jop.core.ErrorsDefine;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.logging.Logger;
import com.nandox.jop.core.processor.attribute.JopAttribute;
/**
 * Class of Page application.<p>
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
	/** DOM JOP block selector */
	protected static final String DOMPARSER_JOP_SELECTOR = PageApp.getAttributeSelector();
	/** Logger */
	protected static final Logger LOG = Logger.Factory.getLogger(PageApp.class);
	
	private static final String DOMPARSER_HEAD_TAG = "script[jop_head=\"true\"]";
	private String id;	// page identifier
	private int hash;	// page hash code
	private Document dom;	// html DOM document
	private Map<String,PageBlock> blocks;	// list of all page blocks
	private WebAppContext appCtx;	// application web context
	private int auto_id_index;	// auto incremental index of anonymous block
	
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
		Document.OutputSettings settings = this.dom.outputSettings();
		settings.prettyPrint(false);
		settings.escapeMode(Entities.EscapeMode.extended);
		settings.charset("ASCII");
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
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("start rendering page: %s", this.id);
		Iterator<PageBlock> i = this.blocks.values().iterator();
		Document d = this.dom.clone();
		while ( i.hasNext() ) {
			PageBlock pb = i.next();
			if ( !pb.isChild ) {
				Element e = d.getElementsByAttributeValue(JopAttribute.JOP_ATTR_ID, pb.id).first();
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
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("parsing page: %s", this.id);
		// Search jop head and substitute with script file include
		Elements list = this.dom.select(DOMPARSER_HEAD_TAG);
		for ( int ix=0; ix<list.size(); ix++ ) {
			Element el = list.get(ix);
			this.buildHeadScript(el);
		}
		// Search every jop block into dom and create it
		list = this.dom.select(DOMPARSER_JOP_SELECTOR);
		// generate auto id if empty
        this.auto_id_index=0;
		for ( int ix=0; ix<list.size(); ix++ ) {
			Element el = list.get(ix);
    		String id = el.attr(JopAttribute.JOP_ATTR_ID);
    		if ( id.isEmpty() ) {
        		auto_id_index++;
    			id = ""+auto_id_index;
    			el.attr(JopAttribute.JOP_ATTR_ID,id);
    		}
		}
		// create blocks
        Iterator<Element> elems = list.iterator();
    	while ( elems.hasNext() ) {
    		Element el = elems.next();
			// check for double jop id
    		String id = el.attr(JopAttribute.JOP_ATTR_ID);
    		if ( this.blocks.containsKey(id) ) {
    			if (LOG != null && LOG.isErrorEnabled() ) LOG.error("double block %s on page %s",id, this.id);
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
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("build child block chain on page: %s",this.id);
    	PageBlock b[] = this.blocks.values().toArray(new PageBlock[0]);
    	for ( int ix=0; ix<b.length; ix++ ) {
    		ArrayList<PageBlock> child = new ArrayList<PageBlock>();
    		elems = b[ix].domEl.select(DOMPARSER_JOP_SELECTOR).iterator();
    		while ( elems.hasNext() ) {
        		Element el = elems.next();
        		Element p = el.parent(); 
    			while ( p != null ) {
    				if ( !p.attr(JopAttribute.JOP_ATTR_ID).isEmpty() ) {
    					if ( p.attr(JopAttribute.JOP_ATTR_ID).equals(b[ix].id) ) {
    						child.add(this.blocks.get(el.attr(JopAttribute.JOP_ATTR_ID)));
    						this.blocks.get(el.attr(JopAttribute.JOP_ATTR_ID)).isChild = true;
    					}
    					break;
    				}
    				p = p.parent();
    			}
    		}
    		b[ix].child = child;
    	}
	}
	// Build head scripts inclusion
	// 
	//
	private void buildHeadScript (Element el) {
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("build head scripts for page: %s", this.id);
		String pth = WebAppContext.getCurrentRequestContext().getHttpRequest().getContextPath();
		el.before("<link type=\"text/css\" rel=\"stylesheet\" href=\""+pth+"/jopscript/base.css\"/>");
		el.before("<script type=\"text/javascript\" src=\""+pth+"/jopscript/baselibs.js\"/>");
		el.before("<script type=\"text/javascript\" src=\""+pth+"/jopscript/core/services.js\"/>");
		el.remove();
	}
	// Generate css selector with all possible jop attributes
	//
	//
	static private String getAttributeSelector() {
		String sel = "["+JopAttribute.JOP_ATTR_ID+"]";
		String lst[] = JopAttribute.Factory.getNameList();
		for ( int ix=0; ix<lst.length; ix++ ) {
			sel += ",["+lst[ix]+"]";
		}
		return sel;
	}
}
