package co.aurasphere.aura.core.service.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import co.aurasphere.aura.common.dto.base.AuraBaseRequest;
import co.aurasphere.aura.common.dto.base.AuraBaseResponse;

/**
 * Utility services used for testing.
 * @author Donato Rimenti
 * @date 13/mag/2016
 */

@Controller
@RequestMapping("/diagnose")
public class DiagnoseServices {
	
	@RequestMapping("/ping")
	public @ResponseBody AuraBaseResponse ping(@RequestBody AuraBaseRequest request){
		return new AuraBaseResponse();
	}

}
