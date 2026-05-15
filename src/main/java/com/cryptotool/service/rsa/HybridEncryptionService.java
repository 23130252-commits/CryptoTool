package com.cryptotool.service.rsa;

import com.cryptotool.util.FileUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class HybridEncryptionService {
    private static final String MAGIC = "RSAENC1";

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    private static final int AES_KEY_SIZE = 128;
    private static final int AES_IV_SIZE_BYTES = 16;

    private final SecureRandom secureRandom;

    public HybridEncryptionService() {
        this.secureRandom = new SecureRandom();
    }

    public File encryptFile(File inputFile, PublicKey publicKey) throws Exception {
        validateInputFile(inputFile);

        if (publicKey == null) {
            throw new IllegalArgumentException("Public key không được null.");
        }

        SecretKey aesKey = generateAESKey();
        byte[] iv = generateIV();

        byte[] originalFileBytes = FileUtil.readFileAsBytes(inputFile.getAbsolutePath());
        byte[] encryptedFileBytes = encryptWithAES(originalFileBytes, aesKey.getEncoded(), iv);
        byte[] encryptedAESKey = encryptAESKeyWithRSA(aesKey.getEncoded(), publicKey);

        byte[] originalFileNameBytes = inputFile.getName().getBytes(StandardCharsets.UTF_8);

        File outputFile = new File(inputFile.getAbsolutePath() + ".rsaenc");

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
            dos.writeUTF(MAGIC);

            dos.writeInt(encryptedAESKey.length);
            dos.writeInt(iv.length);
            dos.writeInt(originalFileNameBytes.length);
            dos.writeInt(encryptedFileBytes.length);

            dos.write(encryptedAESKey);
            dos.write(iv);
            dos.write(originalFileNameBytes);
            dos.write(encryptedFileBytes);
        }

        return outputFile;
    }

    public File decryptFile(File encryptedFile, PrivateKey privateKey) throws Exception {
        validateInputFile(encryptedFile);

        if (privateKey == null) {
            throw new IllegalArgumentException("Private key không được null.");
        }

        byte[] allBytes = FileUtil.readFileAsBytes(encryptedFile.getAbsolutePath());

        ParsedHybridFile parsedFile = parseHybridFile(allBytes);

        byte[] aesKey = decryptAESKeyWithRSA(parsedFile.encryptedAESKey, privateKey);
        byte[] originalFileBytes = decryptWithAES(parsedFile.encryptedFileBytes, aesKey, parsedFile.iv);

        File parent = encryptedFile.getParentFile();
        String outputFileName = "decrypted_" + parsedFile.originalFileName;

        File outputFile;

        if (parent == null) {
            outputFile = new File(outputFileName);
        } else {
            outputFile = new File(parent, outputFileName);
        }

        FileUtil.writeFileAsBytes(outputFile.getAbsolutePath(), originalFileBytes);

        return outputFile;
    }

    private SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(AES_KEY_SIZE);
        return keyGenerator.generateKey();
    }

    private byte[] generateIV() {
        byte[] iv = new byte[AES_IV_SIZE_BYTES];
        secureRandom.nextBytes(iv);
        return iv;
    }

    private byte[] encryptWithAES(byte[] input, byte[] aesKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(
                Cipher.ENCRYPT_MODE,
                new javax.crypto.spec.SecretKeySpec(aesKey, AES_ALGORITHM),
                new IvParameterSpec(iv)
        );

        return cipher.doFinal(input);
    }

    private byte[] decryptWithAES(byte[] input, byte[] aesKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(
                Cipher.DECRYPT_MODE,
                new javax.crypto.spec.SecretKeySpec(aesKey, AES_ALGORITHM),
                new IvParameterSpec(iv)
        );

        return cipher.doFinal(input);
    }

    private byte[] encryptAESKeyWithRSA(byte[] aesKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(aesKey);
    }

    private byte[] decryptAESKeyWithRSA(byte[] encryptedAESKey, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedAESKey);
    }

    private ParsedHybridFile parseHybridFile(byte[] allBytes) throws Exception {
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(allBytes))) {
            String magic = dis.readUTF();

            if (!MAGIC.equals(magic)) {
                throw new IllegalArgumentException("File không đúng định dạng RSA hybrid.");
            }

            int encryptedKeyLength = dis.readInt();
            int ivLength = dis.readInt();
            int fileNameLength = dis.readInt();
            int encryptedDataLength = dis.readInt();

            validateLength(encryptedKeyLength, "encrypted AES key");
            validateLength(ivLength, "IV");
            validateLength(fileNameLength, "tên file");
            validateLength(encryptedDataLength, "dữ liệu mã hóa");

            byte[] encryptedAESKey = new byte[encryptedKeyLength];
            byte[] iv = new byte[ivLength];
            byte[] fileNameBytes = new byte[fileNameLength];
            byte[] encryptedFileBytes = new byte[encryptedDataLength];

            dis.readFully(encryptedAESKey);
            dis.readFully(iv);
            dis.readFully(fileNameBytes);
            dis.readFully(encryptedFileBytes);

            String originalFileName = new String(fileNameBytes, StandardCharsets.UTF_8);

            return new ParsedHybridFile(
                    encryptedAESKey,
                    iv,
                    originalFileName,
                    encryptedFileBytes
            );
        }
    }

    private void validateInputFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File không được null.");
        }

        if (!file.exists()) {
            throw new IllegalArgumentException("File không tồn tại: " + file.getAbsolutePath());
        }

        if (!file.isFile()) {
            throw new IllegalArgumentException("Đường dẫn không phải file hợp lệ.");
        }
    }

    private void validateLength(int length, String name) {
        if (length <= 0) {
            throw new IllegalArgumentException("Độ dài " + name + " không hợp lệ.");
        }
    }

    private static class ParsedHybridFile {
        private final byte[] encryptedAESKey;
        private final byte[] iv;
        private final String originalFileName;
        private final byte[] encryptedFileBytes;

        private ParsedHybridFile(
                byte[] encryptedAESKey,
                byte[] iv,
                String originalFileName,
                byte[] encryptedFileBytes
        ) {
            this.encryptedAESKey = encryptedAESKey;
            this.iv = iv;
            this.originalFileName = originalFileName;
            this.encryptedFileBytes = encryptedFileBytes;
        }
    }
}