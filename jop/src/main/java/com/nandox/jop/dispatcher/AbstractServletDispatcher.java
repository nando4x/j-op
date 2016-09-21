package com.nandox.jop.dispatcher;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    AbstractServletDispatcher.java
 * 
 * @date      19 set 2016 - 19 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public abstract class AbstractServletDispatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected Dispatcher dsp;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.dsp = new Dispatcher();
		dsp.initFromServlet(config);
	}

	protected String proccessPage(String contentPage) {
		return this.dsp.enginePage(contentPage);
	}
}
