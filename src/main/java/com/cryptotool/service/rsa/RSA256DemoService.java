package com.cryptotool.service.rsa;

import java.security.*;
import java.util.Base64;

public class RSA256DemoService {
    private RSAService rsaService;

    /**
     * Khởi tạo RSA256DemoService
     */
    public RSA256DemoService() {
        this.rsaService = new RSAService(2048);
    }

    /**
     * Tạo cặp khóa RSA
     */
    public void generateKeys() throws Exception {
        rsaService.generateKeyPair();
    }

    /**
     * Demo: Mã hóa và giải mã
     */
    public void demoEncryptDecrypt(String plaintext) throws Exception {
        System.out.println("========== RSA Encryption Demo ==========");
        System.out.println("Plaintext: " + plaintext);

        PublicKey publicKey = rsaService.getPublicKey();
        PrivateKey privateKey = rsaService.getPrivateKey();

        // Mã hóa
        String ciphertext = rsaService.encrypt(plaintext, publicKey);
        System.out.println("Ciphertext: " + ciphertext);

        // Giải mã
        String decrypted = rsaService.decrypt(ciphertext, privateKey);
        System.out.println("Decrypted: " + decrypted);
        System.out.println();
    }

    /**
     * Demo: Ký và xác minh chữ ký
     */
    public void demoSignVerify(String data) throws Exception {
        System.out.println("========== Digital Signature Demo ==========");
        System.out.println("Data: " + data);

        PublicKey publicKey = rsaService.getPublicKey();
        PrivateKey privateKey = rsaService.getPrivateKey();

        // Ký dữ liệu
        String signature = rsaService.sign(data, privateKey);
        System.out.println("Signature: " + signature);

        // Xác minh chữ ký
        boolean verified = rsaService.verify(data, signature, publicKey);
        System.out.println("Signature Valid: " + verified);
        System.out.println();
    }

    /**
     * Demo: Lưu và tải khóa
     */
    public void demoKeyStorage() {
        System.out.println("========== Key Storage Demo ==========");

        String publicKeyBase64 = rsaService.getPublicKeyBase64();
        String privateKeyBase64 = rsaService.getPrivateKeyBase64();

        System.out.println("Public Key (Base64):");
        System.out.println(publicKeyBase64);
        System.out.println();

        System.out.println("Private Key (Base64):");
        System.out.println(privateKeyBase64);
        System.out.println();
    }

    /**
     * Demo: Trao đổi khóa giữa hai bên
     */
    public void demoKeyExchange() throws Exception {
        System.out.println("========== Key Exchange Demo ==========");

        // Bên A tạo khóa
        RSAService serviceA = new RSAService(2048);
        serviceA.generateKeyPair();

        // Bên B tạo khóa
        RSAService serviceB = new RSAService(2048);
        serviceB.generateKeyPair();

        String messageA = "Hello from Party A";
        String messageB = "Hello from Party B";

        System.out.println("Party A Message: " + messageA);
        System.out.println("Party B Message: " + messageB);
        System.out.println();

        // Bên A gửi tin nhắn cho Bên B (mã hóa bằng public key của B)
        String encryptedFromA = serviceA.encrypt(messageA, serviceB.getPublicKey());
        System.out.println("Encrypted from A to B: " + encryptedFromA);

        // Bên B giải mã
        String decryptedByB = serviceB.decrypt(encryptedFromA, serviceB.getPrivateKey());
        System.out.println("Decrypted by B: " + decryptedByB);
        System.out.println();

        // Bên B gửi tin nhắn cho Bên A (mã hóa bằng public key của A)
        String encryptedFromB = serviceB.encrypt(messageB, serviceA.getPublicKey());
        System.out.println("Encrypted from B to A: " + encryptedFromB);

        // Bên A giải mã
        String decryptedByA = serviceA.decrypt(encryptedFromB, serviceA.getPrivateKey());
        System.out.println("Decrypted by A: " + decryptedByA);
        System.out.println();
    }

    /**
     * Demo: Gửi tin nhắn ký với chữ ký số
     */
    public void demoSignedMessage() throws Exception {
        System.out.println("========== Signed Message Demo ==========");

        String message = "This is a certified message";

        PublicKey publicKey = rsaService.getPublicKey();
        PrivateKey privateKey = rsaService.getPrivateKey();

        // Ký tin nhắn
        String signature = rsaService.sign(message, privateKey);

        System.out.println("Message: " + message);
        System.out.println("Signature: " + signature);
        System.out.println();

        // Người nhận xác minh
        boolean isValid = rsaService.verify(message, signature, publicKey);
        System.out.println("Signature is valid: " + isValid);

        // Kiểm tra nếu tin nhắn bị thay đổi
        String tamperedMessage = "This is a tampered message";
        boolean isTamperedValid = rsaService.verify(tamperedMessage, signature, publicKey);
        System.out.println("Tampered message is valid: " + isTamperedValid);
        System.out.println();
    }

    /**
     * Chạy tất cả các demo
     */
    public static void main(String[] args) {
        try {
            RSA256DemoService demo = new RSA256DemoService();

            // Tạo khóa
            demo.generateKeys();

            // Chạy các demo
            demo.demoEncryptDecrypt("Hello, RSA Encryption!");
            demo.demoSignVerify("Important Data");
            demo.demoKeyStorage();
            demo.demoKeyExchange();
            demo.demoSignedMessage();

            System.out.println("========== All Demos Completed ==========");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
