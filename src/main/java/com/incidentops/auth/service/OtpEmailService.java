package com.incidentops.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpEmailService {

    private final JavaMailSender mailSender;

    @Value("${app.auth.otp-email-from:no-reply@incidentops.local}")
    private String fromAddress;

    @Value("${app.auth.otp-expiration-seconds:120}")
    private long otpExpirationSeconds;

    public void sendOtp(String toEmail, String otp, String flowLabel) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("IncidentOps " + flowLabel + " OTP");
        message.setText(
                "Your OTP is: " + otp + "\n\n"
                        + "This OTP expires in " + otpExpirationSeconds + " seconds.\n"
                        + "If you did not request this, please ignore this email."
        );
        mailSender.send(message);
    }
}
