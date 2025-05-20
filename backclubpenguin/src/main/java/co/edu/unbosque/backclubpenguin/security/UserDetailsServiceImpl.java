package co.edu.unbosque.backclubpenguin.security;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.backclubpenguin.model.Usuario;
import co.edu.unbosque.backclubpenguin.repository.UsuarioRepository;
import co.edu.unbosque.backclubpenguin.util.AESUtil;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UsuarioRepository userRepository;

	public UserDetailsServiceImpl(UsuarioRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		String encrypted = AESUtil.encrypt(username);
		Usuario user = userRepository.findByUsername(encrypted)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no hallado: " + username));

		user.setUsername(username);
		user.setEmail(AESUtil.decrypt(user.getEmail()));
		return user;
	}
}
