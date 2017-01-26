package com.nandox.jop.bean.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
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
public class BeanPostCreation implements BeanPostProcessor {

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
		// TODO Auto-generated method stub
		return Bean;
	}

}
