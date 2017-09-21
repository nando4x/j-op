package demo.integration_services.stub;

import demo.integration_services.IntegrationServiceDispatcher;
import demo.integration_services.IntegrationServiceRequest;
import demo.integration_services.IntegrationServiceResponse;
import demo.integration_services.generalData.IntegrationServiceDataContainer;
import demo.integration_services.auth.AuthenticationRequest;
import demo.model.IntegrationServiceData;


public class DispatcherImpl implements IntegrationServiceDispatcher {

	@Override
	public <E extends IntegrationServiceData> IntegrationServiceDataContainer<E> servRequestDataRetrive(
			IntegrationServiceDataContainer<E> Request) {
		if ( Request != null ) {
			if ( Request.getData() instanceof demo.model.UserSecurity ) {
				((demo.model.UserSecurity)Request.getData()).setUsername("test");
				((demo.model.UserSecurity)Request.getData()).setPassword("test");
				return Request;
			}
		}
		return null;
	}

	@Override
	public IntegrationServiceResponse servRequest(IntegrationServiceRequest Request) {
		if ( Request != null ) {
			if ( Request instanceof AuthenticationRequest && ((AuthenticationRequest)Request).getSecurity() != null ) {
				demo.model.UserSecurity sic = ((AuthenticationRequest)Request).getSecurity();
				if ( sic.getUID() == null || sic.getUID().isEmpty() ) {
					if ( sic.getPassword().equals("test") && sic.getUsername().equals("test") ) {
						sic = new demo.model.UserSecurity();
						sic.setUID("1");
						((AuthenticationRequest)Request).setSecurity(sic);
					}
					sic.setPassword(null);
				} else if ( !sic.getToken().equals("12345") ) {
					sic = new demo.model.UserSecurity();
					sic.setUID(null);
					((AuthenticationRequest)Request).setSecurity(sic);
				}
				return (IntegrationServiceResponse)Request;
			}
		}
		return null;
	}


}
