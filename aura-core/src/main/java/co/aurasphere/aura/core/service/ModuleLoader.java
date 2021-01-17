package co.aurasphere.aura.core.service;

import java.util.HashMap;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import co.aurasphere.aura.common.module.AuraModule;
import co.aurasphere.aura.common.module.OnDemandService;
import co.aurasphere.aura.common.module.ParallelService;

/**
 * Loads all the modules implementing {@link AuraModule}.
 */
@Component
public class ModuleLoader implements ApplicationListener<ContextRefreshedEvent> {

	@Inject
	private AuraModule[] modules;
	
	@Inject
	private ServiceHub serviceHub;
	
	@Inject
	private ParallelService[] parallelServices;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		// Register all the services in a map for each module.
		HashMap<String, OnDemandService> onDemandServiceMap = serviceHub
				.getOnDemandServicesMap();
		HashMap<String, ParallelService> parallelServiceMap = serviceHub
				.getParallelServicesMap();
		for (AuraModule m : modules) {
			m.registerServices(onDemandServiceMap,
					parallelServiceMap);
		}

		// Starts the parallel services.
		for (ParallelService service : parallelServices) {
			service.setServiceBus(serviceHub.getServiceBus());
			service.start();
		}
	}

}
