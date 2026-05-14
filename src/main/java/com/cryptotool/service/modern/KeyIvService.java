package com.cryptotool.service.modern;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyIvService {
    private final SecureRandom secureRandom;

    public KeyIvService() {
        this.secureRandom = new SecureRandom();
    }

    public byte[] generateKey(int keySizeInBits) {
        validateKeySize(keySizeInBits);

        int keySizeInBytes = keySizeInBits / 8;
        byte[] key = new byte[keySizeInBytes];
        secureRandom.nextBytes(key);

        return key;
    }

    public byte[] generateIV(int ivSizeInBits) {
        validateIvSize(ivSizeInBits);

        int ivSizeInBytes = ivSizeInBits / 8;
        byte[] iv = new byte[ivSizeInBytes];
        secureRandom.nextBytes(iv);

        return iv;
    }

    public String keyToBase64(byte[] keyOrIv) {
        if (keyOrIv == null || keyOrIv.length == 0) {
            throw new IllegalArgumentException("Dữ liệu key/IV không được để trống.");
        }

        return Base64.getEncoder().encodeToString(keyOrIv);
    }

    public byte[] base64ToKey(String base64) {
        if (base64 == null || base64.trim().isEmpty()) {
            throw new IllegalArgumentException("Chuỗi Base64 không được để trống.");
        }

        try {
            return Base64.getDecoder().decode(base64.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Chuỗi Base64 không hợp lệ.");
        }
    }

    public byte[] generateAESKey128() {
        return generateKey(128);
    }

    public byte[] generateAESKey192() {
        return generateKey(192);
    }

    public byte[] generateAESKey256() {
        return generateKey(256);
    }

    public byte[] generate3DESKey() {
        return generateKey(192);
    }

    public byte[] generateAESIV() {
        return generateIV(128);
    }

    public byte[] generate3DESIV() {
        return generateIV(64);
    }

    public boolean isValidAESKey(byte[] key) {
        return key != null && (key.length == 16 || key.length == 24 || key.length == 32);
    }

    public boolean isValid3DESKey(byte[] key) {
        return key != null && key.length == 24;
    }

    public boolean isValidAESIV(byte[] iv) {
        return iv != null && iv.length == 16;
    }

    public boolean isValid3DESIV(byte[] iv) {
        return iv != null && iv.length == 8;
    }

    private void validateKeySize(int keySizeInBits) {
        if (keySizeInBits != 128
                && keySizeInBits != 192
                && keySizeInBits != 256) {
            throw new IllegalArgumentException(
                    "Key size không hợp lệ. Chỉ hỗ trợ 128, 192 hoặc 256 bit."
            );
        }
    }

    private void validateIvSize(int ivSizeInBits) {
        if (ivSizeInBits != 64 && ivSizeInBits != 128) {
            throw new IllegalArgumentException(
                    "IV size không hợp lệ. Chỉ hỗ trợ 64 bit cho 3DES hoặc 128 bit cho AES."
            );
        }
    }
}