package co.aurasphere.aura.core.service;

import co.aurasphere.aura.common.module.ServiceBus;
import co.aurasphere.aura.common.module.dto.ServiceRequest;
import co.aurasphere.aura.common.module.dto.ServiceResponse;

public class ServiceBusImpl implements ServiceBus {

	private ServiceHub serviceHub;

	public ServiceBusImpl(ServiceHub serviceHub) {
		this.serviceHub = serviceHub;
	}

	@Override
	public ServiceResponse requestService(ServiceRequest request) {
		return serviceHub.dispatch(request);
	}

}
