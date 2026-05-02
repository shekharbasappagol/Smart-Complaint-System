package com.complaint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.complaint.service.EmailService;
import com.complaint.service.OtpService;

@RestController
@RequestMapping("/otp")
@CrossOrigin
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendOtp(@RequestParam String email) {

        if(!email.contains("@")) {
            return "Invalid email format";
        }

        String otp = otpService.generateOtp(email);
        emailService.sendOtp(email, otp);

        return "OTP sent";
    }

    @PostMapping("/verify")
    public boolean verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return otpService.verifyOtp(email, otp);
    }
}