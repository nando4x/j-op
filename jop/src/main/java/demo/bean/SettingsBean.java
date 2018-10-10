package demo.bean;

import com.nandox.jop.core.context.WebAppContext;

import demo.integration_services.IntegrationServiceDispatcher;
import demo.integration_services.dataservice.IntegrationServiceDataContainer;
import demo.model.User;
import demo.model.SecurityContact;
import demo.model.UserSecurity;

public class SettingsBean {
	private IntegrationServiceDispatcher dsp;
	private User user;
	private UserSecurity sec;
	private SecurityContact security;

	public SettingsBean() {
		this.user = new User();
		this.security = new SecurityContact();
		this.sec = (UserSecurity)WebAppContext.getCurrentRequestContext().getSession().getAttribute("security");
	}
	/**
	 * @param dsp the dsp to set
	 */
	public void setDsp(IntegrationServiceDispatcher dsp) {
		this.dsp = dsp;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * @return the security
	 */
	public SecurityContact getSecurity() {
		return security;
	}
	/**
	 * @param sec the sec to set
	 */
	public void setSecurity(SecurityContact sec) {
		this.security = sec;
	}
	public void load() {
		IntegrationServiceDataContainer<User,String> req = new IntegrationServiceDataContainer<User,String>();
		req.setData(this.user);
		if ( this.sec != null )
			req.setKey(this.sec.getUID());
		this.dsp.servRequestDataRetrive(req);

		IntegrationServiceDataContainer<SecurityContact,String> req1 = new IntegrationServiceDataContainer<SecurityContact,String>();
		req1.setData(this.security);
		if ( this.sec != null )
			req1.setKey(this.sec.getUID());
		this.dsp.servRequestDataRetrive(req1);
	}
	public void save() {
		IntegrationServiceDataContainer<User,String> req = new IntegrationServiceDataContainer<User,String>();
		req.setData(this.user);
		if ( this.sec != null )
			req.setKey(this.sec.getUID());
		this.dsp.servRequestDataChange(req);
	}
}
