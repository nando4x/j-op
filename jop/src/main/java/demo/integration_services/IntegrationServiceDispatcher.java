package demo.integration_services;

import demo.integration_services.dataservice.IntegrationServiceDataContainer;
import demo.model.IntegrationServiceData;

public interface IntegrationServiceDispatcher {
		public <E extends IntegrationServiceData, K> IntegrationServiceDataContainer<E,K> servRequestDataRetrive(IntegrationServiceDataContainer<E,K> Request);
		public <E extends IntegrationServiceData, K> IntegrationServiceDataContainer<E,K> servRequestDataChange(IntegrationServiceDataContainer<E,K> Request);
		public IntegrationServiceResponse servRequest(IntegrationServiceRequest Request);
}
