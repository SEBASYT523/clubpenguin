package co.edu.unbosque.backclubpenguin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRegistrationDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
    
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo válido")
    private String email;
    
    @NotBlank(message = "El color del avatar es obligatorio")
    private String color;
    
    public UserRegistrationDTO() {
		// TODO Auto-generated constructor stub
	}

	public UserRegistrationDTO(@NotBlank(message = "El nombre de usuario es obligatorio") String username,
			@NotBlank(message = "La contraseña es obligatoria") String password,
			@NotBlank(message = "El correo es obligatorio") @Email(message = "Debe ser un correo válido") String email,
			@NotBlank(message = "El color del avatar es obligatorio") String color) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.color = color;
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
    
}
