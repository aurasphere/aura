package co.aurasphere.aura.security.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUser implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private long id;
	
	private Collection<GrantedAuthority> authorities;
	
	private String password;
	
	private String username;
	
	private String token;
	
	private boolean accountNonExpired;
	
	private boolean credentialsNonExpired;
	
	private boolean accountNonLocked;
	
	private boolean enabled;

	public AuthenticatedUser(long id, String username, String token,
			List<GrantedAuthority> authorityList) {
		this.setId(id);
		this.username = username;
		this.setToken(token);
		this.authorities = authorityList;
		this.accountNonExpired = true;
		this.credentialsNonExpired = true;
		this.accountNonLocked = true;
		this.enabled = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
