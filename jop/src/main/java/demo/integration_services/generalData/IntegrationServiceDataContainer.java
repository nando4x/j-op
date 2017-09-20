package demo.integration_services.generalData;

import demo.integration_services.*;
import demo.model.IntegrationServiceData;

public class IntegrationServiceDataContainer<D extends IntegrationServiceData> implements IntegrationServiceRequest, IntegrationServiceResponse {
	private D data;
	
	public D getData() {
		return data;
	}
	public void setData(D Data) {
		this.data = Data;
	}
	
}
