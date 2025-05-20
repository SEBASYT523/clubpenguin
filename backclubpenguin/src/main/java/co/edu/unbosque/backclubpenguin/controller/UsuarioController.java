package co.edu.unbosque.backclubpenguin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.backclubpenguin.dto.UsuarioDTO;
import co.edu.unbosque.backclubpenguin.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import co.edu.unbosque.backclubpenguin.util.AESUtil;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "http://localhost:8080", "http://localhost:8081", "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioServ;

	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createNewWithJSON(@RequestBody UsuarioDTO newUser) {
		if (newUser == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error al crear el usuario"));

		}
		if (usuarioServ.findUsernameAlreadyTaken(newUser.getUsername())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("message", "El nombre de usuario ya está en uso"));
		}
		int status = usuarioServ.create(newUser);
		if (status == 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Usuario creado exitosamente"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error al crear el usuario"));
	}

	@GetMapping("/getall")
	public ResponseEntity<List<UsuarioDTO>> getAll() {
		List<UsuarioDTO> users = usuarioServ.getAll();
		return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
	}

	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateNewWithJSON(@RequestParam String username, @RequestBody UsuarioDTO newUser) {

		newUser = new UsuarioDTO(newUser.getUsername(), newUser.getPassword(), newUser.getEmail(), newUser.getColor(),
				newUser.isEmailVerified(), newUser.getRole());
		username = AESUtil.encrypt(username);
		int status = usuarioServ.updateByUsername(username, newUser);
		return switch (status) {
		case 0 -> ResponseEntity.status(HttpStatus.OK).body(Map.of("messages", "Usuario actualizado exitosamente"));
		case 1 -> ResponseEntity.status(HttpStatus.IM_USED)
				.body(Map.of("message", "El nuevo nombre de usuario ya está en uso"));
		case 2 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Usuario no encontrado"));
		default -> ResponseEntity.badRequest().body(Map.of("message", "Error al actualizar"));
		};
	}

	@DeleteMapping("/deletebyname")
	public ResponseEntity<?> deleteByName(@RequestParam String name) {
		return usuarioServ.deleteByUsername(name) == 0
				? ResponseEntity.status(HttpStatus.OK).body(Map.of("messages", "Usuario eliminado exitosamente"))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Usuario no encontrado"));
	}

	@PutMapping(path = "/password")
	public ResponseEntity<?> updatePassword(@RequestParam String username, @RequestParam String mail) {
		if (username == null || mail == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error al enviar contraseña"));

		}
		if (!usuarioServ.findUsernameAlreadyTaken(username)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "No existe el correo/usuario"));
		}

		int status = usuarioServ.cambiarContrasenia(username, mail);

		return switch (status) {
		case 0 -> ResponseEntity.status(HttpStatus.OK).body(Map.of("messages", "Contraseña en tu correo"));
		case 1 -> ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", "Usuario/Correo no encontrado"));
		case 2 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No coincide la informacion"));
		default -> ResponseEntity.badRequest().body(Map.of("message", "Error al crear contraseña"));
		};
	}

}
