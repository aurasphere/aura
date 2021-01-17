package co.aurasphere.aura.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Entry point for a REST request. By default, when a user tries to access to a
 * secured resource without being logged, Spring Security redirects it to the
 * login page. However, this doesn't make sense for a REST Web-Service. This
 * EntryPoint, returns a 401 UNAUTHORIZED response instead of redirecting.
 * 
 * @author Donato Rimenti
 * @date 06/mag/2016
 */

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest arg0, HttpServletResponse arg1,
			AuthenticationException arg2) throws IOException, ServletException {
		arg1.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}

}
