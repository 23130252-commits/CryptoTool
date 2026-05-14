package com.cryptotool.ui;

import com.cryptotool.service.rsa.RSA256DemoService;
import com.cryptotool.service.rsa.RSAService;
import com.cryptotool.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAPanel extends JPanel {
    private JComboBox<String> rsaModeCombo;
    private JComboBox<String> keySizeCombo;

    private JTextArea publicKeyArea;
    private JTextArea privateKeyArea;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;

    private JButton generateKeysButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton clearButton;
    private JButton copyButton;
    private JButton savePublicKeyButton;
    private JButton savePrivateKeyButton;
    private JButton loadPublicKeyButton;
    private JButton loadPrivateKeyButton;

    private JFileChooser fileChooser;
    private JLabel statusLabel;

    private RSAService rsaService;
    private RSA256DemoService rsa256DemoService;

    public RSAPanel() {
        this.rsaService = new RSAService(2048);
        this.rsa256DemoService = new RSA256DemoService();

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        updateMode();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cấu hình RSA"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rsaModeCombo = new JComboBox<>(new String[]{
                "RSA 256-bit demo",
                "RSA chuẩn"
        });
        rsaModeCombo.setPreferredSize(new Dimension(170, 30));
        rsaModeCombo.addActionListener(e -> updateMode());

        keySizeCombo = new JComboBox<>(new String[]{
                "1024",
                "2048"
        });
        keySizeCombo.setSelectedItem("2048");
        keySizeCombo.setPreferredSize(new Dimension(100, 30));

        generateKeysButton = new JButton("Tạo cặp khóa");
        generateKeysButton.setPreferredSize(new Dimension(130, 35));
        generateKeysButton.addActionListener(e -> generateKeys());

        loadPublicKeyButton = new JButton("Tải public key");
        loadPublicKeyButton.addActionListener(e -> loadPublicKey());

        loadPrivateKeyButton = new JButton("Tải private key");
        loadPrivateKeyButton.addActionListener(e -> loadPrivateKey());

        savePublicKeyButton = new JButton("Lưu public key");
        savePublicKeyButton.addActionListener(e -> savePublicKey());

        savePrivateKeyButton = new JButton("Lưu private key");
        savePrivateKeyButton.addActionListener(e -> savePrivateKey());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Chế độ:"), gbc);

        gbc.gridx = 1;
        panel.add(rsaModeCombo, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Key size:"), gbc);

        gbc.gridx = 3;
        panel.add(keySizeCombo, gbc);

        gbc.gridx = 4;
        panel.add(generateKeysButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(loadPublicKeyButton, gbc);

        gbc.gridx = 1;
        panel.add(loadPrivateKeyButton, gbc);

        gbc.gridx = 2;
        panel.add(savePublicKeyButton, gbc);

        gbc.gridx = 3;
        panel.add(savePrivateKeyButton, gbc);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JPanel publicKeyPanel = new JPanel(new BorderLayout(5, 5));
        publicKeyPanel.setBorder(BorderFactory.createTitledBorder("Public Key"));

        publicKeyArea = new JTextArea();
        publicKeyArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        publicKeyArea.setLineWrap(true);
        publicKeyArea.setWrapStyleWord(true);

        publicKeyPanel.add(new JScrollPane(publicKeyArea), BorderLayout.CENTER);

        JPanel privateKeyPanel = new JPanel(new BorderLayout(5, 5));
        privateKeyPanel.setBorder(BorderFactory.createTitledBorder("Private Key"));

        privateKeyArea = new JTextArea();
        privateKeyArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        privateKeyArea.setLineWrap(true);
        privateKeyArea.setWrapStyleWord(true);

        privateKeyPanel.add(new JScrollPane(privateKeyArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Văn bản đầu vào"));

        inputTextArea = new JTextArea();
        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);

        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBorder(BorderFactory.createTitledBorder("Kết quả"));

        outputTextArea = new JTextArea();
        outputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);

        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        panel.add(publicKeyPanel);
        panel.add(privateKeyPanel);
        panel.add(inputPanel);
        panel.add(outputPanel);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Điều khiển"));

        encryptButton = new JButton("Mã hóa");
        encryptButton.setPreferredSize(new Dimension(100, 35));
        encryptButton.addActionListener(e -> encrypt());

        decryptButton = new JButton("Giải mã");
        decryptButton.setPreferredSize(new Dimension(100, 35));
        decryptButton.addActionListener(e -> decrypt());

        clearButton = new JButton("Xóa");
        clearButton.setPreferredSize(new Dimension(90, 35));
        clearButton.addActionListener(e -> clear());

        copyButton = new JButton("Sao chép");
        copyButton.setPreferredSize(new Dimension(100, 35));
        copyButton.addActionListener(e -> copyResult());

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(copyButton);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Sẵn sàng");
        statusPanel.add(statusLabel);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateMode() {
        boolean demo256 = isRSA256DemoMode();

        keySizeCombo.setEnabled(!demo256);

        loadPublicKeyButton.setEnabled(!demo256);
        loadPrivateKeyButton.setEnabled(!demo256);
        savePublicKeyButton.setEnabled(true);
        savePrivateKeyButton.setEnabled(true);

        if (demo256) {
            statusLabel.setText("RSA 256-bit demo chỉ dùng cho chuỗi ngắn hoặc số.");
        } else {
            statusLabel.setText("RSA chuẩn dùng 1024/2048-bit.");
        }
    }

    private void generateKeys() {
        try {
            if (isRSA256DemoMode()) {
                rsa256DemoService.generateKeys();

                publicKeyArea.setText(rsa256DemoService.getPublicKeyText());
                privateKeyArea.setText(rsa256DemoService.getPrivateKeyText());

                statusLabel.setText("Đã tạo khóa RSA 256-bit demo.");
                return;
            }

            int keySize = Integer.parseInt((String) keySizeCombo.getSelectedItem());

            rsaService = new RSAService(keySize);
            rsaService.generateKeyPair();

            publicKeyArea.setText(rsaService.getPublicKeyBase64());
            privateKeyArea.setText(rsaService.getPrivateKeyBase64());

            statusLabel.setText("Đã tạo khóa RSA " + keySize + "-bit.");
        } catch (Exception e) {
            showError("Lỗi tạo khóa: " + e.getMessage());
        }
    }

    private void encrypt() {
        try {
            String input = inputTextArea.getText();

            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập văn bản cần mã hóa.");
            }

            if (isRSA256DemoMode()) {
                String cipherText = rsa256DemoService.encrypt(input);
                outputTextArea.setText(cipherText);
                statusLabel.setText("Mã hóa RSA 256-bit demo thành công.");
                return;
            }

            String publicKeyBase64 = publicKeyArea.getText();

            if (publicKeyBase64 == null || publicKeyBase64.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập hoặc tạo public key.");
            }

            PublicKey publicKey = rsaService.getPublicKeyFromBase64(publicKeyBase64.trim());
            String cipherText = rsaService.encrypt(input, publicKey);

            outputTextArea.setText(cipherText);
            statusLabel.setText("Mã hóa RSA thành công.");
        } catch (Exception e) {
            showError("Lỗi mã hóa RSA: " + e.getMessage());
        }
    }

    private void decrypt() {
        try {
            String input = inputTextArea.getText();

            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập dữ liệu cần giải mã.");
            }

            if (isRSA256DemoMode()) {
                String plainText = rsa256DemoService.decrypt(input);
                outputTextArea.setText(plainText);
                statusLabel.setText("Giải mã RSA 256-bit demo thành công.");
                return;
            }

            String privateKeyBase64 = privateKeyArea.getText();

            if (privateKeyBase64 == null || privateKeyBase64.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập hoặc tạo private key.");
            }

            PrivateKey privateKey = rsaService.getPrivateKeyFromBase64(privateKeyBase64.trim());
            String plainText = rsaService.decrypt(input, privateKey);

            outputTextArea.setText(plainText);
            statusLabel.setText("Giải mã RSA thành công.");
        } catch (Exception e) {
            showError("Lỗi giải mã RSA: " + e.getMessage());
        }
    }

    private void loadPublicKey() {
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                String content = FileUtil.readFile(file.getAbsolutePath());
                publicKeyArea.setText(content.trim());
                statusLabel.setText("Đã tải public key: " + file.getName());
            } catch (Exception e) {
                showError("Lỗi tải public key: " + e.getMessage());
            }
        }
    }

    private void loadPrivateKey() {
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                String content = FileUtil.readFile(file.getAbsolutePath());
                privateKeyArea.setText(content.trim());
                statusLabel.setText("Đã tải private key: " + file.getName());
            } catch (Exception e) {
                showError("Lỗi tải private key: " + e.getMessage());
            }
        }
    }

    private void savePublicKey() {
        saveTextToFile(publicKeyArea.getText(), "public_key.txt", "public key");
    }

    private void savePrivateKey() {
        saveTextToFile(privateKeyArea.getText(), "private_key.txt", "private key");
    }

    private void saveTextToFile(String content, String defaultFileName, String label) {
        if (content == null || content.trim().isEmpty()) {
            showError("Không có " + label + " để lưu.");
            return;
        }

        fileChooser.setSelectedFile(new File(defaultFileName));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                FileUtil.writeFile(file.getAbsolutePath(), content.trim());
                statusLabel.setText("Đã lưu " + label + ": " + file.getName());
            } catch (Exception e) {
                showError("Lỗi lưu " + label + ": " + e.getMessage());
            }
        }
    }

    private void clear() {
        publicKeyArea.setText("");
        privateKeyArea.setText("");
        inputTextArea.setText("");
        outputTextArea.setText("");
        statusLabel.setText("Đã xóa dữ liệu");
    }

    private void copyResult() {
        String text = outputTextArea.getText();

        if (text == null || text.isEmpty()) {
            showError("Không có kết quả để sao chép.");
            return;
        }

        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);

        statusLabel.setText("Đã sao chép kết quả");
    }

    private boolean isRSA256DemoMode() {
        String selected = (String) rsaModeCombo.getSelectedItem();
        return selected != null && selected.startsWith("RSA 256");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Lỗi: " + message);
    }
}