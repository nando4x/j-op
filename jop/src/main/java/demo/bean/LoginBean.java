package demo.bean;

import demo.model.User;

public class LoginBean {
	private String username;
	private String password;
	private String token;
	private int tryCount;
	private int status;
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
	
	public boolean isAuthorized() {
		return this.isAuthenticated() && this.token != null && this.token.equals("12345");
	}
	public boolean isAuthenticated() {
		return this.username!= null && this.password != null && this.username.equals("test") && this.password.equals("test");
	}
	
	public void submit() {
		switch ( this.status) {
			case 0:
				if ( !this.isAuthenticated() )
					this.tryCount--;
				else {
					this.tryCount = 5;
					this.status = 1;
				}
				break;
			case 1:
				if ( !this.isAuthorized() )
					this.tryCount--;
				else {
					this.tryCount = 5;
					this.status = 2;
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
