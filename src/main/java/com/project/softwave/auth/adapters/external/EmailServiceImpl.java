package com.project.softwave.auth.adapters.external;

import com.project.softwave.auth.domain.ports.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private Environment env;

    @Override
    public void enviarEmailResetSenha(String email, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(email);
            helper.setSubject("Redefinição de Senha");

            String htmlContent = """
                        <html>
                        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                            <div style="max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">
                                <h2 style="color: #333;">Redefinição de Senha</h2>
                                <p style="font-size: 16px; color: #555;">
                                    Para redefinir sua senha, utilize o código abaixo:
                                </p>
                                <div style="margin: 20px 0; padding: 15px; background-color: #f0f0f0; border-left: 5px solid #007bff; font-size: 18px; font-weight: bold; color: #333;">
                                    %s
                                </div>
                                <p style="font-size: 14px; color: #999;">
                                    O token expira em 5 minutos.
                                </p>
                            </div>
                        </body>
                        </html>
                    """.formatted(token);

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "O serviço não está disponível!" +
                            "Por favor, contate o nosso suporte para que possamos ajudá-lo!"
            );
        }
    }

    @Override
    public void enviarEmailPrimeiroAcesso(String email, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(email);
            helper.setSubject("Primeiro Acesso");

            String htmlContent = """
                        <html>
                        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                            <div style="max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">
                                <h2 style="color: #333;">Primeiro Acesso</h2>
                                <p style="font-size: 16px; color: #555;">
                                    Para acessar sua conta pela primeira vez, utilize o código abaixo:
                                </p>
                                <div style="margin: 20px 0; padding: 15px; background-color: #f0f0f0; border-left: 5px solid #007bff; font-size: 18px; font-weight: bold; color: #333;">
                                    %s
                                </div>
                                <p style="font-size: 14px; color: #999;">
                                    O token expira em 2 horas.
                                </p>
                            </div>
                        </body>
                        </html>
                    """.formatted(token);

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "O serviço não está disponível!" +
                            "Por favor, contate o nosso suporte para que possamos ajudá-lo!"
            );
        }
    }
}