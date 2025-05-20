package co.edu.unbosque.backclubpenguin.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import co.edu.unbosque.backclubpenguin.dto.UsuarioDTO;

@Service
public class EmailService {

	private final JavaMailSender mailSender;
	private final String validationUrl;

	public EmailService(JavaMailSender mailSender, @Value("${app.validation.url}") String validationUrl) {
		this.mailSender = mailSender;
		this.validationUrl = validationUrl;
	}

	public void sendValidationEmail(UsuarioDTO usuario, String token) throws MessagingException {

		String username = usuario.getUsername();
		String recipient = usuario.getEmail();
		String link = validationUrl + "?token=" + token;

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		helper.setTo(recipient);
		helper.setSubject("Verify Your Email!");

		String htmlMsg = """
				<html>
				<body style="margin:0;padding:0;font-family:Arial,sans-serif;">

				  <div style="background-color:#0078D7;padding:20px;text-align:center;">
				    <h1 style="color:#ffffff;margin:0;">Verifica tu correo!</h1>
				  </div>


				  <div style="padding:30px;">
				    <p>Hola <strong>%s</strong>, gracias por crear una cuenta en nuestra app de Club Penguin!</p>
				    <p>Para que tengas acceso completo, por favor verifica tu direccion de correo en el siguiente link:</p>
				    <p style="text-align:center;margin:40px 0;">
				      <a href="%s" style="
				           background-color:#0078D7;
				           color:#ffffff;
				           padding:15px 30px;
				           text-decoration:none;
				           font-weight:bold;
				           border-radius:5px;
				           display:inline-block;">
				        Confirma el correo
				      </a>
				    </p>
				    <p>Si no creaste esta cuenta, ignora al completo este mensaje.</p>
				    <p>Saludos,<br/>Club Artic Penguin App Team</p>
				  </div>


				  <div style="background-color:#f1f1f1;padding:10px;text-align:center;color:#555;">
				    &copy; 2025 Club Artic Penguin App. All rights reserved.
				  </div>
				</body>
				</html>
				"""
				.formatted(username, link);

		helper.setText(htmlMsg, true);
		mailSender.send(mimeMessage);

	}

	public void sendPassword(UsuarioDTO usuario, String contrasenia) throws MessagingException {

		String username = (usuario.getUsername());
		String recipient = (usuario.getEmail());
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

		helper.setTo(recipient);
		helper.setSubject("Tu nueva contraseña — Club Artic Penguin");

		String htmlMsg = """
				<html>
				<body style="margin:0;padding:0;font-family:Arial,sans-serif;">

				  <div style="background-color:#0078D7;padding:20px;text-align:center;">
				    <h1 style="color:#ffffff;margin:0;">Solicitud de contraseña</h1>
				  </div>

				  <div style="padding:30px;">
				    <p>Hola <strong>%s</strong>,</p>
				    <p>Se ha generado una <strong>nueva contraseña</strong> para tu cuenta:</p>
				    <p style="background:#f5f5f5; padding:15px; font-size:1.2rem; text-align:center; margin:20px 0; border-radius:5px;">
				      %s
				    </p>
				    <p>Por favor, úsala para iniciar sesión y luego cámbiala desde tu perfil.</p>
				    <p>Si no solicitaste este cambio, por favor contacta con soporte inmediatamente.</p>
				    <p>Saludos,<br/>Equipo Club Artic Penguin App</p>
				  </div>

				  <div style="background-color:#f1f1f1;padding:10px;text-align:center;color:#555;">
				    &copy;  Club Artic Penguin App. All rights reserved.
				  </div>
				</body>
				</html>
				"""
				.formatted(username, contrasenia);

		helper.setText(htmlMsg, true);
		mailSender.send(mimeMessage);
	}
}
