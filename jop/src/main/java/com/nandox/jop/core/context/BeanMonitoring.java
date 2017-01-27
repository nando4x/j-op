package com.nandox.jop.core.context;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.beans.BeanCopier;

/**
 * Class to proxy bean.<br>
 * Beans are proxy to intercept calling of @JopMonitoredBean method to auto refresh page 
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

	public Object ProxyBean(Class<?> Bean, String BeanName) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Bean);
		enhancer.setCallback(new MethodInterceptor() {
		    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		        	return proxy.invokeSuper(obj, args);
		    	}
			}
		);
		Object b = enhancer.create();
		return b;
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
