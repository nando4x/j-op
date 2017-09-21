package demo.integration_services;

import demo.model.IntegrationServiceData;
import demo.integration_services.generalData.IntgrationServiceRetriveData;

public interface IntegrationServiceDispatcher {
		public IntgrationServiceRetriveData<? extends IntegrationServiceData> getDataRetriver(Class<? extends IntegrationServiceData> clazz);
}
