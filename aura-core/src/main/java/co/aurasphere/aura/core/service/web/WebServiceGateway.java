package co.aurasphere.aura.core.service.web;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import co.aurasphere.aura.common.module.dto.ServiceRequest;
import co.aurasphere.aura.common.module.dto.ServiceResponse;
import co.aurasphere.aura.core.service.ServiceHub;

@Controller
public class WebServiceGateway {
	
	@Inject
	private ServiceHub hub;

	@RequestMapping("/service")
	public @ResponseBody ServiceResponse service(@RequestBody ServiceRequest request){
		return hub.dispatch(request);
	}
	
}
