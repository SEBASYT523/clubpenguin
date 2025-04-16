package co.edu.unbosque.backclubpenguin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import co.edu.unbosque.backclubpenguin.model.Usuario;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    // Puedes configurar esta URL en tu application.properties
    @Value("${app.validation.url}")
    private String validationUrl;

    public void sendValidationEmail(Usuario usuario) {
        // Generar token (por ejemplo, usar el id del usuario; en una implementación real, genera un token seguro)
        String token = usuario.getId();
        
        String subject = "Validación de cuenta en Club Penguin App";
        String text = "Hola " + usuario.getUsername() + ",\n\n"
                + "Para validar tu cuenta, haz clic en el siguiente enlace:\n"
                + validationUrl + "?token=" + token + "\n\n"
                + "Si no solicitaste este registro, ignora este correo.\n\n"
                + "Saludos,\nEquipo Club Penguin App";
        
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(usuario.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        
        mailSender.send(mailMessage);
    }
}
