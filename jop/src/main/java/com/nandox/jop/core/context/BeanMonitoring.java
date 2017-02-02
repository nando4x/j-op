package com.nandox.jop.core.context;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.nandox.jop.bean.JopMonitoring;
import com.nandox.jop.core.processor.RefreshableBlock;

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
	
	private Map<String,Set<RefreshableBlock>> beans;

	/**
	 * Costruttore
	 * @date      01 feb 2017 - 01 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	
	public BeanMonitoring() {
		this.beans = new HashMap<String,Set<RefreshableBlock>>();
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
			enhancer.setCallback(new Interceptor(BeanName,this));
			Object b = enhancer.create();
			return b;
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
	public void RegisterRefreshable(String[] BeanName, RefreshableBlock Block) {
		for ( int ix=0; ix<BeanName.length; ix++ ) {
			if ( this.beans.containsKey(BeanName[ix]) ) {
				Set<RefreshableBlock> blocks = this.beans.get(BeanName[ix]);
				if ( blocks == null ) 
					blocks = new HashSet<RefreshableBlock>();
				blocks.add(Block);
				this.beans.put(BeanName[ix], blocks);
			}
		}
	}
	
	/**
	 * Descrizione
	 * @date      02 feb 2017 - 02 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected void setToBeRefreshed(String BeanName) {
		Set<RefreshableBlock> b = this.beans.get(BeanName);
		if ( b != null ) {
			Iterator<RefreshableBlock> i = b.iterator();
			while ( i.hasNext() ) {
				i.next().SetToBeRefreshed();
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
	//
	//
	//
	private class Interceptor implements MethodInterceptor {
		private String beanName;
		private BeanMonitoring monitor;
	    /**
		 * Costruttore
		 * @date      02 feb 2017 - 02 feb 2017
		 * @author    Fernando Costantino
		 * @revisor   Fernando Costantino
		 * @exception
		 */
		protected Interceptor(String BeanName, BeanMonitoring Monitor) {
			this.beanName = BeanName;
			this.monitor = Monitor;
		}

		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    		if ( method.isAnnotationPresent(JopMonitoring.class) || method.getDeclaringClass().isAnnotationPresent(JopMonitoring.class) ) {
    			this.monitor.setToBeRefreshed(beanName);
    		}
        	return proxy.invokeSuper(obj, args);
    	}
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
