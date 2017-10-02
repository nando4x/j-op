package demo.integration_services.stub;

import demo.integration_services.IntegrationServiceDispatcher;
import demo.integration_services.IntegrationServiceRequest;
import demo.integration_services.IntegrationServiceResponse;
import demo.integration_services.auth.AuthenticationRequest;
import demo.integration_services.dataservice.IntegrationServiceDataContainer;
import demo.model.IntegrationServiceData;


public class DispatcherImpl implements IntegrationServiceDispatcher {

	@Override
	public <E extends IntegrationServiceData, K> IntegrationServiceDataContainer<E,K> servRequestDataRetrive(
			IntegrationServiceDataContainer<E,K> Request) {
		if ( Request != null ) {
			if ( Request.getData() instanceof demo.model.UserSecurity ) {
				DataInPropertyFile<E> dt = new DataInPropertyFile<E>("user");
				dt.readEntity("security.test", (E)Request.getData());
				return Request;
			}
			if ( Request.getData() instanceof demo.model.User ) {
				DataInPropertyFile<E> dt = new DataInPropertyFile<E>("user");
				dt.readEntity("user.test", (E)Request.getData());
				return Request;
			}
		}
		return null;
	}

	@Override
	public <E extends IntegrationServiceData, K> IntegrationServiceDataContainer<E, K> servRequestDataChange(
			IntegrationServiceDataContainer<E, K> Request) {
		if ( Request != null ) {
			if ( Request.getData() instanceof demo.model.UserSecurity ) {
				DataInPropertyFile<E> dt = new DataInPropertyFile<E>("user");
				dt.writeEntity("security.test", (E)Request.getData());
				return Request;
			}
			if ( Request.getData() instanceof demo.model.User ) {
				DataInPropertyFile<E> dt = new DataInPropertyFile<E>("user");
				dt.writeEntity("user.test", (E)Request.getData());
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
