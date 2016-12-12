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
public class ExpressionInvoker {

	@SuppressWarnings("rawtypes")
	private Class<ExpressionExecutor> beanClass;
	private Method beanMethod;
	private String[] beans;
	/**
	 * @param	  Clazz class of bean
	 * @param	  Method bean's method fired
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	@SuppressWarnings("rawtypes")
	public ExpressionInvoker(Class Clazz, Method Method) {
		this.beanClass = Clazz;
		this.beanMethod = Method;
	}
	/**
	 * @param	  Clazz class of bean
	 * @param	  Beans list of application bean names 
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	@SuppressWarnings("rawtypes")
	public ExpressionInvoker(Class<ExpressionExecutor> Clazz, String[] Beans) {
		this.beanClass = Clazz;
		this.beans = Beans;
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
	 * @return	  result of method or null if error  
	 */
	public Object Invoke(Object BeanInstance) {
		Object ret = null;
		try {
			ret = this.beanMethod.invoke(BeanInstance);
		} catch (Exception e) {}
		return ret;
	}
	public Object Invoke(WebAppContext Context) {
		Object ret = null;
		Object beans[] = new Object[this.beans.length];
		try {
			ExpressionExecutor<?> o = this.beanClass.newInstance();
			for ( int ix=0; ix<this.beans.length; ix++)
				beans[ix] = Context.GetBeanInstance(this.beans[ix]);
			ret = o.invoke(beans);
		} catch (Exception e) {
			// TODO: gestire erroe
		}
		return ret;
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
