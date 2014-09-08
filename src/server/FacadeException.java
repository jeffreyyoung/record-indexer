package server;

public class FacadeException extends Exception {

	public FacadeException() {
		return;
	}

	public FacadeException(String message) {
		super(message);
	}

	public FacadeException(Throwable cause) {
		super(cause);
	}

	public FacadeException(String message, Throwable cause) {
		super(message, cause);
	}

}
