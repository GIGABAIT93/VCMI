package ua.co.tensa.modules.rcon.manager;

public class AuthenticationException extends Exception {
	private static final long serialVersionUID = 1L;

	public AuthenticationException(String message) {
		super(message);
	}
}