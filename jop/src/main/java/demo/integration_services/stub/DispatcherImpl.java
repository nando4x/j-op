package demo.integration_services.stub;

import demo.integration_services.IntegrationServiceDispatcher;
import demo.integration_services.generalData.IntgrationServiceRetriveData;
import demo.model.IntegrationServiceData;
import demo.model.UserSecurity;

public class DispatcherImpl implements IntegrationServiceDispatcher {

	@Override
	public IntgrationServiceRetriveData<? extends IntegrationServiceData> getDataRetriver(
			Class<? extends IntegrationServiceData> clazz) {
		try {
			IntgrationServiceRetriveData i = null;
			return i;
		} catch (Exception e) {
			return null;
		}
	}

}
