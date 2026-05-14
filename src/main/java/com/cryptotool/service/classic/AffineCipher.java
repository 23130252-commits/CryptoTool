package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

public class AffineCipher {
    private Alphabet alphabet;

    public AffineCipher(AlphabetType alphabetType) {
        this.alphabet = AlphabetRepository.getAlphabet(alphabetType);
    }

    /**
     * Tính GCD (Greatest Common Divisor)
     */
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /**
     * Tính modular multiplicative inverse của a mod m
     */
    private int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1;
    }

    /**
     * Mã hóa Affine Cipher
     * key là "a,b" (ví dụ: "5,8")
     * E(x) = (a*x + b) mod n
     */
    public String encrypt(String plaintext, String key) throws Exception {
        int[] ab = parseKey(key);
        int a = ab[0];
        int b = ab[1];
        int n = alphabet.getSize();

        // Kiểm tra gcd(a, n) = 1
        if (gcd(a, n) != 1) {
            throw new Exception("gcd(a, n) phải bằng 1! a=" + a + ", n=" + n);
        }

        StringBuilder result = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            if (alphabet.contains(c)) {
                int x = alphabet.getIndex(c);
                int encrypted = (a * x + b) % n;
                result.append(alphabet.getCharAt(encrypted));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Giải mã Affine Cipher
     * D(y) = a^-1 * (y - b) mod n
     */
    public String decrypt(String ciphertext, String key) throws Exception {
        int[] ab = parseKey(key);
        int a = ab[0];
        int b = ab[1];
        int n = alphabet.getSize();

        // Kiểm tra gcd(a, n) = 1
        if (gcd(a, n) != 1) {
            throw new Exception("gcd(a, n) phải bằng 1! a=" + a + ", n=" + n);
        }

        int aInverse = modInverse(a, n);
        if (aInverse == -1) {
            throw new Exception("Không tìm được modular inverse của a!");
        }

        StringBuilder result = new StringBuilder();
        for (char c : ciphertext.toCharArray()) {
            if (alphabet.contains(c)) {
                int y = alphabet.getIndex(c);
                int decrypted = (aInverse * (y - b)) % n;
                if (decrypted < 0) {
                    decrypted += n;
                }
                result.append(alphabet.getCharAt(decrypted));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Parse key từ định dạng "a,b"
     */
    private int[] parseKey(String key) throws Exception {
        if (key == null || key.trim().isEmpty()) {
            throw new Exception("Key không được để trống!");
        }

        String[] parts = key.trim().split(",");
        if (parts.length != 2) {
            throw new Exception("Key phải có định dạng 'a,b' (ví dụ: '5,8')");
        }

        try {
            int a = Integer.parseInt(parts[0].trim());
            int b = Integer.parseInt(parts[1].trim());
            return new int[]{a, b};
        } catch (NumberFormatException e) {
            throw new Exception("Key phải chứa các số nguyên!");
        }
    }
}
