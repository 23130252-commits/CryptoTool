package com.cryptotool.service.rsa;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class RSA256DemoService {
    private static final int PRIME_BIT_LENGTH = 128;
    private static final BigInteger DEFAULT_E = BigInteger.valueOf(65537);

    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;

    private final SecureRandom random;

    public RSA256DemoService() {
        this.random = new SecureRandom();
    }

    public void generateKeys() {
        do {
            p = BigInteger.probablePrime(PRIME_BIT_LENGTH, random);
            q = BigInteger.probablePrime(PRIME_BIT_LENGTH, random);
        } while (p.equals(q));

        n = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        e = DEFAULT_E;

        if (!e.gcd(phi).equals(BigInteger.ONE)) {
            e = findValidE(phi);
        }

        d = e.modInverse(phi);
    }

    public String encrypt(String plaintext) {
        ensureKeysGenerated();

        if (plaintext == null || plaintext.isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu cần mã hóa không được để trống.");
        }

        byte[] plainBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        BigInteger message = new BigInteger(1, plainBytes);

        if (message.compareTo(n) >= 0) {
            throw new IllegalArgumentException(
                    "Dữ liệu quá dài cho RSA 256-bit demo. " +
                            "Hãy nhập chuỗi ngắn hơn hoặc dùng RSA chuẩn 1024/2048-bit."
            );
        }

        BigInteger cipher = message.modPow(e, n);

        return cipher.toString();
    }

    public String decrypt(String cipherText) {
        ensureKeysGenerated();

        if (cipherText == null || cipherText.trim().isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu cần giải mã không được để trống.");
        }

        BigInteger cipher;

        try {
            cipher = new BigInteger(cipherText.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Ciphertext RSA 256 demo phải là số nguyên.");
        }

        if (cipher.compareTo(BigInteger.ZERO) < 0 || cipher.compareTo(n) >= 0) {
            throw new IllegalArgumentException("Ciphertext không hợp lệ với khóa RSA hiện tại.");
        }

        BigInteger message = cipher.modPow(d, n);
        byte[] messageBytes = removeLeadingZero(message.toByteArray());

        return new String(messageBytes, StandardCharsets.UTF_8);
    }

    public String getPublicKeyText() {
        ensureKeysGenerated();

        return "RSA 256-bit Demo Public Key\n"
                + "n=" + n + "\n"
                + "e=" + e;
    }

    public String getPrivateKeyText() {
        ensureKeysGenerated();

        return "RSA 256-bit Demo Private Key\n"
                + "n=" + n + "\n"
                + "d=" + d + "\n\n"
                + "Tham số demo:\n"
                + "p=" + p + "\n"
                + "q=" + q + "\n"
                + "phi=" + phi;
    }

    public String getDetailText() {
        ensureKeysGenerated();

        return "p = " + p + "\n"
                + "q = " + q + "\n"
                + "n = p * q = " + n + "\n"
                + "phi = (p - 1)(q - 1) = " + phi + "\n"
                + "e = " + e + "\n"
                + "d = e^-1 mod phi = " + d;
    }

    public boolean hasKeys() {
        return n != null && e != null && d != null;
    }

    private BigInteger findValidE(BigInteger phi) {
        BigInteger candidate = BigInteger.valueOf(3);

        while (candidate.compareTo(phi) < 0) {
            if (candidate.gcd(phi).equals(BigInteger.ONE)) {
                return candidate;
            }

            candidate = candidate.add(BigInteger.TWO);
        }

        throw new IllegalStateException("Không tìm được e hợp lệ.");
    }

    private void ensureKeysGenerated() {
        if (!hasKeys()) {
            throw new IllegalStateException("Chưa tạo khóa RSA 256-bit demo.");
        }
    }

    private byte[] removeLeadingZero(byte[] bytes) {
        if (bytes.length > 1 && bytes[0] == 0) {
            byte[] result = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, result, 0, result.length);
            return result;
        }

        return bytes;
    }
}