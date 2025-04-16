package co.edu.unbosque.backclubpenguin.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
	@NotBlank(message = "El nombre de usuario es obligatorio")
	private String username;

	@NotBlank(message = "La contraseña es obligatoria")
	private String password;

	public LoginDTO() {
		// TODO Auto-generated constructor stub
	}

	public LoginDTO(@NotBlank(message = "El nombre de usuario es obligatorio") String username,
			@NotBlank(message = "La contraseña es obligatoria") String password) {
		super();
		this.username = username;
		this.password = password;
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

}
