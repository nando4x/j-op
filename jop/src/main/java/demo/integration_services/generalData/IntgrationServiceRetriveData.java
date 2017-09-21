package demo.integration_services.generalData;

import demo.integration_services.*;
import demo.model.IntegrationServiceData;

public class IntgrationServiceRetriveData<E extends IntegrationServiceData> implements IntegrationServiceManager<IntegrationServiceDataContainer<E>,IntegrationServiceDataContainer<E>> {
	private IntegrationServiceDispatcher dsp;
	
	public IntgrationServiceRetriveData(IntegrationServiceDispatcher Dsp) {
		this.dsp = Dsp;
	}
	public IntegrationServiceDataContainer<E> servRequest(IntegrationServiceDataContainer<E> Request) {
		return this.dsp.<E>servRequestDataRetrive(Request);
	}

}
