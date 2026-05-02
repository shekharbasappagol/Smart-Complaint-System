package com.complaint.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.complaint.model.OtpData;

@Service
public class OtpService {

    private Map<String, OtpData> otpStorage = new HashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        otpStorage.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(2)));

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        OtpData data = otpStorage.get(email);

        if(data == null) return false;

        if(LocalDateTime.now().isAfter(data.getExpiry())) {
            otpStorage.remove(email);
            return false;
        }

        return data.getOtp().equals(otp);
    }
}