package demo.integration_services;

import demo.model.IntegrationServiceData;
import demo.integration_services.generalData.IntegrationServiceDataContainer;

public interface IntegrationServiceDispatcher {
		public <E extends IntegrationServiceData> IntegrationServiceDataContainer<E> servRequestDataRetrive(IntegrationServiceDataContainer<E> Request);
		public IntegrationServiceResponse servRequest(IntegrationServiceRequest Request);
}
