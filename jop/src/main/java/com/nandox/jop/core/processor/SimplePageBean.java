package com.nandox.jop.core.processor;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Simple page bean that return a string format
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    SimplePageBean.java
 * 
 * @date      13 ott 2016 - 13 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class SimplePageBean extends PageBeanPrototype<String> {

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageBean#PageBean(WebAppContext,String)
	 */
	public SimplePageBean(WebAppContext Context, String BeanId) throws DomException {
		super(Context, BeanId);
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageBean#Fire(com.nandox.jop.core.context.WebAppContext)
	 */
	@Override
	public String Fire(WebAppContext Context) {
		return (String)this.Invoke(Context);
	}
}
