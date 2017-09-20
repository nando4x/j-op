package demo.integration_services.generalData;

import demo.integration_services.*;
import demo.model.IntegrationServiceData;

public abstract class IntgrationServiceRetriveData<D extends IntegrationServiceData> implements IntegrationServiceManager<IntegrationServiceDataContainer<D>,IntegrationServiceDataContainer<D>> {

	@Override
	public abstract IntegrationServiceDataContainer<D> servRequest(IntegrationServiceDataContainer<D> Request);

}
