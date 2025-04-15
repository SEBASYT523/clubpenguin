package co.edu.unbosque.backclubpenguin.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.backclubpenguin.model.Usuario;
import co.edu.unbosque.backclubpenguin.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> optUsuario = usuarioRepository.findByUsername(username);
        
        if (!optUsuario.isPresent()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        
        Usuario usuario = optUsuario.get();
        
        // Con Spring Security, podemos construir un objeto User que implemente UserDetails.
        // Si en el futuro deseas incluir roles o authorities, se pueden agregar en el ArrayList.
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword()) // La contraseña ya se encuentra encriptada
                .authorities(new ArrayList<>())
                .build();
    }
}
