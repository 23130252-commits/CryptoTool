package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

public class AffineCipher {
    private final Alphabet alphabet;

    public AffineCipher() {
        this(AlphabetType.ENGLISH);
    }

    public AffineCipher(AlphabetType alphabetType) {
        this.alphabet = AlphabetRepository.getAlphabet(alphabetType);
    }

    public String encrypt(String plaintext, String key) {
        int[] parsedKey = parseKey(key);
        return encrypt(plaintext, parsedKey[0], parsedKey[1]);
    }

    public String decrypt(String ciphertext, String key) {
        int[] parsedKey = parseKey(key);
        return decrypt(ciphertext, parsedKey[0], parsedKey[1]);
    }

    public String encrypt(String plaintext, int a, int b) {
        validateA(a);

        if (plaintext == null) {
            throw new IllegalArgumentException("Văn bản cần mã hóa không được null.");
        }

        plaintext = alphabet.normalizeText(plaintext);

        int n = alphabet.size();
        StringBuilder result = new StringBuilder();

        for (char ch : plaintext.toCharArray()) {
            int x = alphabet.indexOf(ch);

            if (x == -1) {
                result.append(ch);
                continue;
            }

            int encryptedIndex = mod(a * x + b, n);
            result.append(alphabet.charAt(encryptedIndex));
        }

        return result.toString();
    }

    public String decrypt(String ciphertext, int a, int b) {
        validateA(a);

        if (ciphertext == null) {
            throw new IllegalArgumentException("Văn bản cần giải mã không được null.");
        }

        ciphertext = alphabet.normalizeText(ciphertext);

        int n = alphabet.size();
        int inverseA = modInverse(a, n);

        StringBuilder result = new StringBuilder();

        for (char ch : ciphertext.toCharArray()) {
            int y = alphabet.indexOf(ch);

            if (y == -1) {
                result.append(ch);
                continue;
            }

            int decryptedIndex = mod(inverseA * (y - b), n);
            result.append(alphabet.charAt(decryptedIndex));
        }

        return result.toString();
    }

    private int[] parseKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key Affine không được để trống. Dạng đúng: a,b. Ví dụ: 5,8");
        }

        String[] parts = key.trim().split(",");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Key Affine phải có dạng a,b. Ví dụ: 5,8");
        }

        try {
            int a = Integer.parseInt(parts[0].trim());
            int b = Integer.parseInt(parts[1].trim());

            return new int[]{a, b};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Key Affine chỉ được chứa số nguyên. Ví dụ: 5,8");
        }
    }

    private void validateA(int a) {
        int n = alphabet.size();

        if (gcd(a, n) != 1) {
            throw new IllegalArgumentException(
                    "Key a không hợp lệ. Cần gcd(a, " + n + ") = 1."
            );
        }
    }

    private int modInverse(int a, int n) {
        a = mod(a, n);

        for (int x = 1; x < n; x++) {
            if (mod(a * x, n) == 1) {
                return x;
            }
        }

        throw new IllegalArgumentException("Không tìm được nghịch đảo modulo của a.");
    }

    private int mod(int value, int n) {
        return ((value % n) + n) % n;
    }

    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }
}