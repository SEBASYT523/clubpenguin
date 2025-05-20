package co.edu.unbosque.backclubpenguin.configuration;

import co.edu.unbosque.backclubpenguin.repository.UsuarioRepository;
import co.edu.unbosque.backclubpenguin.util.AESUtil;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepo,
            BCryptPasswordEncoder passwordEncoder) {
        return args -> {
        
        };
    }
}
