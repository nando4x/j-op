package com.nandox.jop.core.context;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.nandox.jop.bean.JopMonitoring;

/**
 * Class to proxy bean.<br>
 * Beans are proxy to intercept calling of @JopMonitoring method to auto refresh page 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ProxyingBean.java
 * 
 * @date      26 gen 2017 - 26 gen 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class BeanMonitoring {
	
	private Map<String,Set<Object>> beans;

	/**
	 * Costruttore
	 * @date      01 feb 2017 - 01 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	
	public BeanMonitoring() {
		this.beans = new HashMap<String,Set<Object>>();
	}
	/** If class bean contain @JopMonitoring annotation create new proxied bean
	 * @date      26 gen 2017 - 26 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  
	 * @exception
	 */
	public Object ProxyBean(Class<?> BeanClass, String BeanName) {
		// Check if is to be monitoring 
		if ( this.hasClassMonitoringAnnotations(BeanClass) ) {
			// Add bean name on the refreshable list
			this.beans.put(BeanName, null);
			// Proxy the bean class
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(BeanClass);
			enhancer.setCallback(new MethodInterceptor() {
			    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			    		if ( method.isAnnotationPresent(JopMonitoring.class) || method.getDeclaringClass().isAnnotationPresent(JopMonitoring.class) )
			    			obj = obj;
			        	return proxy.invokeSuper(obj, args);
			    	}
				}
			);
			return enhancer.create();
		}
		return null;
	}
	
	/**
	 * Register the refreshable block associated to a bean
	 * @date      01 feb 2017 - 01 feb 2017
	 * @param	  BeanName array of bean name
	 * @param	  Block refreshable block
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public void RegisterRefreshable(String[] BeanName, Object Block) {
		for ( int ix=0; ix<BeanName.length; ix++ ) {
			if ( this.beans.containsKey(BeanName[ix]) ) {
				Set<Object> blocks = this.beans.get(BeanName[ix]);
				if ( blocks == null ) 
					blocks = new HashSet<Object>();
				blocks.add(Block);
				this.beans.put(BeanName[ix], blocks);
			}
		}
	}
	// check if class or own method have monitoring annotation @JopMonitoring
	//
	//
	private boolean hasClassMonitoringAnnotations(Class<?> clazz) {
		if ( clazz.isAnnotationPresent(JopMonitoring.class) )
			return true;
		Method m[] = clazz.getMethods();
		for ( int ix=0; ix<m.length; ix++ ) {
			if ( m[ix].isAnnotationPresent(JopMonitoring.class) )
				return true;
		}
		return false;
	}
	/**
	 * @author ee38938
	 *
	 */
	public static class Utils {
		private static BeanMonitoring instance;
		/** Create or return instance
		 * @date      26 gen 2017 - 26 gen 2017
		 * @author    Fernando Costantino
		 * @revisor   Fernando Costantino
		 * @return	  
		 * @exception
		 */
		public static BeanMonitoring GetInstance() {
			if ( instance == null )
				instance = new BeanMonitoring();
			return instance;
		}
	}
}
