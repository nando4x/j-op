package demo.bean;

public class LoginBean {
	private String username;
	private String password;
	private boolean inaction;
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isAuthorized() {
		return this.username!= null && this.password != null && this.username.equals("test") && this.password.equals("test");
	}
	
	public boolean isInAction() {
		return this.inaction;
	}
	public void submit() {
		this.inaction = true;
	}
	public void endAction() {
		this.inaction = false;
	}
}
