package com.emi.user_service.serviceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.emi.user_service.model.OtpData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

   private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
   private final Map<String, Long> lastRequestTime = new HashMap<>();

    private static long OTP_VALIDITY = 5 * 60 * 1000;

    public void sendOtp(String phoneNumber){
      long now = System.currentTimeMillis();
      String otp = String.valueOf(new Random().nextInt(900000) + 100000);

      long expiryTime = System.currentTimeMillis() + OTP_VALIDITY;
      if (lastRequestTime.containsKey(phoneNumber) &&
          now - lastRequestTime.get(phoneNumber) < 60000) {
          throw new RuntimeException("Wait before requesting again");
      }

      lastRequestTime.put(phoneNumber, now);
      otpStore.put(phoneNumber, new OtpData(otp, expiryTime));

      log.info("OTP sent to " + phoneNumber + " : " + otp);
  
    }

      public boolean verifyOtp(String phoneNumber, String otp) {

      OtpData data = otpStore.get(phoneNumber);

      if (data == null) {
          throw new RuntimeException("OTP not found");
      }

      // Check expiry
      if (System.currentTimeMillis() > data.getExpiryDate()) {
          otpStore.remove(phoneNumber);
          throw new RuntimeException("OTP expired");
      }

      // Check match
      if (!data.getOtp().equals(otp)) {
          throw new RuntimeException("Invalid OTP");
      }

      // One-time use (IMPORTANT)
      otpStore.remove(phoneNumber);

      return true;
    }
}
