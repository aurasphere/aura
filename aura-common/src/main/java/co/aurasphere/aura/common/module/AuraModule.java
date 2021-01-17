package co.aurasphere.aura.common.module;

import java.util.HashMap;

public interface AuraModule {
	
	public String getModuleName();
	
	public void registerServices(
			HashMap<String, OnDemandService> onDemandServiceMap,
			HashMap<String, ParallelService> parallelServiceMap);

}
