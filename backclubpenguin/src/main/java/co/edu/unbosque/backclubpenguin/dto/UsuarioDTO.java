package co.edu.unbosque.backclubpenguin.dto;

import co.edu.unbosque.backclubpenguin.model.Usuario.Role;

public class UsuarioDTO {

	private String id;

	private String username;
	private String password;
	private String email;
	private String color;
	private boolean emailVerified;
	private Role role;

	public UsuarioDTO() {

	}

	public UsuarioDTO(String username, String password) {
		this();
		this.username = username;
		this.password = password;

	}

	public UsuarioDTO(String username, String password, String email, String color, boolean emailVerified, Role role) {

		this.username = username;
		this.password = password;
		this.email = email;
		this.color = color;
		this.emailVerified = emailVerified;
		this.role = role;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
