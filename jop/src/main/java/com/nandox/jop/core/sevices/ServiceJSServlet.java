package com.nandox.jop.core.sevices;

import java.io.IOException;
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
 * Servlet for JavaScript services.<br>
 * The services are implemented to exchange json fragment, command and response
 *   
 *   Possible Service:
 *   
 *      	/inject		to base inject operation
 *   		Services command:<br>
 *   			GetBlock: { cmd:'getblock', id:'jop id' } - response block html
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
		StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { /*report an error*/ }
		  // get service context
		  String sctx = req.getPathInfo();
		  
		  try {
			  JSONObject dt = new JSONObject(new JSONTokener(jb.toString()));
		  } catch (JSONException e) {
		    // crash and burn
		    throw new IOException("Error parsing JSON request string");
		  }

		  // Work with the data using methods like...
		  // int someInt = jsonObject.getInt("intParamName");
		  // String someString = jsonObject.getString("stringParamName");
		  // JSONObject nestedObj = jsonObject.getJSONObject("nestedObjName");
		  // JSONArray arr = jsonObject.getJSONArray("arrayParamName");
		  // etc...	}
	}
}
