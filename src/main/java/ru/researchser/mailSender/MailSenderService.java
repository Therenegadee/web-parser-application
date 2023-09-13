package ru.researchser.mailSender;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PropertySource(value = "classpath:application.yml")
public class MailSenderService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private final String emailFrom;
    @Value("${service.activation.uri}")
    private final String activationUri;


    public void sendVerificationEmail(SendMailRequest request){
        String subject = "Email address confirmation";
        String messageBody = String.format("Для подтверждения Ваше электронной почты и " +
                        "активации аккаунта необходимо перейти по ссылке %s", activationUri);
        messageBody = messageBody.replace("id", request.getCryptoUserId());
        String emailTo = request.getUserEmail();

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        mailSender.send(mailMessage);
    }
}
