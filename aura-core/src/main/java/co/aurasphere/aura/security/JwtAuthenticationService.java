package co.aurasphere.aura.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.aurasphere.aura.security.dao.UserDao;
import co.aurasphere.aura.security.model.LoginRequest;
import co.aurasphere.aura.security.model.LoginResponse;
import co.aurasphere.aura.security.model.User;

/**
 * REST controller which handles the user login and returns the user's JWT
 * token.
 * 
 * @author Donato Rimenti
 * @date 13/mag/2016
 */

@Controller
public class JwtAuthenticationService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private JwtUtil jwt;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody LoginResponse login(@RequestBody LoginRequest request,
			HttpServletResponse servletResponse) {
		LoginResponse response = new LoginResponse();

		// Checks the credentials.
		User user = userDao.getUser(request.getUsername(),
				request.getPassword());
		if (user != null) {
			// If the user is found returns a positivo outcome with the JWT
			// token in the header.
			String token = jwt.generateToken(user);
			servletResponse.setHeader("Token", token);
			return response;
		}
		// Otherwise a negative outcome is returned.
		response.setOutcome(false);
		return response;

	}
}
