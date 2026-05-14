package com.cryptotool.service.rsa;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class RSAService {
    private int keySize = 2048;
    private KeyPair keyPair;

    /**
     * Khởi tạo RSAService với kích thước key mặc định (2048 bits)
     */
    public RSAService() {
        this.keySize = 2048;
    }

    /**
     * Khởi tạo RSAService với kích thước key tùy chỉnh
     */
    public RSAService(int keySize) {
        if (keySize != 1024 && keySize != 2048 && keySize != 4096) {
            throw new IllegalArgumentException("Key size phải là 1024, 2048, hoặc 4096 bits!");
        }
        this.keySize = keySize;
    }

    /**
     * Tạo cặp khóa RSA
     */
    public void generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize);
        this.keyPair = keyGen.generateKeyPair();
    }

    /**
     * Mã hóa RSA
     */
    public String encrypt(String plaintext, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Giải mã RSA
     */
    public String decrypt(String ciphertext, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    /**
     * Ký dữ liệu với private key
     */
    public String sign(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signed = signature.sign();
        return Base64.getEncoder().encodeToString(signed);
    }

    /**
     * Xác minh chữ ký với public key
     */
    public boolean verify(String data, String signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        byte[] decodedSignature = Base64.getDecoder().decode(signature);
        return sig.verify(decodedSignature);
    }

    /**
     * Lấy public key dạng Base64
     */
    public String getPublicKeyBase64() {
        if (keyPair == null) {
            throw new IllegalStateException("KeyPair chưa được tạo! Gọi generateKeyPair() trước.");
        }
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    /**
     * Lấy private key dạng Base64
     */
    public String getPrivateKeyBase64() {
        if (keyPair == null) {
            throw new IllegalStateException("KeyPair chưa được tạo! Gọi generateKeyPair() trước.");
        }
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    /**
     * Tạo public key từ Base64
     */
    public PublicKey getPublicKeyFromBase64(String base64Key) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    /**
     * Tạo private key từ Base64
     */
    public PrivateKey getPrivateKeyFromBase64(String base64Key) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    /**
     * Lấy public key từ KeyPair
     */
    public PublicKey getPublicKey() {
        if (keyPair == null) {
            throw new IllegalStateException("KeyPair chưa được tạo!");
        }
        return keyPair.getPublic();
    }

    /**
     * Lấy private key từ KeyPair
     */
    public PrivateKey getPrivateKey() {
        if (keyPair == null) {
            throw new IllegalStateException("KeyPair chưa được tạo!");
        }
        return keyPair.getPrivate();
    }

    /**
     * Lấy kích thước key hiện tại
     */
    public int getKeySize() {
        return keySize;
    }
}
