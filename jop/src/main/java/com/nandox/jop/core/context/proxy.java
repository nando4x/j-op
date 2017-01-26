package com.nandox.jop.core.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import net.sf.cglib.proxy.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    proxy.java
 * 
 * @date      25 gen 2017 - 25 gen 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class proxy implements BeanPostProcessor {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	  // simply return the instantiated bean as-is
	  public Object postProcessBeforeInitialization(Object bean, String beanName)
                                            throws BeansException {
      	  Enhancer enhancer = new Enhancer();
      	  enhancer.setSuperclass(bean.getClass());
      	  enhancer.setCallback(new MethodInterceptor() {
      	    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
      	        throws Throwable {
      	      if(method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
      	        return "Hello cglib!";
      	      } else {
      	        return proxy.invokeSuper(obj, args);
      	      }
      	    }
      	  });
      	  Object f = enhancer.create();
      	
      	return f; // we could potentially return any object reference here...
	  }

	  public Object postProcessAfterInitialization(Object bean, String beanName)
	                                                                     throws BeansException {
	      System.out.println("Bean '" + beanName + "' created : " + bean.toString());
	      return bean;
	  }
	  static class Handler implements InvocationHandler {
	        private final Object original;
	        public Handler(Object original) {
	            this.original = original;
	        }
	        public Object invoke(Object proxy, Method method, Object[] args)
	                throws IllegalAccessException, IllegalArgumentException,
	                InvocationTargetException {
	            System.out.println("BEFORE");
	            method.invoke(original, args);
	            System.out.println("AFTER");
	            return null;
	        }
	    }
}
