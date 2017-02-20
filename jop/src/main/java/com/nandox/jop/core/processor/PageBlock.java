package com.nandox.jop.core.processor;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.context.BeanMonitoring;
import com.nandox.jop.core.processor.attribute.JopAttribute;
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
public class PageBlock implements RefreshableBlock {
	/** Identification bean: jop_bean */
//	public static final String JOP_BEAN_INI = "jop_bean={";
	public static final String JOP_EXPR_INI = "{";
	public static final String JOP_EXPR_END = "}";
	public static final String JOP_BEAN_TAG = "jbean";
	/** block DOM */
	protected Element domEl;
	/** block identifier */
	protected String id;
	/** child blocks */
	protected List<PageBlock> child;
	/** true if block is child of another block */
	protected boolean isChild;
	
	private String pageId;
	private PageExpression render;
	private static final String tmp_attr_id = "_jop_tmp_id";
	private static final String form_selector = "[value^=java{]";
	private int auto_id_index;
	private boolean toBeRefresh;
	protected Element clone;
	
	private Map<String,PageExpression> exprs;
	private Map<String,PageExpression> beans;
	private Map<String,PageWriteExpression> forms;

	private class AttributeExpr {
		String name;
		PageExpression expr;
	}
	
	private Map<String,AttributeExpr> html_attrs;
	private List<JopAttribute> attrs;
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
		this.id = this.domEl.attr(BlockAttribute.JOP_ATTR_ID);
		
		this.exprs = new HashMap<String,PageExpression>();
		this.beans = new HashMap<String,PageExpression>();
		this.forms = new HashMap<String,PageWriteExpression>();
		this.html_attrs = new HashMap<String,AttributeExpr>();
		this.attrs = new ArrayList<JopAttribute>();
		
