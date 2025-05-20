package co.edu.unbosque.backclubpenguin.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.backclubpenguin.dto.UsuarioDTO;
import co.edu.unbosque.backclubpenguin.exception.ExceptionChecker;
import co.edu.unbosque.backclubpenguin.exception.NotValidEmailException;
import co.edu.unbosque.backclubpenguin.exception.NotValidPasswordException;
import co.edu.unbosque.backclubpenguin.exception.NotValidUsernameException;
import co.edu.unbosque.backclubpenguin.model.Usuario;
import co.edu.unbosque.backclubpenguin.model.Usuario.Role;
import co.edu.unbosque.backclubpenguin.security.JwtUtil;
import co.edu.unbosque.backclubpenguin.service.UsuarioService;
import co.edu.unbosque.backclubpenguin.util.AESUtil;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = { "http://localhost:8080", "http://localhost:8081", "http://localhost:4200" })

public class AuthController {

	private final AuthenticationManager authenticationManager;

	private final JwtUtil jwtUtil;

	private final UsuarioService userService;

	public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UsuarioDTO loginRequest) {
		loginRequest = new UsuarioDTO(loginRequest.getUsername(), loginRequest.getPassword());
		String username = AESUtil.encrypt(loginRequest.getUsername());
		UsuarioDTO validado = userService.buscarPorUsername(username);
		if (validado.isEmailVerified() == false || validado == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("message","Nombre de usuario o contraseña inválidos o usuario no encontrado o correo no validado"));
		}
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String jwt = jwtUtil.generateToken(userDetails);

			String role = null;
			if (userDetails instanceof Usuario) {
				Usuario user = (Usuario) userDetails;
				role = user.getRole().name();
			}

			return ResponseEntity.ok(new AuthResponse(jwt, role));
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("message","Nombre de usuario o contraseña inválidos o usuario no encontrado o correo no validado"));
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UsuarioDTO registerRequest) {

		if (registerRequest == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al crear el usuario"));
		}
		try {
			ExceptionChecker.notValidPasswordException(registerRequest.getPassword());
			ExceptionChecker.notValidUsernameException(registerRequest.getUsername());
			ExceptionChecker.validateEmail(registerRequest.getEmail());
		} catch (NotValidPasswordException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Digite una contraseña valida"));
		} catch (NotValidUsernameException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Digite un usuario valido"));

		} catch (NotValidEmailException e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("message","Digite una direccion de correo valida"));

		}
		registerRequest = new UsuarioDTO(registerRequest.getUsername(), registerRequest.getPassword(),
				registerRequest.getEmail(), registerRequest.getColor(), false, Role.USER);

		if (userService.findUsernameAlreadyTaken(registerRequest.getUsername())) {
			return ResponseEntity
		            .status(HttpStatus.CONFLICT)
		            .body(Map.of("message", "El nombre de usuario ya está en uso"));		}
		String token = jwtUtil.generateEmailToken(registerRequest.getUsername());
		int status = userService.create(registerRequest);
		if (status == 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Usuario creado exitosamente. Valida el correo "));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al crear el usuario"));
	}
	
	@GetMapping("/validate")
	public ResponseEntity<?> validateAccount(@RequestParam("token") String token) {
		boolean success = userService.validarCorreo(token);
		if (success) {
			return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","Cuenta validada correctamente. Ya puedes iniciar sesión."));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message","Token inválido o expirado, o usuario no encontrado."));
		}
	}

	private static class AuthResponse {
		private final String token;

		private final String role;

		public AuthResponse(String token) {
			this.token = token;
			this.role = null;
		}

		public AuthResponse(String token, String role) {
			this.token = token;
			this.role = role;
		}

		public String getToken() {
			return token;
		}

		public String getRole() {
			return role;
		}
	}
}
