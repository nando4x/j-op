package com.nandox.jop.bean.spring;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import com.nandox.jop.core.context.BeanMonitoring;
/**
 * Spring Bean post processor.<br>
 * In the post creation do this:<br>
 * attach proxy to bean before initialization 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BeanPostCreation.java
 * 
 * @date      26 gen 2017 - 26 gen 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class BeanPostCreation implements MethodBeforeAdvice {//InstantiationAwareBeanPostProcessor {

	/* (non-Javadoc)
	 * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
		arg0 = arg0;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation(java.lang.Object, java.lang.String)
	 */
	public boolean postProcessAfterInstantiation(Object arg0, String arg1) throws BeansException {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation(java.lang.Class, java.lang.String)
	 */
	public Object postProcessBeforeInstantiation(Class<?> Bean, String BeanName) throws BeansException {
		BeanMonitoring bm = BeanMonitoring.Utils.GetInstance();
		return bm.ProxyBean(Bean, BeanName);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessPropertyValues(org.springframework.beans.PropertyValues, java.beans.PropertyDescriptor[], java.lang.Object, java.lang.String)
	 */
	public PropertyValues postProcessPropertyValues(PropertyValues arg0, PropertyDescriptor[] arg1, Object arg2,
			String arg3) throws BeansException {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	public Object postProcessAfterInitialization(Object Bean, String BeanName) throws BeansException {
		return Bean;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
	public Object postProcessBeforeInitialization(Object Bean, String BeanName) throws BeansException {
		return Bean;
	}

}
