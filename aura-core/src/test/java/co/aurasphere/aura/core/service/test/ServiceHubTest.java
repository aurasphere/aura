package co.aurasphere.aura.core.service.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.aurasphere.aura.AuraInitializer;
import co.aurasphere.aura.common.module.AuraModule;
import co.aurasphere.aura.common.module.OnDemandService;
import co.aurasphere.aura.common.module.ParallelService;
import co.aurasphere.aura.common.module.dto.ServiceRequest;
import co.aurasphere.aura.common.module.dto.ServiceResponse;
import co.aurasphere.aura.core.service.ServiceHub;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AuraSpringTestConfiguration.class, initializers = AuraInitializer.class)
public class ServiceHubTest {

	@Autowired
	private ApplicationContext context;

	private ServiceHub service;

	@Before
	public void setup() {
		service = context.getAutowireCapableBeanFactory().createBean(
				ServiceHub.class);
	}

	public void moduleLoader() {

		Map<String, AuraModule> modules = context
				.getBeansOfType(AuraModule.class);
		Set<String> beanNames = modules.keySet();

		ServiceHub serviceHub = context.getBean(ServiceHub.class);
		HashMap<String, OnDemandService> onDemandServiceMap = serviceHub
				.getOnDemandServicesMap();
		HashMap<String, ParallelService> parallelServiceMap = serviceHub
				.getParallelServicesMap();
		for (String s : beanNames) {
			modules.get(s).registerServices(onDemandServiceMap,
					parallelServiceMap);
		}

		// Starts the parallel services.
		Set<String> parallelServiceNames = parallelServiceMap.keySet();
		ParallelService service = null;
		for (String s : parallelServiceNames) {
			service = parallelServiceMap.get(s);
			service.setServiceBus(serviceHub.getServiceBus());
			service.start();
		}
	}

	@Test
	@Ignore
	public void testServiceNotFound() {
		ServiceRequest request = new ServiceRequest("udt");
		request.setServiceName("test");
		ServiceResponse response = service.dispatch(request);
		System.out.println(response.getMessage());
	}

	@Test
	public void testServiceFound() {
		moduleLoader();
		ServiceRequest request = new ServiceRequest("udt");
		request.setServiceName("udt");
		ServiceResponse response = service.dispatch(request);
		System.out.println(response.getMessage());
	}

}
