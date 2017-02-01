package com.nandox.jop.core.processor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.context.BeanMonitoring;
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
	private List<PageExpression> beans;
	private List<PageWriteExpression> forms;
	private List<BlockAttribute> attrs;
	private List<BlockAttribute> attrs_child;
	private PageExpression render;
	private static final String tmp_attr_id = "_jop_tmp_id";
	private static final String form_selector = "[value^=java{]";
	private int auto_id_index;
	protected Element clone;
	
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
		this.beans = new ArrayList<PageExpression>();
		this.forms = new ArrayList<PageWriteExpression>();
		this.attrs = new ArrayList<BlockAttribute>();
		this.attrs_child = new ArrayList<BlockAttribute>();
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
	public Node Render(WebAppContext Context) {
		this.clone = this.domEl.clone();
		// ### Rendering all child in recursive mode
		Iterator<PageBlock> cl = this.child.iterator();
		while ( cl.hasNext() ) {
			PageBlock c = cl.next();
			Element e = this.clone.getElementsByAttributeValue(BlockAttribute.JOP_ATTR_ID, c.id).first();
			e.replaceWith(c.Render(Context));
		}
		// check render attribute
		if ( this.render != null && !(Boolean)this.render.Execute(Context) ) {
			return new TextNode("","");
		}
		// ### Fire every own bean and insert into html
		Iterator<PageExpression> bs = this.beans.iterator();
		while ( bs.hasNext() ) {
			PageExpression b = bs.next();
			String v = (String)b.Execute(Context);
			//this.clone.html(this.clone.html().replace(b.getBeanId(), v));
			Element elem = this.clone.select(JOP_BEAN_TAG+"#"+b.getId()).iterator().next();
			TextNode txt = new TextNode(v,"");
			elem.replaceWith(txt);
		}
		// ### Compute own attributes
		Iterator<BlockAttribute> la = this.attrs.iterator();
		while ( la.hasNext() ) {
			BlockAttribute ba = la.next();
			String a = this.domEl.attr(ba.name);
			this.clone.attr(ba.name,a.replace("java"+ba.expr.getCode(), (String)ba.expr.Execute(Context)));
		}
		// ### Compute attributes of children
		la = this.attrs_child.iterator();
		while ( la.hasNext() ) {
			BlockAttribute ba = la.next();
			Element e = this.clone.getElementsByAttributeValue(tmp_attr_id, ba.elem_tmp_id).first();
			String a = e.attr(ba.name);
			e.attr(ba.name,a.replace("java"+ba.expr.getCode(), (String)ba.expr.Execute(Context)));
			e.removeAttr(tmp_attr_id);
		}
		// ### Compute forms 
		Iterator<PageWriteExpression> f = this.forms.iterator();
		while ( f.hasNext() ) {
			PageWriteExpression b = f.next();
			String v = (String)b.Execute(Context);
			Element e = this.clone.getElementsByAttributeValue("name", b.getId()).first();
			String a = e.attr("value");
			e.attr("value",a.replace("java"+b.getCode(), v));
			// add page id to name attribute
			e.attr("name","["+this.pageId+"]."+e.attr("name"));
		}
		
		
		// delete jop_ attribute (exclude jop_id) from dom and then add page id into jop_id
		BlockAttribute.CleanDomFromAttribute(this.clone);
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
	public void Action(WebAppContext Context, Map<String,String[]> Data) {
		// Search form tag with key (name) of the Data
		Iterator<PageWriteExpression> i = this.forms.iterator();
		while ( i.hasNext() ) {
			PageWriteExpression pe;
			if ( Data.containsKey((pe=i.next()).getId()) ) {
				// Invoke expression in write mode
				String val = Data.get(pe.getId())[0]; // get string data
				pe.Execute(Context, val);
			}
		}
		// Reset all value expression 
		Iterator<PageExpression> bs = this.beans.iterator();
		while ( bs.hasNext() ) {
			bs.next().ResetValue();
		}
	}
	// Parsing Dom Element to search and build beans and attributes
	//
	//
	private void parse(WebAppContext Context) throws DomException {
		BeanMonitoring mon = BeanMonitoring.Utils.GetInstance();
		// Scan for element that is not inside another child block 
		Iterator<Element> elems = this.domEl.select(JOP_BEAN_TAG).iterator();
		elems = this.domEl.getAllElements().iterator();
		HashMap<String,PageExpression> lst = new HashMap<String,PageExpression>();
		while (elems.hasNext() ) {
			Element el = elems.next();
			if ( this.checkIfParentBlockIsThis(el)) {
				// check if bean or attributes element 
				if ( el.tag().getName().equalsIgnoreCase(JOP_BEAN_TAG) ) {
					// build bean and join the same
					PageExpression bean;
					String code = this.parseBean(el);
					if ( !lst.containsKey(AbstractPageExpression.ComputeId(code)) ) {
						bean = new SimplePageExpression(Context,code);
						lst.put(bean.getId(), bean);
						mon.RegisterRefreshable(bean.GetBeansList(), this);
					} else
						bean = lst.get(AbstractPageExpression.ComputeId(code));
					this.beans.add(bean);
					el.attr("id",bean.getId());
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
					String bid = this.parseJavaExpression(a); 
					if ( bid != null ) {
						PageWriteExpression form;
						form = new SimplePageExpression(Context,bid);
						mon.RegisterRefreshable(form.GetBeansList(), this);
						if ( !el.hasAttr("name") || el.attr("name").isEmpty() ) {
							el.attr("name",""+this.auto_id_index);
							auto_id_index++;
						}
						((SimplePageExpression)form).Id = el.attr("name");
						this.forms.add(form);
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
		Iterator<Attribute> attrs = el.attributes().iterator();
		while (attrs.hasNext() ) {
			Attribute attr =  attrs.next();
			if ( !attr.getKey().equalsIgnoreCase(BlockAttribute.JOP_ATTR_ID) && !attr.getKey().equalsIgnoreCase("value") ) {
				String a = attr.getValue();
				String bid = this.parseJavaExpression(a); 
				if ( bid != null ) {
					BlockAttribute at;
					if ( el == this.domEl ) {
						at = new BlockAttribute(context,attr.getKey(),bid);
						if ( attr.getKey().equalsIgnoreCase(BlockAttribute.JOP_ATTR_RENDERED) )
							this.render = at.expr;
						else
							this.attrs.add(at);
						mon.RegisterRefreshable(at.expr.GetBeansList(), this);
					} else {
						String id = this.pageId + "-" + this.auto_id_index++;
						at = new BlockAttribute(context,attr.getKey(),bid,id);
						el.attr(tmp_attr_id,id);
						this.attrs_child.add(at);
						mon.RegisterRefreshable(at.expr.GetBeansList(), this);
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
}
