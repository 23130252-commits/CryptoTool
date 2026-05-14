package com.cryptotool.service.modern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class SymmetricCipherService {
    private KeyIvService keyIvService = new KeyIvService();

    /**
     * Mã hóa AES với ECB mode
     */
    public String encryptAESECB(String plaintext, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Giải mã AES với ECB mode
     */
    public String decryptAESECB(String ciphertext, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    /**
     * Mã hóa AES với CBC mode
     */
    public String encryptAESCBC(String plaintext, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Giải mã AES với CBC mode
     */
    public String decryptAESCBC(String ciphertext, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    /**
     * Mã hóa AES với CFB mode
     */
    public String encryptAESCFB(String plaintext, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Giải mã AES với CFB mode
     */
    public String decryptAESCFB(String ciphertext, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    /**
     * Mã hóa DES với ECB mode
     */
    public String encryptDESECB(String plaintext, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Giải mã DES với ECB mode
     */
    public String decryptDESECB(String ciphertext, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    /**
     * Mã hóa DES với CBC mode
     */
    public String encryptDESCBC(String plaintext, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Giải mã DES với CBC mode
     */
    public String decryptDESCBC(String ciphertext, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    /**
     * Mã hóa 3DES với ECB mode
     */
    public String encrypt3DESECB(String plaintext, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Giải mã 3DES với ECB mode
     */
    public String decrypt3DESECB(String ciphertext, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    /**
     * Mã hóa 3DES với CBC mode
     */
    public String encrypt3DESCBC(String plaintext, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Giải mã 3DES với CBC mode
     */
    public String decrypt3DESCBC(String ciphertext, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }
}
