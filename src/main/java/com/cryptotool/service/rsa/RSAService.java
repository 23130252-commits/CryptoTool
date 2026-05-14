package com.cryptotool.service.rsa;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSAService {
    private static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    private final int keySize;

    private KeyPair keyPair;

    public RSAService(int keySize) {
        validateKeySize(keySize);
        this.keySize = keySize;
    }

    public void generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    public PublicKey getPublicKey() {
        ensureKeyPairGenerated();
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        ensureKeyPairGenerated();
        return keyPair.getPrivate();
    }

    public String getPublicKeyBase64() {
        ensureKeyPairGenerated();
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    public String getPrivateKeyBase64() {
        ensureKeyPairGenerated();
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    public PublicKey getPublicKeyFromBase64(String publicKeyBase64) throws Exception {
        if (publicKeyBase64 == null || publicKeyBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("Public key không được để trống.");
        }

        byte[] keyBytes;

        try {
            keyBytes = Base64.getDecoder().decode(publicKeyBase64.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Public key phải là chuỗi Base64 hợp lệ.");
        }

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpec);
    }

    public PrivateKey getPrivateKeyFromBase64(String privateKeyBase64) throws Exception {
        if (privateKeyBase64 == null || privateKeyBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("Private key không được để trống.");
        }

        byte[] keyBytes;

        try {
            keyBytes = Base64.getDecoder().decode(privateKeyBase64.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Private key phải là chuỗi Base64 hợp lệ.");
        }

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);
    }

    public String encrypt(String plaintext, PublicKey publicKey) throws Exception {
        if (plaintext == null || plaintext.isEmpty()) {
            throw new IllegalArgumentException("Văn bản cần mã hóa không được để trống.");
        }

        if (publicKey == null) {
            throw new IllegalArgumentException("Public key không được null.");
        }

        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = processBlocks(inputBytes, publicKey, Cipher.ENCRYPT_MODE);

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String ciphertextBase64, PrivateKey privateKey) throws Exception {
        if (ciphertextBase64 == null || ciphertextBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("Ciphertext không được để trống.");
        }

        if (privateKey == null) {
            throw new IllegalArgumentException("Private key không được null.");
        }

        byte[] encryptedBytes;

        try {
            encryptedBytes = Base64.getDecoder().decode(ciphertextBase64.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ciphertext phải là chuỗi Base64 hợp lệ.");
        }

        byte[] decryptedBytes = processBlocks(encryptedBytes, privateKey, Cipher.DECRYPT_MODE);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private byte[] processBlocks(byte[] inputBytes, java.security.Key key, int cipherMode) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
        cipher.init(cipherMode, key);

        int keyByteLength = getKeyByteLength(key);

        int blockSize;

        if (cipherMode == Cipher.ENCRYPT_MODE) {
            // PKCS1Padding cần 11 byte padding.
            blockSize = keyByteLength - 11;
        } else {
            blockSize = keyByteLength;
        }

        if (blockSize <= 0) {
            throw new IllegalArgumentException("Key RSA không hợp lệ hoặc quá nhỏ.");
        }

        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();

        int offset = 0;

        while (offset < inputBytes.length) {
            int length = Math.min(blockSize, inputBytes.length - offset);
            byte[] processedBlock = cipher.doFinal(inputBytes, offset, length);
            outputStream.write(processedBlock);
            offset += length;
        }

        return outputStream.toByteArray();
    }

    private int getKeyByteLength(java.security.Key key) {
        if (!(key instanceof RSAKey)) {
            throw new IllegalArgumentException("Key không phải RSA key.");
        }

        RSAKey rsaKey = (RSAKey) key;
        int bitLength = rsaKey.getModulus().bitLength();

        return (bitLength + 7) / 8;
    }

    public int getKeySize() {
        return keySize;
    }

    private void validateKeySize(int keySize) {
        if (keySize != 1024 && keySize != 2048) {
            throw new IllegalArgumentException("RSA chuẩn chỉ hỗ trợ 1024 hoặc 2048 bit.");
        }
    }

    private void ensureKeyPairGenerated() {
        if (keyPair == null) {
            throw new IllegalStateException("Chưa tạo cặp khóa RSA.");
        }
    }
}