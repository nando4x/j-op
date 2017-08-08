package com.nandox.jop.core.context;

import java.util.Map;

/**
 * Executor of an page expression.<br>
 * Basic interface of runtime class file complied associate to expression 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ExpressionExecutor.java
 * 
 * @date      27 nov 2016 - 27 nov 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public interface ExpressionExecutor<E> {
	
	/** Main method to invoke expression
	 * @param	  Beans array of beans called in expression
	 * @param	  Value value for writing invoke
	 * @param	  NativeValue string value for writing invoke
	 * @param	  Vars 	list of block variables instance [variable name, variable value instanced]
	 * @date      17 nov 2016 - 17 nov 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception specific type value
	 */
	public E invoke(Object Beans[], Object Value, String NativeValue, Map<String,Object> Vars);
}
