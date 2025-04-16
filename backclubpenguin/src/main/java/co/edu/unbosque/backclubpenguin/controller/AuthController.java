package co.edu.unbosque.backclubpenguin.controller;

import co.edu.unbosque.backclubpenguin.dto.UserRegistrationDTO;
import co.edu.unbosque.backclubpenguin.dto.LoginDTO;
import co.edu.unbosque.backclubpenguin.model.Usuario;
import co.edu.unbosque.backclubpenguin.service.JWTService;
import co.edu.unbosque.backclubpenguin.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JWTService jwtService;

    // Endpoint de registro
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(registrationDTO);
            return ResponseEntity.ok("Registro exitoso. Revisa tu correo para validar tu cuenta.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para validar el correo (ejemplo simplificado)
    @GetMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestParam("token") String token) {
        boolean validado = usuarioService.validarCorreo(token);
        if(validado){
            return ResponseEntity.ok("Cuenta validada exitosamente. Ahora puedes iniciar sesión.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido o caducado.");
    }
    
    // Endpoint de login (simplificado)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        Optional<Usuario> userOpt = usuarioService.buscarPorUsername(loginDTO.getUsername());
        if(userOpt.isPresent()){
            Usuario user = userOpt.get();
            // Aquí validar la contraseña (usando el BCryptPasswordEncoder)
            // Y además asegurarse de que el usuario haya validado su correo.
            // Si todo es correcto, generar y retornar el token JWT.
            if (!user.isEmailVerified()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correo no verificado.");
            }
            // Validación de contraseña: (esto se debe hacer usando passwordEncoder.matches(...))
            // Por ejemplo:
            // if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) { ... }
            
            // Supongamos que está correcta, generamos el token:
            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas.");
    }
}
