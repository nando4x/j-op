package com.nandox.jop.core.context;

import java.lang.reflect.Method;
/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ApplicationContext.java
 * 
 * @date      07 ott 2016 - 07 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class BeanInvoker {

	@SuppressWarnings("rawtypes")
	private Class beanClass;
	private Method beanMethod;
	/**
	 * Costruttore
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	@SuppressWarnings("rawtypes")
	public BeanInvoker(Class Clazz, Method Method) {
		this.beanClass = Clazz;
		this.beanMethod = Method;
	}
	/**
	 * Descrizione
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  invoker computed hash code with ComputeHash method  
	 */
	public String GetHash() {
		return ComputeHash(this.beanClass, this.beanMethod);
	}
	/**
	 * Descrizione
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  computed invoker hash code with hash code of class and hash code of method  
	 */
	@SuppressWarnings("rawtypes")
	static protected String ComputeHash(Class Clazz, Method Method) {
		return Clazz.hashCode() + "-" + Method.hashCode();
	}
}
