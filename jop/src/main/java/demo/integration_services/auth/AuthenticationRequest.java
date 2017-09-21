package demo.integration_services.auth;

import demo.model.UserSecurity;
import demo.integration_services.IntegrationServiceRequest;
import demo.integration_services.IntegrationServiceResponse;

public class AuthenticationRequest implements IntegrationServiceRequest, IntegrationServiceResponse {
	private UserSecurity security;

	/**
	 * @return the security
	 */
	public UserSecurity getSecurity() {
		return security;
	}

	/**
	 * @param security the security to set
	 */
	public void setSecurity(UserSecurity security) {
		this.security = security;
	}
}