		this.parse(Context);
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * Rendering block and return string.<br>
	 * @param	  Context	Application context
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  html in string format
	 */	
	public String render(WebAppContext Context) {
		return this.renderAsNode(Context).outerHtml();
	}
	/**
	 * Rendering block.<br>
	 * First rendering child in depth and when child is finish or not present for self invoke the  
	 * @param	  Context	Application context
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  
	 */	
	protected Node renderAsNode(WebAppContext Context) {
		this.clone = this.domEl.clone();
		// ### Rendering all child in recursive mode
		Iterator<PageBlock> cl = this.child.iterator();
		while ( cl.hasNext() ) {
			PageBlock c = cl.next();
			Element e = this.clone.getElementsByAttributeValue(BlockAttribute.JOP_ATTR_ID, c.id).first();
			e.replaceWith(c.renderAsNode(Context));
		}
		// check render attribute
		if ( this.render != null && !(Boolean)this.render.execute(Context) ) {
			return new TextNode("","");
		}
		// ### Fire every own bean and insert into html
		Iterator<Entry<String,PageExpression>> beans = this.beans.entrySet().iterator();
		while (beans.hasNext()) {
			Entry<String,PageExpression> e = beans.next();
			PageExpression b = e.getValue();
			String v = (String)b.execute(Context);
			Iterator<Element> elem = this.clone.select(JOP_BEAN_TAG+"#"+e.getKey()).iterator();
			while (elem.hasNext()) {
				TextNode txt = new TextNode(v,"");
				elem.next().replaceWith(txt);
			}
		}
		// ### Compute all html attributes
		Iterator<Entry<String,AttributeExpr>> attrs = this.html_attrs.entrySet().iterator();
		while (attrs.hasNext()) {
			Entry<String,AttributeExpr> en = attrs.next();
			AttributeExpr b = en.getValue();
			Element e = this.clone.getElementsByAttributeValue(tmp_attr_id, en.getKey()).first();
			String a = e.attr(b.name);
			e.attr(b.name,a.replace("java"+b.expr.getCode(), (String)b.expr.execute(Context)));
			e.removeAttr(tmp_attr_id);
		}
		// ### Compute forms 
		Iterator<Entry<String,PageWriteExpression>> f = this.forms.entrySet().iterator();
		while ( f.hasNext() ) {
			Entry<String,PageWriteExpression> b = f.next();
			String v = (String)b.getValue().execute(Context);
			Element e = this.clone.getElementsByAttributeValue("name", b.getKey()).first();
			String a = e.attr("value");
			e.attr("value",a.replace("java"+b.getValue().getCode(), v));
			// add page id to name attribute
			e.attr("name","["+this.pageId+"]."+e.attr("name"));
		}
		
		// delete jop_ attribute (exclude jop_id) from dom and then add page id into jop_id
		BlockAttribute.cleanDomFromAttribute(this.clone);
		this.clone.attr(BlockAttribute.JOP_ATTR_ID,"["+this.pageId+"]."+this.id);
		return this.clone;
	}
	/**
	 * Perform action submit
	 * @param	  Context	Application context
	 * @param	  Data	Map with name of tag and data
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public void action(WebAppContext Context, Map<String,String[]> Data) {
		// Search form tag with key (name) of the Data
		Iterator<Entry<String,PageWriteExpression>> i = this.forms.entrySet().iterator();
		while ( i.hasNext() ) {
			Entry<String,PageWriteExpression> pe = i.next();
			if ( Data.containsKey(pe.getKey()) ) {
				// Invoke expression in write mode
				String val = Data.get(pe.getKey())[0]; // get string data
				pe.getValue().execute(Context, val);
			}
		}
		// Reset all value expression
		this.resetAllExprValue();
		this.toBeRefresh = true;
	}
	
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.RefreshableBlock#SetToBeRefreshed()
	 */
	public void setToBeRefreshed() {
		// Reset all value expression
		this.resetAllExprValue();
		this.toBeRefresh = true;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.RefreshableBlock#ResetToBeRefreshed()
	 */
	public void resetToBeRefreshed() {
		this.toBeRefresh = false;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.RefreshableBlock#GetToBeRefresh()
	 */
	public boolean getToBeRefresh() {
		return this.toBeRefresh;
	}
	// Parsing Dom Element to search and build beans and attributes
	//
	//
	private void parse(WebAppContext Context) throws DomException {
		BeanMonitoring mon = Context.getBeanMonitor(); // get bean monitor
		// Scan for element that is not inside another child block 
		Iterator<Element> elems = this.domEl.select(JOP_BEAN_TAG).iterator();
		elems = this.domEl.getAllElements().iterator();
		while (elems.hasNext() ) {
			Element el = elems.next();
			if ( this.checkIfParentBlockIsThis(el)) {
				// check if bean or attributes element
				if ( el.tag().getName().equalsIgnoreCase(JOP_BEAN_TAG) ) {
					// build expression and join the same
					PageExpression exp;
					String code = this.parseBean(el);
					if ( !this.exprs.containsKey(AbstractPageExpression.computeId(code)) ) {
						// for a new expression add it and register this block to those to refresh 
						exp = new SimplePageExpression(Context,code);
						this.exprs.put(exp.getId(), exp);
						mon.registerRefreshable(exp.getBeansList(), this);
					} else
						exp = this.exprs.get(AbstractPageExpression.computeId(code));
					this.beans.put(exp.getId(),exp);
					el.attr("id",exp.getId());
				} else {
					// Get and process attributes of children block
					this.parseAttributes(Context, el,mon);
				}
			}
		}
		// Get and process attributes of this block
		this.parseAttributes(Context, this.domEl,mon);
		
		// Get and process value attribute of form tags (es. input)
		Elements grp = this.domEl.select(form_selector);
		grp.add(this.domEl); // add self to the list
		elems = grp.iterator();
		while ( elems.hasNext() ) {
			Element el = elems.next();
			if ( el.tag().isFormSubmittable() ) {
				if ( this.checkIfParentBlockIsThis(el)) {
					String a = el.attr("value");
					String code = this.parseJavaExpression(a); 
					if ( code != null ) {
						// create new expression and resister this block to those to refresh 
						PageWriteExpression exp;

						if ( !this.exprs.containsKey(AbstractPageExpression.computeId(code)) ) {
							// for a new expression add it and register this block to those to refresh 
							exp = new SimplePageExpression(Context,code);
							this.exprs.put(exp.getId(), exp);
							mon.registerRefreshable(exp.getBeansList(), this);
						} else
							exp = (PageWriteExpression)this.exprs.get(AbstractPageExpression.computeId(code));
						// if name attributes don't exist add it with auto index
						if ( !el.hasAttr("name") || el.attr("name").isEmpty() ) {
							el.attr("name",""+this.auto_id_index);
							auto_id_index++;
						}
						this.forms.put(el.attr("name"), exp);
					}
				}
			}
		}
	}
	// Parse page bean of the block to verify delimiter { } and than create one set of bean text  
	//
	//
	private String parseBean(Element element) throws DomException {
		// parse as java expression
		return this.parseJavaExpression("java"+element.text().trim());
	}
	// Parse attributes element to verify delimiter { }
	//
	//
	private void parseAttributes(WebAppContext context, Element el, BeanMonitoring mon) throws DomException {
		// scan all element's attributes excluding value and jop_id
		Iterator<Attribute> attrs = el.attributes().iterator();
		while (attrs.hasNext() ) {
			Attribute attr =  attrs.next();
			if ( !attr.getKey().equalsIgnoreCase(BlockAttribute.JOP_ATTR_ID) && !attr.getKey().equalsIgnoreCase("value") ) {
				String a = attr.getValue();
				String bid = this.parseJavaExpression(a); 
				if ( bid != null ) {
					if ( attr.getKey().toLowerCase().startsWith("jop_") ) {
						JopAttribute ja = JopAttribute.Util.create(context,attr.getKey(),bid);
						this.attrs.add(ja);
						mon.registerRefreshable(ja.getExpression().getBeansList(), this);
					}
					BlockAttribute at;
					if ( attr.getKey().equalsIgnoreCase(BlockAttribute.JOP_ATTR_RENDERED) ) {
						at = new BlockAttribute(context,attr.getKey(),bid);
						this.render = at.expr;
						mon.registerRefreshable(at.expr.getBeansList(), this);
					} else {
						// html attribute: parse expression and verify if exist
						PageExpression exp;
						String code = this.parseJavaExpression(a); 
						if ( !this.exprs.containsKey(AbstractPageExpression.computeId(code)) ) {
							// for a new expression add it and register this block to those to refresh 
							exp = new SimplePageExpression(context,code);
							this.exprs.put(exp.getId(), exp);
							mon.registerRefreshable(exp.getBeansList(), this);
						} else
							exp = this.exprs.get(AbstractPageExpression.computeId(code));
						// add attribute to map and identify element with temp id
						AttributeExpr ae = new AttributeExpr();
						ae.expr = exp;
						ae.name = attr.getKey();
						String id = ""+this.auto_id_index++;
						this.html_attrs.put(id, ae);
						el.attr(tmp_attr_id,id);
					}
				}
			}
		}
	}
	// Parse expression check syntax and extract string of expression
	//
	//
	private String parseJavaExpression (String code) throws DomException {
		if ( !code.isEmpty() ) {
			if ( code.trim().indexOf("java"+JOP_EXPR_INI) >= 0 ) {
				if ( code.lastIndexOf(JOP_EXPR_END) > 0 ) {
					String bid = code.substring(code.indexOf(JOP_EXPR_INI),code.trim().lastIndexOf(JOP_EXPR_END)+1);
					return bid;
				} else
					throw new DomException(ErrorsDefine.JOP_EXPR_SYNTAX);
			}
		}
		return null;
	}
	// Check if parent job_id is of this block
	//
	//
	private boolean checkIfParentBlockIsThis ( Element el ) {
		Element p = el.parent(); 
		while ( p != null ) {
			// check if tag is in another child block 
			if ( !p.attr(BlockAttribute.JOP_ATTR_ID).isEmpty() ) {
				if ( p.attr(BlockAttribute.JOP_ATTR_ID).equals(this.id) )
					return true;
				else
					return false;
			}
			p = p.parent();
		}
		return false;
	}
	// Reset all expression value
	//
	//
	private void resetAllExprValue() {
		Iterator<PageExpression> exp = this.exprs.values().iterator();
		while ( exp.hasNext() ) {
			exp.next().resetValue();
		}
	}
}
