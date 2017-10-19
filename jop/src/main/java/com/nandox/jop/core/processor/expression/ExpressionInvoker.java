package com.nandox.jop.core.processor.expression;

import java.util.Map;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Class to execute an expression.<p>
 * This invoke the runtime class file compiled of an expression on specific web context   
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

	private Class<ExpressionExecutor> expClass;		// expression runtime compiled class
	private String[] beans;	// bean referenced into expression
	/**
	 * @param	  Clazz class of bean
	 * @param	  Beans list of application bean names 
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public ExpressionInvoker(Class<ExpressionExecutor> Clazz, String[] Beans) {
		this.expClass = Clazz;
		this.beans = Beans;
	}
	/**
	 * Invoke expression class on specific context.<br>
	 * @param	  Context	Application context
	 * @param	  Vars 	list of block variables instance [variable name, variable value instanced]
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  result of method or null if error  
	 */
	public Object invoke(WebAppContext Context, Map<String,Object> Vars) {
		Object ret = null;
		Object beans[] = new Object[this.beans.length];
		try {
			ExpressionExecutor o = this.expClass.newInstance();
			for ( int ix=0; ix<this.beans.length; ix++)
				beans[ix] = Context.getBeanInstance(this.beans[ix]);
			ret = o.invoke(WebAppContext.getCurrentRequestContext().getBeanAppContext(),beans,null,null,Vars);
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
		return ret;
	}
	public Object invoke(WebAppContext Context, Object Value, String NativeValue, Map<String,Object> Vars) {
		Object ret = null;
		Object beans[] = new Object[this.beans.length];
		try {
			ExpressionExecutor o = this.expClass.newInstance();
			for ( int ix=0; ix<this.beans.length; ix++)
				beans[ix] = Context.getBeanInstance(this.beans[ix]);
			ret = o.invoke(WebAppContext.getCurrentRequestContext().getBeanAppContext(),beans,Value,NativeValue,Vars);
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
		return ret;
	}
	/**
	 * @return the beans
	 */
	public String[] getBeans() {
		return beans;
	}
}
