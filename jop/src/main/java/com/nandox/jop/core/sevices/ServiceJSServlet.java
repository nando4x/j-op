package com.nandox.jop.core.sevices;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.nandox.jop.core.dispatcher.AbstractServletDispatcher;
import com.nandox.libraries.utils.Reflection;
import com.nandox.libraries.utils.xml.GenerateXmlWithCDATA;

/**
 * Servlet for JavaScript services and file script.<br>
 * The script file are downloaded from path jopscript/*.<br> 
 * The services are implemented to exchange XML fragment, command and response<br>
 * <br>   
 * &emsp;	Possible Service:<br>
 * <br>  
 * &emsp;&emsp;     	/inject		to base inject operation<br>
 * &emsp;&emsp;  		Services command:<br>
 * &emsp;&emsp;&emsp;  			postBlock: { Jop.id:'jop id', .... } - response xml with affected blocks<br>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ServiceJSServlet.java
 * 
 * @date      25 ott 2016 - 25 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class ServiceJSServlet extends AbstractServletDispatcher {

	private static final long serialVersionUID = 1L;
	/** */
	protected static final String SCRIPT_BASE_PATH = "jsscript"; 
	/** */
	protected static final String SCRIPT_REQ = "/jopscript"; 
	/** */
	protected static final String SERVICE_REQ = "/jopservices"; 
	/** */
	protected static final String SERVICE_NAME_INKJECT = "inkject"; 

	private Map<String,ServiceJSManager> services;
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig Config) throws ServletException {
		super.init(Config);
		try {
			Iterator<Class<?>> l = Reflection.getClassesForPackage(this.getClass().getPackage(), ServiceJSManager.class,true).iterator();
			this.services = new HashMap<String,ServiceJSManager>();
			while (l.hasNext()) {
				try {
					ServiceJSManager s = (ServiceJSManager)l.next().newInstance();
					this.services.put(s.getIdentifier(), s);
				} catch(Exception e) {
					// TODO: manage error on create service manager
				}
			}
		} catch (Exception e) {
			// TODO: manage error on scan package  
		}
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	/* Like get
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Dispatch the req depend on the path
		if ( req.getServletPath().equals(SCRIPT_REQ)) {
			this.readFile(req, resp);
		} else if ( req.getServletPath().equals(SERVICE_REQ)) {
			this.servicesManager(req, resp);
		} else {
			System.out.println("query:"+req.getQueryString());
			this.test(req,resp);
		}
	}
	// Manage services searching them from req path and then excute
	//
	//
	private void servicesManager(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Extract service and command
		if ( req.getPathInfo().length() > 0 ) {
			String path = req.getPathInfo().substring(1);
			if ( path.indexOf("/") > 0 ) {
				String service = path.substring(0, path.indexOf("/"));
				String cmd = path.substring(path.indexOf("/")+1);
				// Search service manager and execute it 
				ServiceJSManager serv = this.services.get(service);
				if ( serv != null  ) {
					ServiceJSResponse r = serv.execute(this.dsp,cmd,req.getParameterMap());
					try {
						this.serviceResponser(r, resp);
					} catch (Exception e) {}
					return;
				}
			}
		}
		// TODO: service unknown
		
		
		String out = "DONE\n\r"
				+"XXX";
		resp.setContentLength(out.length());
		resp.setContentType("text/plain");
		resp.getWriter().println(out);
		resp.getWriter().close();
	}
	
	// service responser to transform ServiceJSResponse response to http response and send it 
	//
	//
	private void serviceResponser(ServiceJSResponse sresp, HttpServletResponse resp) throws Exception {
		String ret = "";
		switch (sresp.getFormat()) {
			case XML:
				ret = new GenerateXmlWithCDATA().generate(sresp.getData());
				resp.setContentType("text/xml");
				break;
			default:
				break;
		}
		resp.setContentLength(ret.length());
		resp.getWriter().println(ret);
		resp.getWriter().close();
	}
	
	// Read a file script under package of this  servlet
	//
	//
	private void readFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		InputStream i = this.getClass().getResourceAsStream(SCRIPT_BASE_PATH+"/"+req.getPathInfo());
		if ( i != null ) {
			int len = 0;
			StringBuffer jb = new StringBuffer();
			while ((len = i.available()) > 0 ) {
				byte buff[] = new byte[len]; 
				i.read(buff);
				jb.append(new String(buff));
			}
			resp.setContentLength(jb.length());
			resp.setContentType("text/javascript");
			resp.getWriter().println(jb);
			resp.getWriter().close();
		}
	}
	
	private void test (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ret = "["
				+ "{'id': 0, 'name': 'Alabama0', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'},"
				+ "{'id': 1, 'name': 'Alabama1', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'},"
				+ "{'id': 2, 'name': 'Alabama2', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'},"
				+ "{'id': 3, 'name': 'Alabama3', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'},"
				+ "{'id': 4, 'name': 'Alabama4', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'},"
				+ "{'id': 5, 'name': 'Alabama5', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'},"
				+ "{'id': 6, 'name': 'Alabama6', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'},"
				+ "{'id': 7, 'name': 'Alabama7', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'},"
				+ "{'id': 8, 'name': 'Alabama8', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'},"
				+ "{'id': 9, 'name': 'Alabama9', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'},"
				+ "{'id': 10, 'name': 'Alabama10', 'label':'xxxx', 'abbreviation': 'AL', 'capital':'Montgomery'}"
				+ "]";
		ret = "{'numRows': 101, 'items': " + ret + ", 'identity':'id'}";
		resp.setContentLength(ret.length());
		resp.setContentType("text/json");
		resp.getWriter().println(ret);
		resp.getWriter().close();
	}
}
