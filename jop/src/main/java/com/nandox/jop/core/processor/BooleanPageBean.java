package com.nandox.jop.core.processor;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Boolean Page Bean used for boolean page block attributes
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BooleanPageBean.java
 * 
 * @date      16 ott 2016 - 16 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class BooleanPageBean extends PageBeanPrototype<Boolean> {

	public BooleanPageBean(WebAppContext Context, String BeanId) throws DomException {
		super(Context, BeanId);
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageBeanPrototype#Fire(com.nandox.jop.core.context.WebAppContext)
	 */
	@Override
	public Boolean Fire(WebAppContext Context) {
		return (Boolean)this.Invoke(Context);
	}
}
