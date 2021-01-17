package co.aurasphere.aura.udt;

import java.util.HashMap;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import co.aurasphere.aura.common.module.AuraModule;
import co.aurasphere.aura.common.module.OnDemandService;
import co.aurasphere.aura.common.module.ParallelService;
import co.aurasphere.aura.udt.UdtService;

@Service
public class UdtModule implements AuraModule {
	
	@Inject
	private UdtService udtService;
	
	public void registerServices(
			HashMap<String, OnDemandService> onDemandServiceMap,
			HashMap<String, ParallelService> parallelServiceMap) {
		onDemandServiceMap.put("udt", udtService);
	}

	public String getModuleName() {
		return "Udt";
	}

}
