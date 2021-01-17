package co.aurasphere.aura.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.stereotype.Component;

@Configurable
public class JwtAuthenticationManager extends ProviderManager{
	
	public JwtAuthenticationManager(JwtAuthenticationProvider provider) {
		super(AuraUtils.makeList(provider));
		System.out.println("Provider. " + provider);
	}
}
