package co.edu.unbosque.backclubpenguin.exception;

public class NotValidStringIntException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotValidStringIntException() {
		super("\"Tiene que haber un numero o letras");
	}

}
