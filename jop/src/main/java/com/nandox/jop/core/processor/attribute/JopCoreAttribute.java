package com.nandox.jop.core.processor.attribute;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to declare an Jop attribute.<br>
 * Properties:<br>
 * 		name to give attribute nae<br>
 *  	priority (optional) to give a attribute execute priority (execute before attribute with low number priority)<br>
 *  	nested (optional) to add a more attributes (comma separated) to the same class that are nested to the main attribute   
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    JopCoreAttribute.java
 * 
 * @date      26 gen 2017 - 26 gen 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={TYPE})
public @interface JopCoreAttribute {
	public String name();
	int priority() default 0;
	String nested() default "";
}
