package co.edu.unbosque.backclubpenguin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.backclubpenguin.dto.UsuarioDTO;
import co.edu.unbosque.backclubpenguin.model.Usuario;
import co.edu.unbosque.backclubpenguin.repository.UsuarioRepository;
import co.edu.unbosque.backclubpenguin.security.JwtUtil;
import co.edu.unbosque.backclubpenguin.util.AESUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;

@Service
public class UsuarioService implements CRUDOperation<UsuarioDTO> {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private JwtUtil jwtUtil;

	public UsuarioService(UsuarioRepository usuarioRepository, EmailService emailService, ModelMapper modelMapper,
			JwtUtil jwtUtil) {
		this.usuarioRepository = usuarioRepository;
		this.emailService = emailService;
		this.modelMapper = modelMapper;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public int create(UsuarioDTO data) {
		if (findUsernameAlreadyTaken(data.getUsername())) {
			return 1;
		}
		Usuario entity = new Usuario();
		entity.setUsername(AESUtil.encrypt(data.getUsername()));
		entity.setPassword(AESUtil.hashinBCrypt(data.getPassword()));
		entity.setRole(data.getRole());
		entity.setEmail(AESUtil.encrypt(data.getEmail()));
		entity.setColor(data.getColor());
		entity.setEmailVerified(data.isEmailVerified());

		try {
			String token = jwtUtil.generateEmailToken(data.getUsername());
			emailService.sendValidationEmail(data, token);
			modelMapper.map(usuarioRepository.save(entity), UsuarioDTO.class);
			return 0;
		} catch (MessagingException mex) {
			return 1;
		} catch (JwtException e) {
			return 1;
		}
	}

	public boolean validarCorreo(String token) {
		try {
			Claims claims = jwtUtil.validateAndGetClaims(token);

			String username = claims.getSubject();
			String usernameAValidar = AESUtil.encrypt(username);

			Optional<Usuario> opt = usuarioRepository.findByUsername(usernameAValidar);
			if (opt.isEmpty()) {
				return false;
			}

			Usuario u = opt.get();
			u.setEmailVerified(true);
			usuarioRepository.save(u);
			return true;
		}  catch (ExpiredJwtException ex) {
	        String username;
	        try {
	            username = Jwts.parserBuilder()
	                           .setSigningKey(jwtUtil.getSigningKey())
	                           .build()
	                           .parseClaimsJws(token)
	                           .getBody()
	                           .getSubject();
	        } catch (Exception parseEx) {
	            return false;
	        }
	        String encryptedUsername = AESUtil.encrypt(username);
	        usuarioRepository.findByUsername(encryptedUsername)
	                         .ifPresent(usuarioRepository::delete);
	        return false;

	    } catch (JwtException ex) {
	        return false;
	    }

	}

	public UsuarioDTO buscarPorUsername(String username) {
		return usuarioRepository.findByUsername(username).map(u -> {
			u.setUsername(AESUtil.decrypt(u.getUsername()));
			u.setEmail(AESUtil.decrypt(u.getEmail()));
			return modelMapper.map(u, UsuarioDTO.class);
		}).orElse(null);

	}

	public boolean findUsernameAlreadyTaken(String username) {
		String enc = AESUtil.encrypt(username);
		return usuarioRepository.findByUsername(enc).isPresent();
	}

	public int validateCredentials(String username, String password) {
		String enc = AESUtil.encrypt(username);
		Optional<Usuario> opt = usuarioRepository.findByUsername(enc);
		if (opt.isPresent() && AESUtil.validatePassword(password, opt.get().getPassword())) {
			return 0;
		}
		return 1;
	}

	@Override
	public List<UsuarioDTO> getAll() {
		List<Usuario> entities = usuarioRepository.findAll();
		List<UsuarioDTO> dtos = new ArrayList<>();
		for (Usuario u : entities) {
			u.setUsername(AESUtil.decrypt(u.getUsername()));
			u.setEmail(AESUtil.decrypt(u.getEmail()));
			UsuarioDTO dto = modelMapper.map(u, UsuarioDTO.class);
			dtos.add(dto);
		}
		return dtos;
	}
	
	public int cambiarContrasenia(String username, String mail) {
		Random random = new Random();
		int numeroRango = random.nextInt(1000)+10; 
		
		username = AESUtil.encrypt(username);
		UsuarioDTO contra = buscarPorUsername(username);
		if(contra==null) {
			return 1;
		}
	
		if(mail.equals(contra.getEmail())) {
			Optional<Usuario> opt = usuarioRepository.findByUsername(username);
			if (opt.isEmpty())
				return 1;
			Usuario u = opt.get();
			String contrasenia = "Club"+"Artic"+numeroRango+"#";
			u.setPassword(AESUtil.hashinBCrypt(contrasenia));
			usuarioRepository.save(u);
			try {
				emailService.sendPassword(contra, contrasenia);
				return 0;
			} catch (MessagingException e) {
				return 2;
			}
			
		}else {
			return 2;
		}
	}
	
	
	
	@Override
	public int deleteByUsername(String username) {
		String enc = AESUtil.encrypt(username);
		return usuarioRepository.findByUsername(enc).map(u -> {
			usuarioRepository.delete(u);
			return 0;
		}).orElse(1);
	}

	@Override
	public int updateByUsername(String username, UsuarioDTO newData) {
		Optional<Usuario> opt = usuarioRepository.findByUsername(username);
		if (opt.isEmpty())
			return 2;
		String newEnc = AESUtil.encrypt(newData.getUsername());
		Optional<Usuario> existing = usuarioRepository.findByUsername(newEnc);
		if (existing.isPresent() && !existing.get().getUsername().equals(username)) {
			return 1;
		}
		Usuario u = opt.get();
		u.setUsername(newEnc);
		u.setPassword(AESUtil.hashinBCrypt(newData.getPassword()));
		u.setRole(newData.getRole());
		u.setEmail(AESUtil.encrypt(newData.getEmail()));
		u.setEmailVerified(newData.isEmailVerified());
		usuarioRepository.save(u);
		return 0;
	}

	@Override
	public long count() {
		return usuarioRepository.count();
	}

	@Override
	public boolean exist(String username) {
		return usuarioRepository.existsByUsername(username);
	}

}
