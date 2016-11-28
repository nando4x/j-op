package com.nandox.jop.core.context;

/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BeanExecutor.java
 * 
 * @date      27 nov 2016 - 27 nov 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public interface BeanExecutor<E> {
	public E invoke(Object Beans[]);
}
