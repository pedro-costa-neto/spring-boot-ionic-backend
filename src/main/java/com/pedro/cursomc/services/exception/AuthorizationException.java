package com.pedro.cursomc.services.exception;

public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AuthorizationException( String msg ) {
		super( msg );
	}
	
	public AuthorizationException( String msg, Throwable ceuse ) {
		super( msg, ceuse );
	}
}
