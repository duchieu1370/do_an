package com.shopHMsic.config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class JwtUtil {
    private static final String SECRET_KEY = "msic_shop_secret_key_which_should_be_long_enough_for_hmac_sha256_security";

    public static String generateToken(String username) {
        try {
            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            long now = System.currentTimeMillis();
            long expiry = now + (8 * 60 * 60 * 1000); // 8 hours as requested
            String payload = String.format("{\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}", username, now / 1000, expiry / 1000);

            String headerBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes("UTF-8"));
            String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes("UTF-8"));

            String signatureInput = headerBase64 + "." + payloadBase64;
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "HmacSHA256");
            sha256HMAC.init(secretKeySpec);
            byte[] signatureBytes = sha256HMAC.doFinal(signatureInput.getBytes("UTF-8"));
            String signatureBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

            return headerBase64 + "." + payloadBase64 + "." + signatureBase64;
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    public static String extractUsername(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), "UTF-8");
            
            int subIndex = payloadJson.indexOf("\"sub\":\"");
            if (subIndex == -1) return null;
            int start = subIndex + 7;
            int end = payloadJson.indexOf("\"", start);
            return payloadJson.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean validateToken(String token, String username) {
        try {
            String tokenUser = extractUsername(token);
            if (tokenUser == null || !tokenUser.equals(username)) return false;

            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;
            String headerBase64 = parts[0];
            String payloadBase64 = parts[1];
            String signatureBase64 = parts[2];

            String signatureInput = headerBase64 + "." + payloadBase64;
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "HmacSHA256");
            sha256HMAC.init(secretKeySpec);
            byte[] signatureBytes = sha256HMAC.doFinal(signatureInput.getBytes("UTF-8"));
            String expectedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

            if (!expectedSignature.equals(signatureBase64)) return false;

            String payloadJson = new String(Base64.getUrlDecoder().decode(payloadBase64), "UTF-8");
            int expIndex = payloadJson.indexOf("\"exp\":");
            if (expIndex == -1) return false;
            int start = expIndex + 6;
            int end = payloadJson.indexOf(",", start);
            if (end == -1) {
                end = payloadJson.indexOf("}", start);
            }
            long exp = Long.parseLong(payloadJson.substring(start, end).trim());
            return exp * 1000 > System.currentTimeMillis();
        } catch (Exception e) {
            return false;
        }
    }
}
