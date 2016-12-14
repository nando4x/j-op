package com.nandox.jop.core.processor;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
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
	/** Identification bean: jop_bean */
//	public static final String JOP_BEAN_INI = "jop_bean={";
	public static final String JOP_BEAN_INI = "{";
	public static final String JOP_BEAN_END = "}";
	public static final String JOP_BEAN_TAG = "jbean";
	
	protected Element domEl;
	protected String id;
	protected List<PageBlock> child;
	protected boolean isChild;
	private String pageId;
	private List<PageExpression> beans;
	private List<BlockAttribute> attrs;
	private PageExpression render;
	private Element clone;
	
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
	public PageBlock(WebAppContext Context, String PageId, Element DomElement) throws DomException {
		this.pageId = PageId;
		this.domEl = DomElement;
		this.id = this.domEl.attr(JOP_ATTR_ID);
		this.beans = new ArrayList<PageExpression>();
		this.attrs = new ArrayList<BlockAttribute>();
		this.parse(Context);
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
		// check render attribute
		if ( this.render != null && !(Boolean)this.render.Execute(Context) ) {
			return "";
		}
		// Fire every own bean and insert into html
		Iterator<PageExpression> bs = this.beans.iterator();
		while ( bs.hasNext() ) {
			PageExpression b = bs.next();
			String v = (String)b.Execute(Context);
			//this.clone.html(this.clone.html().replace(b.getBeanId(), v));
			Element elem = this.clone.select(JOP_BEAN_TAG+"#"+b.getId()).iterator().next();
			//elem = elem.wrap("<div>");
			//elem.html(v);
			//elem.unwrap();
			TextNode txt = new TextNode(v,"");
			elem.replaceWith(txt);
		}
		// Compute attributes
		Iterator<BlockAttribute> la = this.attrs.iterator();
		while ( la.hasNext() ) {
			BlockAttribute ba = la.next();
			String a = this.domEl.attr(ba.name);
			this.clone.attr(ba.name,a.replace("java"+ba.bean.getCode(), (String)ba.bean.Execute(Context)));
		}
		// delete jop_ attribute (exclude jop_id) from dom and then add page id into jop_id
		BlockAttribute.CleanDomFromAttribute(this.clone);
		this.clone.attr(JOP_ATTR_ID,"["+this.pageId+"]."+this.id);
		return this.clone.outerHtml();
	}
	// Parsing Dom Element to search and build beans and attributes
	//
	//
	private void parse(WebAppContext Context) throws DomException {
		// scan for bean tag that is not inside another child block 
		Iterator<Element> elems = this.domEl.select(JOP_BEAN_TAG).iterator();
		HashMap<String,PageExpression> lst = new HashMap<String,PageExpression>();
		while (elems.hasNext() ) {
			Element el = elems.next();
			if ( el.attr(JOP_ATTR_ID).isEmpty() ) {
        		Element p = el.parent(); 
    			while ( p != null ) {
    				if ( !p.attr(PageBlock.JOP_ATTR_ID).isEmpty() ) {
    					if ( p.attr(PageBlock.JOP_ATTR_ID).equals(this.id) ) {
    						// build bean and join the same
    						PageExpression bean;
    						String code = this.parseBean(el);
    						if ( !lst.containsKey(AbstractPageExpression.ComputeId(code)) ) {
    							bean = new SimplePageExpression(Context,code);
    							lst.put(bean.getId(), bean);
    						} else
    							bean = lst.get(AbstractPageExpression.ComputeId(code));
    						this.beans.add(bean);
    						el.attr("id",bean.getId());
    					}
						break;
    				}
    				p = p.parent();
    			}
			} else if ( el.attr(JOP_ATTR_ID).equals(this.id) ) {
				// get own direct bean
				//lst.addAll(this.parseBean(el.ownText()));
			}
		}
		// get attributes
		for ( int ix=0; ix<BlockAttribute.ATTR_LIST.length; ix++ ) {
			String a = this.domEl.attr(BlockAttribute.ATTR_LIST[ix][BlockAttribute.ATTR_NAME]);
			if ( !a.isEmpty() ) {
				if ( a.trim().indexOf("{") >= 0 ) {
					if ( a.indexOf("}") > 0 ) {
						String bid = a.substring(a.indexOf("{"),a.trim().indexOf("}")+1);  
						BlockAttribute at = new BlockAttribute(Context,BlockAttribute.ATTR_LIST[ix][BlockAttribute.ATTR_NAME],bid);
						if ( BlockAttribute.ATTR_LIST[ix][BlockAttribute.ATTR_NAME].equals(BlockAttribute.JOP_RENDERED_ID) )
							this.render = at.bean;
						else
							this.attrs.add(at);
					} else
						throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
				} else if (BlockAttribute.ATTR_LIST[ix][BlockAttribute.ATTR_NAME].toLowerCase().startsWith("jop_") ) {
					throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
				}
			}
		}
	}
	// Parse page bean of the block to verify delimiter { } and than create one set of bean text  
	//
	//
	private String parseBean(Element element) throws DomException {
		// check start and end bean
		int inx_st = element.text().trim().indexOf(JOP_BEAN_INI);
		int inx_end = element.text().trim().indexOf(JOP_BEAN_END);
		if ( inx_st >= 0 && inx_end == element.text().trim().length()-1 ) {
			return element.text().trim().substring(inx_st, inx_end+JOP_BEAN_END.length());
		} else 
			throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
	}
	/*private Set<String> parseBean(String txt) throws DomException {
		Set<String> lst = new HashSet<String>();
		int inx_st = 0;
		// Search every bean
		inx_st = txt.indexOf(JOP_BEAN_INI,inx_st);
		while ( inx_st >= 0 ) {
			// search and check end bean
			int inx_end = txt.indexOf(JOP_BEAN_END,inx_st);
			if ( inx_end > inx_st ) {
				String bean = txt.substring(inx_st, inx_end+JOP_BEAN_END.length());
				lst.add(bean);
			} else 
				throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
			inx_st = txt.indexOf(JOP_BEAN_INI,inx_end);
		}
		// TODO: error if empty
		return lst;
	}*/
}
