package co.aurasphere.aura.common.module;

import org.springframework.stereotype.Service;

import co.aurasphere.aura.common.module.dto.ServiceRequest;
import co.aurasphere.aura.common.module.dto.ServiceResponse;

@Service
public interface ServiceBus {
	
	public ServiceResponse requestService(ServiceRequest request);

}
