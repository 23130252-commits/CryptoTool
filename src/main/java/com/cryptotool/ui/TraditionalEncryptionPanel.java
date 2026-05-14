package com.cryptotool.ui;

import com.cryptotool.util.FileUtil;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TraditionalEncryptionPanel extends JPanel {
    private JComboBox<String> algorithmCombo;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JPanel keyPanel;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton clearButton;
    private JButton loadFileButton;
    private JButton saveFileButton;
    private JFileChooser fileChooser;
    private JLabel statusLabel;

    public TraditionalEncryptionPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Panel Top: Chọn thuật toán
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Panel Center: Input/Output
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Panel Bottom: Nút điều khiển
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Left: Chọn thuật toán
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Tên Thuật toán"));

        algorithmCombo = new JComboBox<>(new String[]{
                "Caesar - Mã hóa Caesar",
                "Substitution - Thay thế đơn giản",
                "Vigenère - Vigenère Cipher",
                "Atbash - Atbash Cipher"
        });
        algorithmCombo.setPreferredSize(new Dimension(300, 30));
        algorithmCombo.addActionListener(e -> updateKeyPanel());

        leftPanel.add(new JLabel("Chọn thuật toán:"));
        leftPanel.add(algorithmCombo);

        // Right: Key Panel
        keyPanel = createKeyPanel();

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(keyPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createKeyPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Khóa"));

        String algorithm = (String) algorithmCombo.getSelectedItem();
        if (algorithm == null) {
            algorithm = "Caesar - Mã hóa Caesar";
        }

        panel.removeAll();

        if (algorithm.contains("Caesar")) {
            // Caesar: Chỉ cần shift number
            JLabel shiftLabel = new JLabel("Dịch chuyển:");
            JSpinner shiftSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 25, 1));
            shiftSpinner.setPreferredSize(new Dimension(80, 30));
            panel.add(shiftLabel);
            panel.add(shiftSpinner);

        } else if (algorithm.contains("Substitution")) {
            // Substitution: Key là chuỗi 26 ký tự
            JLabel keyLabel = new JLabel("Khóa (26 chữ cái):");
            JTextField keyField = new JTextField("BCDEFGHIJKLMNOPQRSTUVWXYZA", 20);
            keyField.setPreferredSize(new Dimension(250, 30));
            JButton generateButton = new JButton("Tạo ngẫu nhiên");
            generateButton.setPreferredSize(new Dimension(120, 30));
            generateButton.addActionListener(e -> {
                keyField.setText(generateRandomSubstitutionKey());
                statusLabel.setText("✓ Đã tạo khóa ngẫu nhiên");
            });
            panel.add(keyLabel);
            panel.add(keyField);
            panel.add(generateButton);

        } else if (algorithm.contains("Vigenère")) {
            // Vigenère: Key là từ khóa
            JLabel keyLabel = new JLabel("Từ khóa:");
            JTextField keyField = new JTextField("SECRET", 20);
            keyField.setPreferredSize(new Dimension(250, 30));
            panel.add(keyLabel);
            panel.add(keyField);

        } else if (algorithm.contains("Atbash")) {
            // Atbash: Không cần khóa (hoặc khóa cố định)
            JLabel infoLabel = new JLabel("(Atbash không cần khóa - tự động đảo ngược bảng chữ cái)");
            panel.add(infoLabel);
        }

        panel.revalidate();
        panel.repaint();
        return panel;
    }

    private void updateKeyPanel() {
        if (keyPanel != null) {
            keyPanel.removeAll();
            keyPanel = createKeyPanel();
            revalidate();
            repaint();
        }
    }

    private String generateRandomSubstitutionKey() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] chars = alphabet.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int randomIndex = (int) (Math.random() * (i + 1));
            char temp = chars[i];
            chars[i] = chars[randomIndex];
            chars[randomIndex] = temp;
        }
        return new String(chars);
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Input area
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Văn bản"));
        inputTextArea = new JTextArea();
        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inputTextArea.setLineWrap(true);
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Output area
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBorder(BorderFactory.createTitledBorder("Văn bản trả lời"));
        outputTextArea = new JTextArea();
        outputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        panel.add(inputPanel);
        panel.add(outputPanel);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Điều khiển"));

        encryptButton = new JButton("Mã hóa");
        encryptButton.setPreferredSize(new Dimension(100, 35));
        encryptButton.addActionListener(e -> encrypt());

        decryptButton = new JButton("Giải mã");
        decryptButton.setPreferredSize(new Dimension(100, 35));
        decryptButton.addActionListener(e -> decrypt());

        clearButton = new JButton("Xóa");
        clearButton.setPreferredSize(new Dimension(100, 35));
        clearButton.addActionListener(e -> clear());

        loadFileButton = new JButton("Tải tệp");
        loadFileButton.setPreferredSize(new Dimension(100, 35));
        loadFileButton.addActionListener(e -> loadFile());

        saveFileButton = new JButton("Lưu tệp");
        saveFileButton.setPreferredSize(new Dimension(100, 35));
        saveFileButton.addActionListener(e -> saveFile());

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(loadFileButton);
        buttonPanel.add(saveFileButton);

        // Status bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Sẵn sàng");
        statusPanel.add(statusLabel);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void encrypt() {
        String input = inputTextArea.getText();
        if (input.isEmpty()) {
            showError("Vui lòng nhập văn bản");
            return;
        }

        String algorithm = (String) algorithmCombo.getSelectedItem();

        // Lấy khóa từ keyPanel
        Component[] components = keyPanel.getComponents();
        String key = extractKeyFromPanel();

        if (key == null || key.isEmpty()) {
            showError("Vui lòng nhập khóa");
            return;
        }

        try {
            String result = "";

            if (algorithm.contains("Caesar")) {
                int shift = Integer.parseInt(key);
                result = caesarEncrypt(input, shift);
                outputTextArea.setText(result);
                statusLabel.setText("✓ Mã hóa Caesar thành công (Dịch: " + shift + ")");

            } else if (algorithm.contains("Substitution")) {
                result = substitutionEncrypt(input, key);
                outputTextArea.setText(result);
                statusLabel.setText("✓ Mã hóa Thay thế thành công");

            } else if (algorithm.contains("Vigenère")) {
                result = vigenereEncrypt(input, key);
                outputTextArea.setText(result);
                statusLabel.setText("✓ Mã hóa Vigenère thành công");

            } else if (algorithm.contains("Atbash")) {
                result = atbashEncrypt(input);
                outputTextArea.setText(result);
                statusLabel.setText("✓ Mã hóa Atbash thành công");
            }
        } catch (Exception e) {
            showError("Lỗi mã hóa: " + e.getMessage());
        }
    }

    private void decrypt() {
        String input = inputTextArea.getText();
        if (input.isEmpty()) {
            showError("Vui lòng nhập văn bản");
            return;
        }

        String algorithm = (String) algorithmCombo.getSelectedItem();
        String key = extractKeyFromPanel();

        if (key == null || key.isEmpty()) {
            showError("Vui lòng nhập khóa");
            return;
        }

        try {
            String result = "";

            if (algorithm.contains("Caesar")) {
                int shift = Integer.parseInt(key);
                result = caesarDecrypt(input, shift);
                outputTextArea.setText(result);
                statusLabel.setText("✓ Giải mã Caesar thành công");

            } else if (algorithm.contains("Substitution")) {
                result = substitutionDecrypt(input, key);
                outputTextArea.setText(result);
                statusLabel.setText("✓ Giải mã Thay thế thành công");

            } else if (algorithm.contains("Vigenère")) {
                result = vigenereDecrypt(input, key);
                outputTextArea.setText(result);
                statusLabel.setText("✓ Giải mã Vigenère thành công");

            } else if (algorithm.contains("Atbash")) {
                result = atbashDecrypt(input);
                outputTextArea.setText(result);
                statusLabel.setText("✓ Giải mã Atbash thành công");
            }
        } catch (Exception e) {
            showError("Lỗi giải mã: " + e.getMessage());
        }
    }

    private String extractKeyFromPanel() {
        Component[] components = keyPanel.getComponents();

        String algorithm = (String) algorithmCombo.getSelectedItem();

        if (algorithm.contains("Caesar")) {
            for (Component comp : components) {
                if (comp instanceof JSpinner) {
                    return String.valueOf(((JSpinner) comp).getValue());
                }
            }
        } else if (algorithm.contains("Substitution") || algorithm.contains("Vigenère")) {
            for (Component comp : components) {
                if (comp instanceof JTextField) {
                    return ((JTextField) comp).getText();
                }
            }
        }

        return "";
    }

    // Caesar Cipher
    private String caesarEncrypt(String plaintext, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                result.append((char) ((c - base + shift) % 26 + base));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private String caesarDecrypt(String ciphertext, int shift) {
        return caesarEncrypt(ciphertext, 26 - shift);
    }

    // Substitution Cipher
    private String substitutionEncrypt(String plaintext, String key) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();

        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                int index = Character.toUpperCase(c) - 'A';
                char encrypted = key.charAt(index);
                if (Character.isLowerCase(c)) {
                    result.append(Character.toLowerCase(encrypted));
                } else {
                    result.append(encrypted);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private String substitutionDecrypt(String ciphertext, String key) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();

        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                int index = key.indexOf(Character.toUpperCase(c));
                char decrypted = alphabet.charAt(index);
                if (Character.isLowerCase(c)) {
                    result.append(Character.toLowerCase(decrypted));
                } else {
                    result.append(decrypted);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // Vigenère Cipher
    private String vigenereEncrypt(String plaintext, String key) {
        StringBuilder result = new StringBuilder();
        key = key.toUpperCase();
        int keyIndex = 0;

        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int shift = key.charAt(keyIndex % key.length()) - 'A';
                result.append((char) ((c - base + shift) % 26 + base));
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private String vigenereDecrypt(String ciphertext, String key) {
        StringBuilder result = new StringBuilder();
        key = key.toUpperCase();
        int keyIndex = 0;

        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int shift = key.charAt(keyIndex % key.length()) - 'A';
                result.append((char) ((c - base - shift + 26) % 26 + base));
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // Atbash Cipher
    private String atbashEncrypt(String plaintext) {
        StringBuilder result = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                result.append((char) (base + 'Z' - c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private String atbashDecrypt(String ciphertext) {
        return atbashEncrypt(ciphertext);
    }

    private void clear() {
        inputTextArea.setText("");
        outputTextArea.setText("");
        statusLabel.setText("Đã xóa");
    }

    private void loadFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                String content = FileUtil.readFile(file.getAbsolutePath());
                inputTextArea.setText(content);
                statusLabel.setText("✓ Tải tệp thành công: " + file.getName());
            } catch (Exception e) {
                showError("Lỗi tải tệp: " + e.getMessage());
            }
        }
    }

    private void saveFile() {
        String content = outputTextArea.getText();
        if (content.isEmpty()) {
            showError("Không có dữ liệu để lưu");
            return;
        }
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                FileUtil.writeFile(file.getAbsolutePath(), content);
                statusLabel.setText("✓ Lưu tệp thành công: " + file.getName());
            } catch (Exception e) {
                showError("Lỗi lưu tệp: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("✗ Lỗi: " + message);
    }
}
