package com.pedro.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.pedro.cursomc.security.UserSS;

public class UserServices {
	
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch (Exception e) {
			return null;
		}
		
	}
}
