package co.aurasphere.aura.security.model;

import co.aurasphere.aura.common.dto.base.AuraBaseRequest;

public class LoginRequest extends AuraBaseRequest{
	
	private static final long serialVersionUID = 1L;

	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
