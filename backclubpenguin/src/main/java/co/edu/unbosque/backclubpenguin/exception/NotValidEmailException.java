package co.edu.unbosque.backclubpenguin.exception;

public class NotValidEmailException extends Exception {

   
	private static final long serialVersionUID = 1L;

	public NotValidEmailException(String message) {
        super(message);
    }

    public NotValidEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
