package com.nandox.jop.bean;

import java.util.Map;
/**
 * Application Context visible in expression.<br>
 * In every expression there is an instance of this interface named appContext, with this in the expression you can get every<br>
 * context information and data   
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BeanAppContext.java
 * 
 * @date      27 nov 2016 - 27 nov 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public interface BeanAppContext {
	
	/**
	 * Get servlet request parameters list
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  parameters map [name,value]
	 */
	public Map<String,String[]> getParameters();
	/**
	 * Get request parameter value
	 * @param	  Name parameter name
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  parameter value
	 */
	public String[] getParameter(String Name);
	/**
	 * Add parameter
	 * @param	  Name parameter name
	 * @param	  Value parameter value
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	 
	 */
	public void addParameter(String Name, String Value);
}
