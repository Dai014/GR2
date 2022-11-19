package com.rabiloo.custom.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {

	public static boolean isAuthenticated() {

		Authentication authentication = getAuthentication();
		
		if (authentication == null) {
			return false;
		}
		return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();

	}
	
	public static Authentication getAuthentication(){
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
