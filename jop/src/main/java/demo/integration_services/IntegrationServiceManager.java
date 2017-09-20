package demo.integration_services;

public interface IntegrationServiceManager<I extends IntegrationServiceRequest, O extends IntegrationServiceResponse> {
	public O servRequest(I Request);  
}
