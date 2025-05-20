/**
 * Esta la clase Util, en la cual creamos nuestras excepciones propias, para poder limitar los errores que pueda generar el usuario.
 */
package co.edu.unbosque.backclubpenguin.exception;

import java.util.regex.Matcher; // Quien busca si hay similitud
import java.util.regex.Pattern;//Nos da el patron a buscar

/**
 * Clase utilitaria para verificar la validez de entradas de datos y lanzar
 * excepciones personalizadas.
 */
public class ExceptionChecker {

	public static void notValidPasswordException(String pass) throws NotValidPasswordException {

		if (pass == null || pass.isEmpty()) {
			throw new NotValidPasswordException();
		}
		String longitud = ".{12,}";
		String mayusculas = "(.*[A-Z].*){2,}";
		String numeros = "(.*[0-9].*){2,}";
		String especiales = "(.*[!@#?¿$%^&*()_+].*){2,}";

		if (!pass.matches(longitud) || !pass.matches(mayusculas) || !pass.matches(numeros)
				|| !pass.matches(especiales)) {
			throw new NotValidPasswordException();
		}
	}

	public static void notValidUsernameException(String username) throws NotValidUsernameException {
		if (username == null || username.isEmpty()) {
			throw new NotValidUsernameException("El usuario no puede estar vacío.");
		}
		String invalidChars = "&=<>\"' \n\t-!@#$%^*(){}[];:?,/\\|~.";

		for (char c : invalidChars.toCharArray()) {
			if (username.indexOf(c) != -1) {
				throw new NotValidUsernameException("El nombre de usuario contiene caracteres no válidos.");
			}
		}

		if (username.length() < 3 || username.length() > 20) {
			throw new NotValidUsernameException("El nombre de usuario debe tener entre 3 y 20 caracteres.");
		}
	}

	public static void notValidStringException(String txt) throws NotValidStringException {
		if (txt == null || txt.isEmpty()) {
			throw new NotValidStringException();
		}
		Pattern p = Pattern.compile("[^a-zA-ZñÑ ]");
		Matcher m = p.matcher(txt);

		if (m.find() || txt.trim().isEmpty()) {
			throw new NotValidStringException();
		}
	}

	public static void notValidCedulaNumberException(String phoneNumber) throws NotValidCedulaNumberException {
		if (phoneNumber == null || phoneNumber.isEmpty()) {
			throw new NotValidCedulaNumberException();
		}
		String regex = "^[0-9]{7,15}$";

		if (!Pattern.matches(regex, phoneNumber)) {
			throw new NotValidCedulaNumberException();
		}
	}

	public static void notValidStringIntException(String text) throws NotValidStringIntException {
		if (text == null || text.isEmpty()) {
			throw new NotValidStringIntException();
		}
		Pattern p = Pattern.compile("[^a-zA-ZñÑ0-9\\s,\\.\\-#]");
		Matcher m = p.matcher(text);

		if (m.find() || text == null || text.trim().isEmpty()) {
			throw new NotValidStringIntException();
		}
	}

	public static void validateEmail(String email) throws NotValidEmailException {

		String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		Pattern pattern = Pattern.compile(regex);

		if (email == null || email.isEmpty()) {
			throw new NotValidEmailException("El correo electrónico no puede estar vacío.");
		}

		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new NotValidEmailException("El correo electrónico '" + email + "' no tiene un formato válido.");
		}
	}
}
