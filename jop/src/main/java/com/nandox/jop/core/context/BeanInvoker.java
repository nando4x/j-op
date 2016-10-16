package com.nandox.jop.core.context;

import java.lang.reflect.Method;
/**
 * Single Bean Invoker to invoke a specific method of a bean  
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
	 * @param	  Clazz class of bean
	 * @param	  Method bean's method fired
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
	 * Check if bean method is compliance to return type.<br>
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception BeanException if return type unmatch or not extend ReturnClass
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void CheckCompliance (Class ReturnClass) throws BeanException {
		try {
			this.beanMethod.getReturnType().asSubclass(ReturnClass);
		} catch (Exception e) { throw new BeanException(); } 
	}
	/**
	 * Fire bean method on specific instance.<br>
	 * The method will return an object convertible to string
	 * @param	  BeanInstance instance of bean to invoke
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  result of method in string format or null if error  
	 */
	public String Invoke(Object BeanInstance) {
		Object ret = null;
		try {
			ret = this.beanMethod.invoke(BeanInstance);
		} catch (Exception e) {}
		return ret.toString();
	}
	/**
	 * Return this bean hash code
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  invoker computed hash code with ComputeHash method  
	 */
	public String GetHash() {
		return ComputeHash(this.beanClass, this.beanMethod);
	}
	/**
	 * Utils to compute and return a bean hash code by class and method
	 * @param	  Clazz class of bean
	 * @param	  Method bean's method fired
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  computed invoker hash code with hash code of class and hash code of method  
	 */
	@SuppressWarnings("rawtypes")
	static protected String ComputeHash(Class Clazz, Method Method) {
		return Clazz.hashCode() + "-" + Method.hashCode();
	}
}
