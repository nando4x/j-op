package demo.bean;

import demo.model.UserSecurity;

import com.nandox.jop.core.context.WebAppContext;

import demo.integration_services.IntegrationServiceDispatcher;
import demo.integration_services.auth.AuthenticationRequest;

public class LoginBean {
	private String username;
	private String password;
	private String token;
	private int tryCount;
	private int status;
	private boolean autenticated;
	private boolean authorized;
	private UserSecurity sec;
	IntegrationServiceDispatcher dsp;
	
	public LoginBean () {
		this.sec = new UserSecurity();
	}
	/**
	 * @return the username
	 */
	public String getUsername() {return username;}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {this.username = username;}
	/**
	 * @return the password
	 */
	public String getPassword() {return password;}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {this.password = password;}
	/**
	 * @return the token
	 */
	public String getToken() {return token;}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {this.token = token;}
	
	/**
	 * @return the tryCount
	 */
	public int getTryCount() {return tryCount;}
	
	public boolean isAuthorized() {return this.authorized;}
	public boolean isAuthenticated() {return this.autenticated;}
	
	/**
	 * @param dsp the dsp to set
	 */
	public void setDsp(IntegrationServiceDispatcher dsp) {
		this.dsp = dsp;
	}
	
	public void submit() {
		AuthenticationRequest req = new AuthenticationRequest();
		switch ( this.status) {
			case 0:
				this.sec.setUsername(this.username);
				this.sec.setPassword(this.password);
				req.setSecurity(this.sec);
				req = (AuthenticationRequest)this.dsp.servRequest(req);
				if ( req.getSecurity().getUID() != null ) {
					this.sec = req.getSecurity();
					this.tryCount = 5;
					this.status = 1;
					this.autenticated = true;
				} else {
					this.tryCount--;
					this.autenticated = false;
				}
				break;
			case 1:
				this.sec.setToken(this.token);
				req.setSecurity(this.sec);
				req = (AuthenticationRequest)this.dsp.servRequest(req);
				if ( req.getSecurity().getUID() != null ) {
					this.sec = req.getSecurity();
					this.tryCount = 5;
					this.status = 2;
					this.authorized = true;
					WebAppContext.getCurrentRequestContext().getSession().setAttribute("security", this.sec);
				} else {
					this.tryCount--;
					this.authorized = false;
				}
		}
	}
	
	public void reset() {
		this.autenticated = false;
		this.authorized = false;
		this.username = null;
		this.password = null;
		this.token = null;
		this.tryCount = 5;
		this.status = 0;
	}
}
