package co.aurasphere.aura.core.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import co.aurasphere.aura.common.module.OnDemandService;
import co.aurasphere.aura.common.module.ParallelService;
import co.aurasphere.aura.common.module.dto.ServiceRequest;
import co.aurasphere.aura.common.module.dto.ServiceResponse;

@Service
public class ServiceHub {

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceHub.class);

	private HashMap<String, OnDemandService> onDemandServicesMap;

	private HashMap<String, ParallelService> parallelServicesMap;

	public ServiceHub() {
		this.onDemandServicesMap = new HashMap<String, OnDemandService>();
		this.parallelServicesMap = new HashMap<String, ParallelService>();
	}

	public ServiceResponse dispatch(ServiceRequest request) {

		String serviceName = request.getServiceName();
		OnDemandService service = onDemandServicesMap.get(serviceName);
		if (service == null) {
			logger.warn("Service with name " + serviceName + " not found.");
			return new ServiceResponse(false, "Service with name "
					+ serviceName + " not found.");
		}
		return service.run(request);
	}

	public HashMap<String, OnDemandService> getOnDemandServicesMap() {
		return onDemandServicesMap;
	}

	public void setOnDemandServicesMap(
			HashMap<String, OnDemandService> onDemandServicesMap) {
		this.onDemandServicesMap = onDemandServicesMap;
	}

	public HashMap<String, ParallelService> getParallelServicesMap() {
		return parallelServicesMap;
	}

	public void setParallelServicesMap(
			HashMap<String, ParallelService> parallelServicesMap) {
		this.parallelServicesMap = parallelServicesMap;
	}
	
	@Bean
	public ServiceBusImpl getServiceBus() {
		return new ServiceBusImpl(this);
	}

}
