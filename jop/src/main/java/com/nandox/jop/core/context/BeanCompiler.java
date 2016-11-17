package com.nandox.jop.core.context;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BeanCompiler.java
 * 
 * @date      17 nov 2016 - 17 nov 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class BeanCompiler {

	private JavaCompiler cmpl;
	/**
	 * @param	  Context	Application context
	 * @date      17 nov 2016 - 17 nov 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public BeanCompiler (WebAppContext Context) {
		JavaCompiler c = ToolProvider.getSystemJavaCompiler();
		c.toString();
		ClassLoader l = c.getClass().getClassLoader();
		c.toString();
	}
}
