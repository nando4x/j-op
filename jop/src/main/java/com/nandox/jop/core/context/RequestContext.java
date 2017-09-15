package com.nandox.jop.core.context;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.nandox.jop.bean.BeanAppContext;
import com.nandox.jop.core.processor.RefreshableBlock;
import com.nandox.jop.core.processor.expression.ExpressionValue;
import com.nandox.jop.core.processor.JopId;
/**
 * Implementation of Request Application Context.<p>
 * The request context is instanced for each http request processed by dispatcher.<br>
 * It contains expression value map [expression id,expression value] and refreshable block map [block id, block]  
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    RequestContext.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class RequestContext implements BeanAppContext {
	private HttpServletRequest httpRequest;
	private Session session;	// session
	private Map<String,String[]> params;	// request parameters
	private Map<String,ExpressionValue<?>> eval;	// expression values
	private Map<String,RefreshableBlock> rblock;	// refreshable block
	/**
	 * @param	  Request	standard http request
	 * @param	  Params	standard http request parameters
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public RequestContext (HttpServletRequest Request, Map<String,String[]> Params) {
		this.httpRequest = Request;
		this.params = Params;
		this.eval = new HashMap<String,ExpressionValue<?>>();
		this.rblock = new HashMap<String,RefreshableBlock>();
		HttpSession sess = Request.getSession();
		if ( sess.isNew() )
			this.session = new Session(sess);
		else {
			this.session = (Session)sess.getAttribute("jopsession");
		}
	}
	/**
	 * Return current native http request 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Bean instance
	 */
	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}
	/**
	 * Return current BeanAppContext 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Bean instance
	 */
	public BeanAppContext getBeanAppContext() {
		return this;
	}
	/**
	 * Return current Session 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Bean instance
	 */
	public Session getSession() {
		return session;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.bean.BeanAppContext#getParameters()
	 */
	@Override
	public Map<String, String[]> getParameters() {
		return this.params;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.bean.BeanAppContext#getParameter(java.lang.String)
	 */
	@Override
	public String[] getParameter(String Name) {
		if ( this.params != null )
			return this.params.get(Name);
		return null;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.bean.BeanAppContext#addParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public void addParameter(String Name, String Value) {
		if ( this.params == null )
			this.params = new HashMap<String,String[]>();
		String v[] = {Value};
		this.params.put(Name, v);
	}
	/**
	 * @param params the params to set
	 */
	public void setParameters(Map<String, String[]> params) {
		this.params = params;
	}
	/**
	 * Add expression value
	 * @param	  ExpVal expression value
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	 
	 */
	public void addExpressionValue(ExpressionValue<?> ExpVal) {
		this.eval.put(ExpVal.getId(), ExpVal);
	}
	/**
	 * Return expression value
	 * @param	  Id expression identifier
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  expression value corresponding to Id
	 */
	public ExpressionValue<?> getExpressionValue(String Id) {
		return this.eval.get(Id);
	}
	/**
	 * Add refreshable block
	 * @param	  Id block Jop identifier
	 * @param	  block refreshbale block 
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	 
	 */
	public void addRefreshableBlock(JopId Id, RefreshableBlock block) {
		this.rblock.put(Id.composite(), block);
	}
	
	/**
	 * Return refreshable block
	 * @param	  Id block Jop identifier
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  block corresponding to Id
	 */
	public RefreshableBlock getRefreshableBlock(JopId Id) {
		return this.rblock.get(Id.composite());
	}
}
