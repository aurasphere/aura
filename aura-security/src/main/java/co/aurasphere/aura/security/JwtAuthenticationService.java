package co.aurasphere.aura.security;

import java.util.List;

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

@Controller
public class JwtAuthenticationService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private JwtUtil jwt;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse servletResponse){
		
		// Hibernate query DB check user. Password hashed.
		User user = userDao.getUser(request.getUsername(), request.getPassword());
		List<User> users = userDao.readAll();
		System.out.println(users.get(0).getPassword());
		if(user != null){
			String token = jwt.generateToken(user);
			System.out.println(token);
			servletResponse.setHeader("Token", token);
			return new LoginResponse();
		}
		return null;
		
	}
}
