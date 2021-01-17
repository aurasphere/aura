package co.aurasphere.aura.common.module;

import co.aurasphere.aura.common.module.dto.ServiceRequest;
import co.aurasphere.aura.common.module.dto.ServiceResponse;

public interface OnDemandService extends AuraService{
	
	public ServiceResponse run(ServiceRequest stringRequest);
	
}
