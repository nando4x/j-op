package com.nandox.jop.core.context;

import java.util.Map;
import java.util.HashMap;

import com.nandox.jop.bean.BeanAppContext;

/**
 * Implementation of Bean Application Context 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BeanAppContextImpl.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class BeanAppContextImpl implements BeanAppContext {
	private Map<String,String[]> params;	// request parameters
	/**
	 * @param	  Context	Application context
	 * @param	  Code		expression code
	 * @param	  Vars 		list of block variables definitions [variable name, class]
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public BeanAppContextImpl (Map<String,String[]> Params) {
		this.params = Params;
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
}
