package ru.researchser.mailSender.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@PropertySource(value = "classpath:application.yml")
@RequiredArgsConstructor
public class MailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.sender.email}")
    private String emailFrom;
    @Value("${service.activation.uri}")
    private String activationUri;


    public void sendVerificationEmail(SendMailRequest request){
        String subject = "Email address confirmation";
        String messageBody = String.format("Для подтверждения Ваше электронной почты и " +
                        "активации аккаунта необходимо перейти по ссылке %s", activationUri);
        messageBody = messageBody.replace("{id}", request.getCryptoUserId());
        String emailTo = request.getUserEmail();

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        mailSender.send(mailMessage);
    }
}
