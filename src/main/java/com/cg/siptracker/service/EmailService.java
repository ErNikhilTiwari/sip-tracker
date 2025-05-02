package com.cg.siptracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("praveensharma20113@gmail.com");
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);
        mailSender.send(simpleMailMessage);
    }

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("praveensharma20113@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject("Your OTP for Password Reset!.");
        mailMessage.setText("Your OTP for resetting your password is: " + otp + "\n\nThis OTP will expire in 5 minutes.");
        mailSender.send(mailMessage);
        System.out.println("OTP email sent to : " + to);
    }
}