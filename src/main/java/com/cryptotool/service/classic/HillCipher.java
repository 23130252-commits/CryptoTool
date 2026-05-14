package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

public class HillCipher {
    private Alphabet alphabet;

    public HillCipher(AlphabetType alphabetType) {
        this.alphabet = AlphabetRepository.getAlphabet(alphabetType);
    }

    /**
     * Tính định thức của ma trận 2x2
     */
    private int determinant2x2(int[][] matrix) {
        return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]);
    }

    /**
     * Tính modular inverse
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
     * Kiểm tra ma trận khóa có hợp lệ không
     */
    private boolean isValidKeyMatrix(int[][] keyMatrix) {
        int n = alphabet.getSize();
        int det = determinant2x2(keyMatrix);
        int detMod = ((det % n) + n) % n;
        return modInverse(detMod, n) != -1;
    }

    /**
     * Mã hóa Hill Cipher (2x2)
     */
    public String encrypt(String plaintext, int[][] keyMatrix) throws Exception {
        if (!isValidKeyMatrix(keyMatrix)) {
            throw new Exception("Key matrix không hợp lệ! Định thức phải nguyên tố cùng nhau với kích thước bảng chữ cái.");
        }

        StringBuilder result = new StringBuilder();
        int n = alphabet.getSize();

        // Xử lý từng cặp ký tự
        for (int i = 0; i < plaintext.length(); i += 2) {
            if (i + 1 < plaintext.length()) {
                char c1 = plaintext.charAt(i);
                char c2 = plaintext.charAt(i + 1);

                if (alphabet.contains(c1) && alphabet.contains(c2)) {
                    int p1 = alphabet.getIndex(c1);
                    int p2 = alphabet.getIndex(c2);

                    int e1 = (keyMatrix[0][0] * p1 + keyMatrix[0][1] * p2) % n;
                    int e2 = (keyMatrix[1][0] * p1 + keyMatrix[1][1] * p2) % n;

                    if (e1 < 0) e1 += n;
                    if (e2 < 0) e2 += n;

                    result.append(alphabet.getCharAt(e1));
                    result.append(alphabet.getCharAt(e2));
                }
            } else {
                result.append(plaintext.charAt(i));
            }
        }

        return result.toString();
    }

    /**
     * Giải mã Hill Cipher (2x2)
     */
    public String decrypt(String ciphertext, int[][] keyMatrix) throws Exception {
        if (!isValidKeyMatrix(keyMatrix)) {
            throw new Exception("Key matrix không hợp lệ!");
        }

        StringBuilder result = new StringBuilder();
        int n = alphabet.getSize();
        int det = determinant2x2(keyMatrix);
        int detMod = ((det % n) + n) % n;
        int detInverse = modInverse(detMod, n);

        // Tính inverse matrix
        int[][] inverseMatrix = new int[2][2];
        inverseMatrix[0][0] = (keyMatrix[1][1] * detInverse) % n;
        inverseMatrix[0][1] = (-keyMatrix[0][1] * detInverse % n + n) % n;
        inverseMatrix[1][0] = (-keyMatrix[1][0] * detInverse % n + n) % n;
        inverseMatrix[1][1] = (keyMatrix[0][0] * detInverse) % n;

        // Xử lý từng cặp ký tự
        for (int i = 0; i < ciphertext.length(); i += 2) {
            if (i + 1 < ciphertext.length()) {
                char c1 = ciphertext.charAt(i);
                char c2 = ciphertext.charAt(i + 1);

                if (alphabet.contains(c1) && alphabet.contains(c2)) {
                    int e1 = alphabet.getIndex(c1);
                    int e2 = alphabet.getIndex(c2);

                    int p1 = (inverseMatrix[0][0] * e1 + inverseMatrix[0][1] * e2) % n;
                    int p2 = (inverseMatrix[1][0] * e1 + inverseMatrix[1][1] * e2) % n;

                    if (p1 < 0) p1 += n;
                    if (p2 < 0) p2 += n;

                    result.append(alphabet.getCharAt(p1));
                    result.append(alphabet.getCharAt(p2));
                }
            } else {
                result.append(ciphertext.charAt(i));
            }
        }

        return result.toString();
    }
}
