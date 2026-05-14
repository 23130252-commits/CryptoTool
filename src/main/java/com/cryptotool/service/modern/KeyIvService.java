package com.cryptotool.service.modern;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyIvService {
    private SecureRandom random = new SecureRandom();

    /**
     * Tạo key ngẫu nhiên
     */
    public byte[] generateKey(int keySize) {
        if (keySize != 128 && keySize != 192 && keySize != 256) {
            throw new IllegalArgumentException("Key size phải là 128, 192, hoặc 256 bits!");
        }
        byte[] key = new byte[keySize / 8];
        random.nextBytes(key);
        return key;
    }

    /**
     * Tạo IV (Initialization Vector)
     */
    public byte[] generateIV(int ivSize) {
        byte[] iv = new byte[ivSize / 8];
        random.nextBytes(iv);
        return iv;
    }

    /**
     * Tạo IV 128-bit (thường dùng cho AES)
     */
    public byte[] generateAESIV() {
        return generateIV(128);
    }

    /**
     * Chuyển key/IV thành Base64
     */
    public String keyToBase64(byte[] key) {
        return Base64.getEncoder().encodeToString(key);
    }

    /**
     * Chuyển Base64 thành key/IV
     */
    public byte[] base64ToKey(String base64Key) {
        return Base64.getDecoder().decode(base64Key);
    }

    /**
     * Chuyển key/IV thành Hex
     */
    public String keyToHex(byte[] key) {
        StringBuilder hex = new StringBuilder();
        for (byte b : key) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }

    /**
     * Chuyển Hex thành key/IV
     */
    public byte[] hexToKey(String hexKey) {
        byte[] key = new byte[hexKey.length() / 2];
        for (int i = 0; i < hexKey.length(); i += 2) {
            key[i / 2] = (byte) ((Character.digit(hexKey.charAt(i), 16) << 4)
                    + Character.digit(hexKey.charAt(i + 1), 16));
        }
        return key;
    }
}
