package co.edu.unbosque.backclubpenguin.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import co.edu.unbosque.backclubpenguin.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
