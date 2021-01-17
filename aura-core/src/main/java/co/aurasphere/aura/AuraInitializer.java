package co.aurasphere.aura;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Class which initializes Spring by setting the environment to the %Aura_ENV%
 * enviroment variable.
 * 
 * @author Donato Rimenti
 *
 */
public class AuraInitializer implements
		ApplicationContextInitializer<ConfigurableApplicationContext> {
	
	private static final Logger log = LoggerFactory
			.getLogger(AuraInitializer.class);

	private static final String ENV_VAR_NAME = "Aura_ENV";

	private static final String DEFAULT_ENV = "PRO";

	// TODO: dynamic env switch.
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		ConfigurableEnvironment environment = applicationContext
				.getEnvironment();
		String profile = System.getenv(ENV_VAR_NAME);
		// If no environment is configured then the default is set.
		if (profile == "" || profile == null) {
			profile = DEFAULT_ENV;
		}
		environment.setActiveProfiles(profile);
		// TimeZone.setDefault(TimeZone.getTimeZone("Europe/Rome"));
		log.info("Running on environment: " + profile);

	}

}