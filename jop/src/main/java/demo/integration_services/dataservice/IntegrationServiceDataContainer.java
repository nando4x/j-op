package demo.integration_services.dataservice;

import demo.integration_services.*;
import demo.model.IntegrationServiceData;

public class IntegrationServiceDataContainer<D extends IntegrationServiceData, K> implements IntegrationServiceRequest, IntegrationServiceResponse {
	private D data;
	private K key;
	
	public D getData() {
		return data;
	}
	public void setData(D Data) {
		this.data = Data;
	}
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	
}
