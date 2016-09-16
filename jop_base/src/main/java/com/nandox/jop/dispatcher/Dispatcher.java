package com.nandox.jop.dispatcher;

import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public abstract class Dispatcher {

	protected void initFromServlet(ServletConfig config) throws ServletException {
		// read parameters and map them
	}

	protected void initFromFilter(FilterConfig config) throws ServletException {
		// read parameters and map them
	}
	
	private void initEnv(ServletContext ctx, HashMap params) {
		// manage parameters
		
		// add runtime service servlet
	}
}
