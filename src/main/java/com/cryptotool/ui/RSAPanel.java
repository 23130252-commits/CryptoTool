package com.cryptotool.ui;

import com.cryptotool.service.rsa.HybridEncryptionService;
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

    private JRadioButton textRadioButton;
    private JRadioButton fileRadioButton;

    private JTextArea publicKeyArea;
    private JTextArea privateKeyArea;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;

    private JTextField filePathField;

    private JButton generateKeysButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton clearButton;
    private JButton copyButton;
    private JButton chooseFileButton;
    private JButton savePublicKeyButton;
    private JButton savePrivateKeyButton;
    private JButton loadPublicKeyButton;
    private JButton loadPrivateKeyButton;

    private JFileChooser fileChooser;
    private JLabel statusLabel;

    private File selectedFile;

    private RSAService rsaService;
    private RSA256DemoService rsa256DemoService;
    private HybridEncryptionService hybridEncryptionService;

    public RSAPanel() {
        this.rsaService = new RSAService(2048);
        this.rsa256DemoService = new RSA256DemoService();
        this.hybridEncryptionService = new HybridEncryptionService();

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
        updateInputMode();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cấu hình RSA"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rsaModeCombo = new JComboBox<>(new String[]{
                "RSA 256-bit",
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

        textRadioButton = new JRadioButton("Văn bản", true);
        fileRadioButton = new JRadioButton("File");

        ButtonGroup inputGroup = new ButtonGroup();
        inputGroup.add(textRadioButton);
        inputGroup.add(fileRadioButton);

        textRadioButton.addActionListener(e -> updateInputMode());
        fileRadioButton.addActionListener(e -> updateInputMode());

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
        panel.add(new JLabel("Kiểu dữ liệu:"), gbc);

        gbc.gridx = 1;
        panel.add(textRadioButton, gbc);

        gbc.gridx = 2;
        panel.add(fileRadioButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
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
        inputPanel.setBorder(BorderFactory.createTitledBorder("Văn bản / File đầu vào"));

        inputTextArea = new JTextArea();
        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);

        JPanel filePanel = new JPanel(new BorderLayout(5, 5));

        filePathField = new JTextField();
        filePathField.setEditable(false);

        chooseFileButton = new JButton("Chọn file");
        chooseFileButton.addActionListener(e -> chooseFile());

        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(chooseFileButton, BorderLayout.EAST);

        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);
        inputPanel.add(filePanel, BorderLayout.SOUTH);

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
        if (keySizeCombo == null || fileRadioButton == null || statusLabel == null) {
            return;
        }

        boolean demo256 = isRSA256DemoMode();

        keySizeCombo.setEnabled(!demo256);

        loadPublicKeyButton.setEnabled(!demo256);
        loadPrivateKeyButton.setEnabled(!demo256);

        savePublicKeyButton.setEnabled(true);
        savePrivateKeyButton.setEnabled(true);

        fileRadioButton.setEnabled(!demo256);

        if (demo256 && fileRadioButton.isSelected()) {
            textRadioButton.setSelected(true);
        }

        updateInputMode();

        if (demo256) {
            statusLabel.setText("RSA 256-bit demo chỉ dùng cho văn bản ngắn, không hỗ trợ file.");
        } else {
            statusLabel.setText("RSA chuẩn hỗ trợ văn bản và file bằng Hybrid AES + RSA.");
        }
    }

    private void updateInputMode() {
        if (inputTextArea == null || chooseFileButton == null || filePathField == null) {
            return;
        }

        boolean textMode = textRadioButton.isSelected();

        inputTextArea.setEnabled(textMode);
        inputTextArea.setEditable(textMode);

        chooseFileButton.setEnabled(!textMode);
        filePathField.setEnabled(!textMode);

        if (statusLabel != null) {
            if (textMode) {
                statusLabel.setText("Chế độ RSA văn bản.");
            } else {
                statusLabel.setText("Chế độ RSA file: dùng Hybrid AES + RSA.");
            }
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
        if (textRadioButton.isSelected()) {
            encryptText();
        } else {
            encryptFile();
        }
    }

    private void decrypt() {
        if (textRadioButton.isSelected()) {
            decryptText();
        } else {
            decryptFile();
        }
    }

    private void encryptText() {
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
            statusLabel.setText("Mã hóa RSA văn bản thành công.");
        } catch (Exception e) {
            showError("Lỗi mã hóa RSA: " + e.getMessage());
        }
    }

    private void decryptText() {
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
            statusLabel.setText("Giải mã RSA văn bản thành công.");
        } catch (Exception e) {
            showError("Lỗi giải mã RSA: " + e.getMessage());
        }
    }

    private void encryptFile() {
        try {
            if (isRSA256DemoMode()) {
                throw new IllegalArgumentException("RSA 256-bit demo không hỗ trợ mã hóa file.");
            }

            if (selectedFile == null) {
                throw new IllegalArgumentException("Vui lòng chọn file cần mã hóa.");
            }

            String publicKeyBase64 = publicKeyArea.getText();

            if (publicKeyBase64 == null || publicKeyBase64.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập hoặc tạo public key.");
            }

            PublicKey publicKey = rsaService.getPublicKeyFromBase64(publicKeyBase64.trim());

            File outputFile = hybridEncryptionService.encryptFile(selectedFile, publicKey);

            outputTextArea.setText(
                    "Mã hóa file RSA Hybrid thành công.\n"
                            + "File gốc: " + selectedFile.getAbsolutePath() + "\n"
                            + "File mã hóa: " + outputFile.getAbsolutePath()
            );

            statusLabel.setText("Đã mã hóa file: " + outputFile.getName());
        } catch (Exception e) {
            showError("Lỗi mã hóa file RSA: " + e.getMessage());
        }
    }

    private void decryptFile() {
        try {
            if (isRSA256DemoMode()) {
                throw new IllegalArgumentException("RSA 256-bit demo không hỗ trợ giải mã file.");
            }

            if (selectedFile == null) {
                throw new IllegalArgumentException("Vui lòng chọn file .rsaenc cần giải mã.");
            }

            String privateKeyBase64 = privateKeyArea.getText();

            if (privateKeyBase64 == null || privateKeyBase64.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập hoặc tạo private key.");
            }

            PrivateKey privateKey = rsaService.getPrivateKeyFromBase64(privateKeyBase64.trim());

            File outputFile = hybridEncryptionService.decryptFile(selectedFile, privateKey);

            outputTextArea.setText(
                    "Giải mã file RSA Hybrid thành công.\n"
                            + "File mã hóa: " + selectedFile.getAbsolutePath() + "\n"
                            + "File giải mã: " + outputFile.getAbsolutePath()
            );

            statusLabel.setText("Đã giải mã file: " + outputFile.getName());
        } catch (Exception e) {
            showError("Lỗi giải mã file RSA: " + e.getMessage());
        }
    }

    private void chooseFile() {
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
            statusLabel.setText("Đã chọn file: " + selectedFile.getName());
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
        selectedFile = null;

        publicKeyArea.setText("");
        privateKeyArea.setText("");
        inputTextArea.setText("");
        outputTextArea.setText("");
        filePathField.setText("");

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