package co.aurasphere.aura.common.module.dto;

import co.aurasphere.aura.common.dto.base.AuraBaseResponse;

public class ServiceResponse extends AuraBaseResponse{

	private static final long serialVersionUID = 1L;
	
	private String message;
	
	private String result;
	
	public ServiceResponse(){}

	public ServiceResponse(boolean outcome, String message){
		this.message = message;
		this.outcome = outcome;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
