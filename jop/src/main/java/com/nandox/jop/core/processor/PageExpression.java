package com.nandox.jop.core.processor;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Represent generic page bean
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
public interface PageExpression {

	/**
	 * @return the beanId
	 */
	String getBeanId();
	/**
	 * @return the bean code
	 */
	String getBeanCode();

	/**
	 * Fire bean method on specific context.<br>
	 * Get bean instance from context and then use own invoker to invoke method
	 * @param	  Context	Application context
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  result of method in type bean format  
	 */
	Object Fire(WebAppContext Context);

	/**
	 * Reset bean value to null.<br>
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	    
	 */
	void ResetValue();

}