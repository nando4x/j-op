package demo.bean;

import demo.model.UserSecurity;
import demo.integration_services.IntegrationServiceDispatcher;
import demo.integration_services.generalData.IntgrationServiceRetriveData;
import demo.integration_services.generalData.IntegrationServiceDataContainer;

public class LoginBean {
	private String username;
	private String password;
	private String token;
	private int tryCount;
	private int status;
	private boolean autenticated;
	private boolean authorized;
	private IntgrationServiceRetriveData<UserSecurity> is;
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
		this.is = new IntgrationServiceRetriveData<UserSecurity>(dsp);
		this.dsp = dsp;
	}
	
	public void submit() {
		IntegrationServiceDataContainer<UserSecurity> req = new IntegrationServiceDataContainer<UserSecurity>();
		switch ( this.status) {
			case 0:
				this.sec.setUsername(this.username);
				req.setData(this.sec);
				this.sec = is.servRequest(req).getData();
				if ( this.sec.getPassword() != null && this.sec.getPassword().equals((this.password == null?"":this.password)) ) {
					this.tryCount--;
					this.autenticated = false;
				} else {
					this.tryCount = 5;
					this.status = 1;
					this.autenticated = true;
				}
				break;
			case 1:
				req.setData(this.sec);
				this.sec = is.servRequest(req).getData();
				if ( this.sec.getToken() != null && this.sec.getToken().equals((this.token == null?"":this.token)) ) {
					this.tryCount--;
					this.authorized = false;
				} else {
					this.tryCount = 5;
					this.status = 2;
					this.authorized = true;
				}
		}
	}
	
	public void reset() {
		this.username = null;
		this.password = null;
		this.token = null;
		this.tryCount = 5;
		this.status = 0;
	}
}
