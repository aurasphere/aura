package co.aurasphere.aura.common.module.dto;

import java.util.HashMap;

import co.aurasphere.aura.common.dto.base.AuraBaseRequest;

public class ServiceRequest extends AuraBaseRequest{

	private static final long serialVersionUID = 1L;

	private String serviceName;
	
	private HashMap<String, String> arguments;

	public ServiceRequest(String serviceName) {
		this.serviceName = serviceName;
	}

	public ServiceRequest(String serviceName, HashMap<String, String> arguments) {
		this.serviceName = serviceName;
		this.arguments = arguments;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public HashMap<String, String> getArguments() {
		return arguments;
	}

	public void setArguments(HashMap<String, String> arguments) {
		this.arguments = arguments;
	}
	
}
