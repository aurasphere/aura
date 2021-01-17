package co.aurasphere.aura.udt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.aurasphere.aura.common.module.OnDemandService;
import co.aurasphere.aura.common.module.dto.ServiceRequest;
import co.aurasphere.aura.common.module.dto.ServiceResponse;
import co.aurasphere.jyandex.Jyandex;

@Service
public class UdtService implements OnDemandService{
	
	private static final Logger logger = LoggerFactory.getLogger(UdtService.class);

	// XXX: Removed
	private String API_KEY;
	
	private Jyandex client;	
	
	public UdtService(){
		client = new Jyandex(API_KEY);
	}
	
	public String getServiceName() {
		return "UDT";
	}

	public ServiceResponse run(ServiceRequest request) {
		String udtText = request.getArguments().get("text");
		if(udtText == null) {
			return new ServiceResponse(false, "Text can't be null!");
		} 

//		udtText = StringUtils.mergeStringArray(client.translateText(udtText, "ja").getText(), false);		
//		udtText = StringUtils.mergeStringArray(client.translateText(udtText, "it").getText(), false);
		ServiceResponse response = new ServiceResponse();
		response.setResult(udtText);
		return response;
	}

}
