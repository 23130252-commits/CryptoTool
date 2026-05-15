package com.cryptotool.service.modern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SymmetricCipherService {

    // AES - ECB

    public String encryptAESECB(String plaintext, byte[] key) throws Exception {
        return encryptText(
                plaintext,
                key,
                null,
                "AES",
                "AES/ECB/PKCS5Padding"
        );
    }

    public String decryptAESECB(String ciphertextBase64, byte[] key) throws Exception {
        return decryptText(
                ciphertextBase64,
                key,
                null,
                "AES",
                "AES/ECB/PKCS5Padding"
        );
    }

    // AES - CBC

    public String encryptAESCBC(String plaintext, byte[] key, byte[] iv) throws Exception {
        return encryptText(
                plaintext,
                key,
                iv,
                "AES",
                "AES/CBC/PKCS5Padding"
        );
    }

    public String decryptAESCBC(String ciphertextBase64, byte[] key, byte[] iv) throws Exception {
        return decryptText(
                ciphertextBase64,
                key,
                iv,
                "AES",
                "AES/CBC/PKCS5Padding"
        );
    }

    // DESede / 3DES - ECB

    public String encrypt3DESECB(String plaintext, byte[] key) throws Exception {
        return encryptText(
                plaintext,
                key,
                null,
                "DESede",
                "DESede/ECB/PKCS5Padding"
        );
    }

    public String decrypt3DESECB(String ciphertextBase64, byte[] key) throws Exception {
        return decryptText(
                ciphertextBase64,
                key,
                null,
                "DESede",
                "DESede/ECB/PKCS5Padding"
        );
    }

    // DESede / 3DES - CBC

    public String encrypt3DESCBC(String plaintext, byte[] key, byte[] iv) throws Exception {
        return encryptText(
                plaintext,
                key,
                iv,
                "DESede",
                "DESede/CBC/PKCS5Padding"
        );
    }

    public String decrypt3DESCBC(String ciphertextBase64, byte[] key, byte[] iv) throws Exception {
        return decryptText(
                ciphertextBase64,
                key,
                iv,
                "DESede",
                "DESede/CBC/PKCS5Padding"
        );
    }
    // Core text methods


    private String encryptText(
            String plaintext,
            byte[] key,
            byte[] iv,
            String keyAlgorithm,
            String transformation
    ) throws Exception {
        if (plaintext == null) {
            throw new IllegalArgumentException("Dữ liệu cần mã hóa không được null.");
        }

        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = encryptBytes(inputBytes, key, iv, keyAlgorithm, transformation);

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String decryptText(
            String ciphertextBase64,
            byte[] key,
            byte[] iv,
            String keyAlgorithm,
            String transformation
    ) throws Exception {
        if (ciphertextBase64 == null || ciphertextBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu cần giải mã không được để trống.");
        }

        byte[] encryptedBytes;

        try {
            encryptedBytes = Base64.getDecoder().decode(ciphertextBase64.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dữ liệu giải mã phải là chuỗi Base64 hợp lệ.");
        }

        byte[] decryptedBytes = decryptBytes(encryptedBytes, key, iv, keyAlgorithm, transformation);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // Core byte methods

    public byte[] encryptBytes(
            byte[] input,
            byte[] key,
            byte[] iv,
            String keyAlgorithm,
            String transformation
    ) throws Exception {
        validateInputBytes(input);
        validateKey(key, keyAlgorithm);
        validateIvIfNeeded(iv, keyAlgorithm, transformation);

        SecretKeySpec secretKey = new SecretKeySpec(key, keyAlgorithm);
        Cipher cipher = Cipher.getInstance(transformation);

        if (requiresIv(transformation)) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        }

        return cipher.doFinal(input);
    }

    public byte[] decryptBytes(
            byte[] input,
            byte[] key,
            byte[] iv,
            String keyAlgorithm,
            String transformation
    ) throws Exception {
        validateInputBytes(input);
        validateKey(key, keyAlgorithm);
        validateIvIfNeeded(iv, keyAlgorithm, transformation);

        SecretKeySpec secretKey = new SecretKeySpec(key, keyAlgorithm);
        Cipher cipher = Cipher.getInstance(transformation);

        if (requiresIv(transformation)) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        }

        return cipher.doFinal(input);
    }

    // Validation
    private void validateInputBytes(byte[] input) {
        if (input == null) {
            throw new IllegalArgumentException("Dữ liệu đầu vào không được null.");
        }
    }

    private void validateKey(byte[] key, String keyAlgorithm) {
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("Key không được để trống.");
        }

        if ("AES".equals(keyAlgorithm)) {
            if (key.length != 16 && key.length != 24 && key.length != 32) {
                throw new IllegalArgumentException("AES key phải dài 16, 24 hoặc 32 byte.");
            }
            return;
        }

        if ("DESede".equals(keyAlgorithm)) {
            if (key.length != 24) {
                throw new IllegalArgumentException("DESede/3DES key phải dài 24 byte.");
            }
            return;
        }

        throw new IllegalArgumentException("Thuật toán key không được hỗ trợ: " + keyAlgorithm);
    }

    private void validateIvIfNeeded(byte[] iv, String keyAlgorithm, String transformation) {
        if (!requiresIv(transformation)) {
            return;
        }

        if (iv == null || iv.length == 0) {
            throw new IllegalArgumentException("Mode CBC cần IV.");
        }

        if ("AES".equals(keyAlgorithm) && iv.length != 16) {
            throw new IllegalArgumentException("AES CBC cần IV dài 16 byte.");
        }

        if ("DESede".equals(keyAlgorithm) && iv.length != 8) {
            throw new IllegalArgumentException("DESede/3DES CBC cần IV dài 8 byte.");
        }
    }

    private boolean requiresIv(String transformation) {
        return transformation != null && transformation.contains("/CBC/");
    }
}