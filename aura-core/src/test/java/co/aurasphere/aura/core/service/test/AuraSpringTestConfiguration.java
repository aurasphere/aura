/**
 * 
 */
package co.aurasphere.aura.core.service.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import co.aurasphere.aura.core.service.ServiceHub;

/**
 * @author Donato Rimenti
 */
@Configuration

@Import(ServiceHub.class)
@ImportResource({ "classpath:**/applicationContext.xml",
	"classpath:**/dispatcherServlet.xml",
	"classpath:**/security-context.xml" })
public class AuraSpringTestConfiguration {

}
