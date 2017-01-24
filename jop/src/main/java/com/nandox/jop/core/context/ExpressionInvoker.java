package com.nandox.jop.core.context;

import java.lang.reflect.Method;
/**
 * Single Bean Invoker to invoke a specific method of a bean  
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ExpressionInvoker.java
 * 
 * @date      07 ott 2016 - 07 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class ExpressionInvoker {

	@SuppressWarnings("rawtypes")
	private Class<ExpressionExecutor> expClass;
	private Method beanMethod;
	private String[] beans;
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
		this.expClass = Clazz;
		this.beans = Beans;
	}
	/**
	 * Invoke expression class on specific context.<br>
	 * @param	  Context	Application context
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  result of method or null if error  
	 */
	public Object Invoke(WebAppContext Context) {
		Object ret = null;
		Object beans[] = new Object[this.beans.length];
		try {
			ExpressionExecutor<?> o = this.expClass.newInstance();
			for ( int ix=0; ix<this.beans.length; ix++)
				beans[ix] = Context.GetBeanInstance(this.beans[ix]);
			ret = o.invoke(beans,null,null);
		} catch (Exception e) {
			// TODO: gestire erroe
		}
		return ret;
	}
	public Object Invoke(WebAppContext Context, Object Value, String NativeValue) {
		Object ret = null;
		Object beans[] = new Object[this.beans.length];
		try {
			ExpressionExecutor<?> o = this.expClass.newInstance();
			for ( int ix=0; ix<this.beans.length; ix++)
				beans[ix] = Context.GetBeanInstance(this.beans[ix]);
			ret = o.invoke(beans,Value,NativeValue);
		} catch (Exception e) {
			// TODO: gestire erroe
		}
		return ret;
	}
}
