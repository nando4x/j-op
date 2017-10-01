package demo.bean;

import demo.integration_services.IntegrationServiceDispatcher;
import demo.integration_services.generalData.IntegrationServiceDataContainer;
import demo.model.User;
import demo.model.UserSecurity;

public class SettingsBean {
	private IntegrationServiceDispatcher dsp;
	private User user;

	public SettingsBean() {
		this.user = new User();
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
	
	public void load() {
		IntegrationServiceDataContainer<User> req = new IntegrationServiceDataContainer<User>();
		req.setData(this.user);
		this.dsp.servRequestDataRetrive(req);
	}
}
