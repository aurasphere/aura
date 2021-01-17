package co.aurasphere.aura.social;

import java.util.HashMap;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import co.aurasphere.aura.common.module.AuraModule;
import co.aurasphere.aura.common.module.OnDemandService;
import co.aurasphere.aura.common.module.ParallelService;
import co.aurasphere.aura.social.facebook.FacebookParallelService;

@Service
public class SocialModule implements AuraModule{

	@Inject
	private FacebookParallelService facebookParallelService;

	public void registerServices(
			HashMap<String, OnDemandService> onDemandServiceMap,
			HashMap<String, ParallelService> parallelServiceMap) {
		parallelServiceMap.put("facebook", facebookParallelService);
	}

	public String getModuleName() {
		return "Social";
	}

}
