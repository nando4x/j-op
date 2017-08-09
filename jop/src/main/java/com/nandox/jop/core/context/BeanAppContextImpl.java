package com.nandox.jop.core.context;

import java.util.Map;

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
	private int recursiveCnt;	// recursive count to check if  not more referenced
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
	/**
	 * @return the recursiveCnt
	 */
	protected int getRecursiveCnt() {
		return recursiveCnt;
	}
	/**
	 * @param recursiveCnt the recursiveCnt to set
	 */
	protected void setRecursiveCnt(int recursiveCnt) {
		this.recursiveCnt = recursiveCnt;
	}
}
