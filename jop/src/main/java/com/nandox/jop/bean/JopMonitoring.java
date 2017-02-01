package com.nandox.jop.bean;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to monitor an object when call one method.<br>
 * Is applicable to entire Class or single method and when method is called consider page to be refreshed
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    JopMonitoring.java
 * 
 * @date      26 gen 2017 - 26 gen 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={METHOD, TYPE})
public @interface JopMonitoring {

}
