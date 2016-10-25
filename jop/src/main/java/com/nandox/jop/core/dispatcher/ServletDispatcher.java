package com.nandox.jop.core.dispatcher;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.nandox.jop.core.ErrorsDefine;

/**
 * Real servlet handler to read page file requested and process its content 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ServletDispatcher.java
 * 
 * @date      19 set 2016 - 19 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class ServletDispatcher extends AbstractServletDispatcher {
	private static final long serialVersionUID = 1L;

	/* Read file corrisponding to url requested, and return content processed  
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// read requested page file in buffer
		String pth = req.getServletPath()+req.getPathInfo();
		String contextPath = this.getServletContext().getRealPath(File.separator);
		File f = new File(contextPath+File.separator+pth);
		if ( f.canRead() ) {
			InputStream i = this.getServletContext().getResourceAsStream(pth);
			byte buff[] = new byte[(int)f.length()]; 
			i.read(buff);
			i.close();
			// Process page content
			String out = new String(buff);
			try {
				out = this.processPage((pth.charAt(0)=='/'?pth.substring(1):pth),out);
			} catch (Exception e) {
				// TODO: manage exception
				throw new ServletException(e);
			}
			// Send out processed content
			resp.setContentLength(out.length());
			resp.setContentType("text/html");
			resp.getWriter().println(out);
			resp.getWriter().close();
		} else {
			throw new FileNotFoundException(ErrorsDefine.JOP_PAGE_NOTFOUND); 
		}
	}

	/* Like get
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
