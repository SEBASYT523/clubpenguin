package co.edu.unbosque.backclubpenguin.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.backclubpenguin.dto.UserRegistrationDTO;
import co.edu.unbosque.backclubpenguin.model.Usuario;
import co.edu.unbosque.backclubpenguin.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // Asegúrate de definir un bean en SecurityConfig

    // Registro de usuario
    public Usuario registrarUsuario(UserRegistrationDTO registroDTO) throws Exception {
        if(usuarioRepository.existsByUsername(registroDTO.getUsername())) {
            throw new Exception("El nombre de usuario ya existe");
        }
        if(usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new Exception("El correo ya está registrado");
        }
        
        Usuario usuario = new Usuario();
        usuario.setUsername(registroDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword())); // Encriptar contraseña
        usuario.setEmail(registroDTO.getEmail());
        usuario.setColor(registroDTO.getColor());
        usuario.setEmailVerified(false);

        // Guardamos el usuario en MongoDB
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Enviar correo de validación
        emailService.sendValidationEmail(usuarioGuardado);
        
        return usuarioGuardado;
    }

    // Método para validar el correo a partir de un token (puedes implementar una lógica de token simple o con JWT)
    public boolean validarCorreo(String token) {
        // La lógica podría ser: decodificar el token para obtener el ID del usuario
        // y actualizar el campo emailVerified a true.
        // Se recomienda usar JWTService para crear y validar el token.
        
        // Ejemplo simplificado:
        Optional<Usuario> optUsuario = usuarioRepository.findById(token); // token = id en este ejemplo
        if(optUsuario.isPresent()){
            Usuario user = optUsuario.get();
            user.setEmailVerified(true);
            usuarioRepository.save(user);
            return true;
        }
        return false;
    }
    
    // Método de login (validación de usuario y contraseña) se implementará en conjunto con JWTService
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}
