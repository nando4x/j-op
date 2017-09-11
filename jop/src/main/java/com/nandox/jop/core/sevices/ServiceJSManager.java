package com.nandox.jop.core.sevices;

import java.util.Map;

import com.nandox.jop.core.dispatcher.Dispatcher;
/**
 * Interface for all services javascript manager.<p>
 * Every service javascript is loaded from init ServiceJSServlet and have an identifier equal to base path service<br>
 * (es: inject) that is used to identify and execute the manager.<br>
 * To implement a new service create a class that implements this interface and into getIdentifier method return the identifier.<br>
 * es for inject service:<br>
 * <br>
 * &emsp;	public Class inject implements ServiceJSManager {<br>
 * &emsp;&emsp;			public String getIdentifier() {<br>
 * &emsp;&emsp;&emsp;				return "inject";<br>
 * &emsp;&emsp;			}<br>
 * &emsp;&emsp;			...<br>
 * &emsp;		}<br>
 * <br>
 * service is called by url /jopservices/inject
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ServiceJSManager.java
 * 
 * @date      24 gen 2017 - 24 gen 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public interface ServiceJSManager {
	/**
	 * Return the service manager identifier
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public String getIdentifier();
	/**
	 * Execute a specific command of service
	 * @param	  Dsp application dispatcher
	 * @param	  Cmd command to execute
	 * @param	  Params parameter map received from url request query
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return    Service response
	 */
	public ServiceJSResponse execute(Dispatcher Dsp, String Cmd, Map<String,String[]> Params);
}
