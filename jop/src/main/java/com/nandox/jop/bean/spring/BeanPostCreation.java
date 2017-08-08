package com.nandox.jop.bean.spring;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.nandox.jop.core.context.BeanMonitoring;
/**
 * Spring Bean post processor.<br>
 * In the post creation attach proxy to bean before initialization.<br>
 * Use it in the configuration if you want monitor the beans:<br><br>
 * &emsp;		<bean class="com.nandox.jop.bean.spring.BeanPostCreation"/>
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
public class BeanPostCreation implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
	
	private AutowireCapableBeanFactory bfact;
	/* Monitoring the bean 
	 * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation(java.lang.Class, java.lang.String)
	 */
	public Object postProcessBeforeInstantiation(Class<?> Bean, String BeanName) throws BeansException {
		BeanMonitoring bm = BeanMonitoring.Utils.getInstance();
		Object b = bm.proxyBean(Bean, BeanName);
		if ( b != null )
			this.bfact.configureBean(b, BeanName);
		return b;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	public void setBeanFactory(BeanFactory factory) throws BeansException {
		this.bfact = (AutowireCapableBeanFactory)factory;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation(java.lang.Object, java.lang.String)
	 */
	public boolean postProcessAfterInstantiation(Object arg0, String arg1) throws BeansException {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessPropertyValues(org.springframework.beans.PropertyValues, java.beans.PropertyDescriptor[], java.lang.Object, java.lang.String)
	 */
	public PropertyValues postProcessPropertyValues(PropertyValues arg0, PropertyDescriptor[] arg1, Object arg2,
			String arg3) throws BeansException {
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
