package co.aurasphere.aura.security;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Autowired
	private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;

	@Autowired
	private ServletContext servletContext;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(jwtAuthenticationProvider);
// TODO
	}

	@Override
	    protected void configure(HttpSecurity http) throws Exception { 
		
		 http
	        .csrf().disable()
	        .exceptionHandling()
	        .authenticationEntryPoint(restAuthenticationEntryPoint)
	        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/rest/**").authenticated()
            .antMatchers("/rest/login").permitAll();
//	        http
//	        .csrf().disable()
//	        .exceptionHandling()
//	    	.authenticationEntryPoint(restAuthenticationEntryPoint)
//	        .and().authorizeRequests().
//	        anyRequest().authenticated().and()
//	        .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
//	    	http.antMatcher("/rest/login").securityContext().disable();	   
	    	    }

	@Bean
	public RestAuthenticationSuccessHandler mySuccessHandler() {
		return new RestAuthenticationSuccessHandler();
	}

	@Bean
	public SimpleUrlAuthenticationFailureHandler myFailureHandler() {
		return new SimpleUrlAuthenticationFailureHandler();
	}

	// @Bean
	// public DelegatingFilterProxy springSecurityFilterChain(){
	// DelegatingFilterProxy filter = new DelegatingFilterProxy();
	// servletContext.addFilter("springSecurityFilterChain", filter);
	// return filter;
	// }

}
