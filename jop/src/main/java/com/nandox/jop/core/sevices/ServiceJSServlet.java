package com.nandox.jop.core.sevices;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONTokener;
import org.json.JSONObject;
import org.json.JSONException;

import com.nandox.jop.core.dispatcher.Dispatcher;

/**
 * Servlet for JavaScript services and file script.<br>
 * The script file are downloaded from path jopscript/*.<br> 
 * The services are implemented to exchange XML fragment, command and response
 *   
 *   Possible Service:
 *   
 *      	/inject		to base inject operation
 *   		Services command:<br>
 *   			postBlock: { Jop.id:'jop id', .... } - response html of affected blocks
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

public class ServiceJSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	/** */
	protected static final String SCRIPT_BASE_PATH = "jsscript"; 
	/** */
	protected static final String SCRIPT_REQ = "/jopscript"; 
	/** */
	protected static final String SERVICE_REQ = "/jopservices"; 

	protected Dispatcher dsp;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig Config) throws ServletException {
		super.init(Config);
		this.dsp = new Dispatcher();
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
			this.servicesDispatcher(req, resp);
		} else {
			System.out.println("query:"+req.getQueryString());
			this.test(req,resp);
		}
	}
	//
	//
	//
	private void servicesDispatcher(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// extract service and command
		String path = req.getPathInfo();
		if ( path.indexOf("/") > 0 ) {
			
		}
		String out = "DONE\n\r"
				+"XXX";
		resp.setContentLength(out.length());
		resp.setContentType("text/plain");
		resp.getWriter().println(out);
		resp.getWriter().close();
		  /*StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) {  }
		  // get service context
		  String sctx = req.getPathInfo();
		  
		  try {
			  JSONObject dt = new JSONObject(new JSONTokener(jb.toString()));
		  } catch (JSONException e) {
		    // crash and burn
		    throw new IOException("Error parsing JSON request string");
		  }*/
		
		  // Work with the data using methods like...
		  // int someInt = jsonObject.getInt("intParamName");
		  // String someString = jsonObject.getString("stringParamName");
		  // JSONObject nestedObj = jsonObject.getJSONObject("nestedObjName");
		  // JSONArray arr = jsonObject.getJSONArray("arrayParamName");
		  // etc...	}
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
