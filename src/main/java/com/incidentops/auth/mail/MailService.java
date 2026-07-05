package com.incidentops.auth.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String email, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        String subject = "IncidentOps Verification OTP";
        String body = """
        Hello,

        Your IncidentOps verification code is:

        %s

        This code will expire in 5 minutes.

        If you did not request this, you can safely ignore this email.
        """.formatted(otp);
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
