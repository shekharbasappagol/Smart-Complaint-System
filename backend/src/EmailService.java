package com.complaint.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP Verification");
        message.setText(
            "Dear User,\n\n" +
            "Your OTP for verification is: " + otp + "\n" +
            "This OTP is valid for 2 minutes.\n\n" +
            "Regards,\nSmart Complaint System"
            );

        mailSender.send(message);
    }
    public void sendStatusUpdate(String toEmail, String status, String title) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Complaint Status Update");

        message.setText(
            "Hello,\n\n" +
            "Your complaint titled '" + title + "' is now: " + status + ".\n\n" +
            "Thank you."
        );

        mailSender.send(message);
    }
    
    public void sendComplaintCreated(String toEmail, String title) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Complaint Submitted Successfully");

        message.setText(
            "Hello,\n\n" +
            "Your complaint titled '" + title + "' has been successfully submitted.\n\n" +
            "Our team will review it shortly.\n\n" +
            "Thank you."
        );

        mailSender.send(message);
    }
}