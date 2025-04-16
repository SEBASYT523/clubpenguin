package co.edu.unbosque.backclubpenguin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "usuarios")

public class Usuario {
    @Id
    private String id;
    
    private String username;
    private String password; // Se recomienda almacenar la contraseña encriptada.
    private String email;
    private String color; // Color del avatar o pinguino
    private boolean emailVerified = false; // Por defecto, en false hasta validar el correo

    // Puedes agregar campos adicionales, como fecha de creación, roles, etc.
    public Usuario() {
		// TODO Auto-generated constructor stub
	}

    
	public Usuario( String username, String password, String email, String color, boolean emailVerified) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.color = color;
		this.emailVerified = emailVerified;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
    
}
