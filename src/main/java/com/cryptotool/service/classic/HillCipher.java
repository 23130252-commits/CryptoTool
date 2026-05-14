package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

public class HillCipher {
    private final Alphabet alphabet;

    public HillCipher() {
        this(AlphabetType.ENGLISH);
    }

    public HillCipher(AlphabetType alphabetType) {
        // Hill Cipher trong đồ án chỉ dùng bảng English A-Z để dễ kiểm tra ma trận nghịch đảo modulo 26.
        this.alphabet = AlphabetRepository.getEnglishUppercaseAlphabet();
    }

    public String encrypt(String plaintext, int[][] keyMatrix) {
        validateMatrix(keyMatrix);

        if (plaintext == null) {
            throw new IllegalArgumentException("Văn bản cần mã hóa không được null.");
        }

        String preparedText = prepareTextForEncrypt(plaintext);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < preparedText.length(); i += 2) {
            int x1 = alphabet.indexOf(preparedText.charAt(i));
            int x2 = alphabet.indexOf(preparedText.charAt(i + 1));

            int y1 = keyMatrix[0][0] * x1 + keyMatrix[0][1] * x2;
            int y2 = keyMatrix[1][0] * x1 + keyMatrix[1][1] * x2;

            result.append(alphabet.charAt(y1));
            result.append(alphabet.charAt(y2));
        }

        return result.toString();
    }

    public String decrypt(String ciphertext, int[][] keyMatrix) {
        validateMatrix(keyMatrix);

        if (ciphertext == null) {
            throw new IllegalArgumentException("Văn bản cần giải mã không được null.");
        }

        String preparedText = prepareTextForDecrypt(ciphertext);
        int[][] inverseMatrix = inverseMatrix(keyMatrix);

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < preparedText.length(); i += 2) {
            int y1 = alphabet.indexOf(preparedText.charAt(i));
            int y2 = alphabet.indexOf(preparedText.charAt(i + 1));

            int x1 = inverseMatrix[0][0] * y1 + inverseMatrix[0][1] * y2;
            int x2 = inverseMatrix[1][0] * y1 + inverseMatrix[1][1] * y2;

            result.append(alphabet.charAt(x1));
            result.append(alphabet.charAt(x2));
        }

        return removePaddingX(result.toString());
    }

    private String prepareTextForEncrypt(String input) {
        StringBuilder result = new StringBuilder();

        input = input.toUpperCase();

        for (char ch : input.toCharArray()) {
            if (alphabet.contains(ch)) {
                result.append(ch);
            }
        }

        if (result.length() == 0) {
            throw new IllegalArgumentException("Hill Cipher chỉ hỗ trợ chữ cái English A-Z.");
        }

        if (result.length() % 2 != 0) {
            result.append('X');
        }

        return result.toString();
    }

    private String prepareTextForDecrypt(String input) {
        StringBuilder result = new StringBuilder();

        input = input.toUpperCase();

        for (char ch : input.toCharArray()) {
            if (alphabet.contains(ch)) {
                result.append(ch);
            }
        }

        if (result.length() == 0) {
            throw new IllegalArgumentException("Ciphertext Hill chỉ được chứa chữ cái English A-Z.");
        }

        if (result.length() % 2 != 0) {
            throw new IllegalArgumentException("Ciphertext Hill phải có số lượng ký tự chẵn.");
        }

        return result.toString();
    }

    private void validateMatrix(int[][] matrix) {
        if (matrix == null || matrix.length != 2) {
            throw new IllegalArgumentException("Ma trận Hill phải là ma trận 2x2.");
        }

        if (matrix[0] == null || matrix[1] == null
                || matrix[0].length != 2 || matrix[1].length != 2) {
            throw new IllegalArgumentException("Ma trận Hill phải là ma trận 2x2.");
        }

        int determinant = determinant(matrix);
        int n = alphabet.size();

        if (gcd(determinant, n) != 1) {
            throw new IllegalArgumentException(
                    "Ma trận không khả nghịch modulo " + n
                            + ". Cần gcd(det, " + n + ") = 1. det = " + determinant
            );
        }
    }

    private int[][] inverseMatrix(int[][] matrix) {
        int n = alphabet.size();

        int a = matrix[0][0];
        int b = matrix[0][1];
        int c = matrix[1][0];
        int d = matrix[1][1];

        int det = determinant(matrix);
        int detInverse = modInverse(det, n);

        int[][] inverse = new int[2][2];

        inverse[0][0] = mod(detInverse * d, n);
        inverse[0][1] = mod(detInverse * (-b), n);
        inverse[1][0] = mod(detInverse * (-c), n);
        inverse[1][1] = mod(detInverse * a, n);

        return inverse;
    }

    private int determinant(int[][] matrix) {
        int a = matrix[0][0];
        int b = matrix[0][1];
        int c = matrix[1][0];
        int d = matrix[1][1];

        return a * d - b * c;
    }

    private int modInverse(int value, int modulo) {
        value = mod(value, modulo);

        for (int x = 1; x < modulo; x++) {
            if ((value * x) % modulo == 1) {
                return x;
            }
        }

        throw new IllegalArgumentException("Không tìm được nghịch đảo modulo.");
    }

    private int mod(int value, int modulo) {
        return ((value % modulo) + modulo) % modulo;
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

    private String removePaddingX(String text) {
        if (text.endsWith("X")) {
            return text.substring(0, text.length() - 1);
        }

        return text;
    }
}