package com.shopHMsic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
public class KeyPairManager {

    @Value("${idp.private-key}")
    private String privateKeyPath;

    @Value("${idp.public-key}")
    private String publicKeyPath;

    private PrivateKey privateKey;
    private String publicKeyPem;

    @PostConstruct
    public void init() {
        try {
            // Load Private Key from ClassPath (src/main/resources/privateKey.rsa)
            ClassPathResource privateKeyResource = new ClassPathResource(privateKeyPath);
            if (!privateKeyResource.exists()) {
                throw new RuntimeException("Private key file not found in classpath: " + privateKeyPath);
            }

            String privateKeyBase64 = new String(Files.readAllBytes(Paths.get(privateKeyResource.getURI()))).trim();
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = keyFactory.generatePrivate(keySpec);

            // Deriving Public Key from Private Key
            if (this.privateKey instanceof RSAPrivateCrtKey) {
                RSAPrivateCrtKey apc = (RSAPrivateCrtKey) this.privateKey;
                RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(apc.getModulus(), apc.getPublicExponent());
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
                byte[] pubBytes = publicKey.getEncoded();
                String base64PublicKey = Base64.getEncoder().encodeToString(pubBytes);

                // Format PEM public key
                StringBuilder pem = new StringBuilder();
                pem.append("-----BEGIN PUBLIC KEY-----\n");
                int index = 0;
                while (index < base64PublicKey.length()) {
                    int end = Math.min(index + 64, base64PublicKey.length());
                    pem.append(base64PublicKey, index, end).append("\n");
                    index = end;
                }
                pem.append("-----END PUBLIC KEY-----");
                this.publicKeyPem = pem.toString();

                // Write public key to file (src/main/resources/publicKey.rsa) if not exists
                ClassPathResource publicKeyResource = new ClassPathResource(publicKeyPath);
                boolean existsInClasspath = publicKeyResource.exists();

                if (!existsInClasspath) {
                    // Write to the resources directory in workspace
                    String workspaceResourcesPath = "d:/Working/CaNhan/do_an/src/main/resources/" + publicKeyPath;
                    File wsFile = new File(workspaceResourcesPath);
                    try (FileOutputStream fos = new FileOutputStream(wsFile)) {
                        fos.write(base64PublicKey.getBytes("UTF-8"));
                        System.out.println("Tự động tạo file publicKey.rsa thành công tại: " + wsFile.getAbsolutePath());
                    } catch (Exception ex) {
                        System.err.println("Không thể ghi file publicKey.rsa vào workspace: " + ex.getMessage());
                    }

                    // Also write to target/classes so it's readable immediately
                    String targetClassesPath = "d:/Working/CaNhan/do_an/target/classes/" + publicKeyPath;
                    File targetFile = new File(targetClassesPath);
                    targetFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                        fos.write(base64PublicKey.getBytes("UTF-8"));
                        System.out.println("Tự động tạo file publicKey.rsa thành công tại target: " + targetFile.getAbsolutePath());
                    } catch (Exception ex) {
                        // Ignore target write errors
                    }
                }
            } else {
                throw new RuntimeException("Loaded key is not an RSA private CRT key");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing KeyPairManager from resource key files", e);
        }
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public String getPublicKeyPem() {
        return this.publicKeyPem;
    }

    public String getPublicKey() {
        return this.publicKeyPath;
    }
}
