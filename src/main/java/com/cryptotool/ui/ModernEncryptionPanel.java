package com.cryptotool.ui;

import com.cryptotool.service.modern.KeyIvService;
import com.cryptotool.service.modern.SymmetricCipherService;
import com.cryptotool.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.Base64;

public class ModernEncryptionPanel extends JPanel {
    private JComboBox<String> algorithmCombo;
    private JComboBox<String> modeCombo;
    private JComboBox<String> keySizeCombo;

    private JRadioButton textRadioButton;
    private JRadioButton fileRadioButton;

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTextArea keyTextArea;
    private JTextArea ivTextArea;

    private JTextField filePathField;

    private JButton encryptButton;
    private JButton decryptButton;
    private JButton clearButton;
    private JButton chooseFileButton;
    private JButton saveResultButton;
    private JButton copyResultButton;
    private JButton generateKeyButton;
    private JButton generateIvButton;

    private JFileChooser fileChooser;
    private JLabel statusLabel;

    private File selectedFile;

    private final SymmetricCipherService cipherService;
    private final KeyIvService keyIvService;

    public ModernEncryptionPanel() {
        this.cipherService = new SymmetricCipherService();
        this.keyIvService = new KeyIvService();

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

        updateAlgorithmConfig();
        updateInputMode();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cấu hình mã hóa hiện đại"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        algorithmCombo = new JComboBox<>(new String[]{
                "AES",
                "DESede / 3DES"
        });
        algorithmCombo.setPreferredSize(new Dimension(160, 30));
        algorithmCombo.addActionListener(e -> updateAlgorithmConfig());

        modeCombo = new JComboBox<>(new String[]{
                "CBC",
                "ECB"
        });
        modeCombo.setPreferredSize(new Dimension(100, 30));
        modeCombo.addActionListener(e -> updateIvState());

        keySizeCombo = new JComboBox<>(new String[]{
                "128",
                "192",
                "256"
        });
        keySizeCombo.setPreferredSize(new Dimension(90, 30));

        textRadioButton = new JRadioButton("Văn bản", true);
        fileRadioButton = new JRadioButton("File");

        ButtonGroup inputTypeGroup = new ButtonGroup();
        inputTypeGroup.add(textRadioButton);
        inputTypeGroup.add(fileRadioButton);

        textRadioButton.addActionListener(e -> updateInputMode());
        fileRadioButton.addActionListener(e -> updateInputMode());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Thuật toán:"), gbc);

        gbc.gridx = 1;
        panel.add(algorithmCombo, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Mode:"), gbc);

        gbc.gridx = 3;
        panel.add(modeCombo, gbc);

        gbc.gridx = 4;
        panel.add(new JLabel("Key size:"), gbc);

        gbc.gridx = 5;
        panel.add(keySizeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Kiểu dữ liệu:"), gbc);

        gbc.gridx = 1;
        panel.add(textRadioButton, gbc);

        gbc.gridx = 2;
        panel.add(fileRadioButton, gbc);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel keyPanel = createKeyPanel();
        JPanel dataPanel = createDataPanel();

        mainPanel.add(keyPanel, BorderLayout.NORTH);
        mainPanel.add(dataPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createKeyPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel keyBoxPanel = new JPanel(new BorderLayout(5, 5));
        keyBoxPanel.setBorder(BorderFactory.createTitledBorder("Key Base64"));

        keyTextArea = new JTextArea(3, 30);
        keyTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        keyTextArea.setLineWrap(true);
        keyTextArea.setWrapStyleWord(true);

        generateKeyButton = new JButton("Tạo key");
        generateKeyButton.addActionListener(e -> generateKey());

        keyBoxPanel.add(new JScrollPane(keyTextArea), BorderLayout.CENTER);
        keyBoxPanel.add(generateKeyButton, BorderLayout.EAST);

        JPanel ivBoxPanel = new JPanel(new BorderLayout(5, 5));
        ivBoxPanel.setBorder(BorderFactory.createTitledBorder("IV Base64"));

        ivTextArea = new JTextArea(3, 30);
        ivTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ivTextArea.setLineWrap(true);
        ivTextArea.setWrapStyleWord(true);

        generateIvButton = new JButton("Tạo IV");
        generateIvButton.addActionListener(e -> generateIv());

        ivBoxPanel.add(new JScrollPane(ivTextArea), BorderLayout.CENTER);
        ivBoxPanel.add(generateIvButton, BorderLayout.EAST);

        panel.add(keyBoxPanel);
        panel.add(ivBoxPanel);

        return panel;
    }

    private JPanel createDataPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

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

        saveResultButton = new JButton("Lưu kết quả");
        saveResultButton.setPreferredSize(new Dimension(120, 35));
        saveResultButton.addActionListener(e -> saveResult());

        copyResultButton = new JButton("Sao chép");
        copyResultButton.setPreferredSize(new Dimension(100, 35));
        copyResultButton.addActionListener(e -> copyResult());

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveResultButton);
        buttonPanel.add(copyResultButton);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Sẵn sàng");
        statusPanel.add(statusLabel);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateAlgorithmConfig() {
        String algorithm = getSelectedAlgorithm();

        keySizeCombo.removeAllItems();

        if ("AES".equals(algorithm)) {
            keySizeCombo.addItem("128");
            keySizeCombo.addItem("192");
            keySizeCombo.addItem("256");
            keySizeCombo.setSelectedItem("128");
        } else {
            // DESede/3DES dùng 24 byte key.
            // Thường gọi là 168-bit effective key, nhưng Java dùng 24 byte = 192 bit dữ liệu key.
            keySizeCombo.addItem("192");
            keySizeCombo.setSelectedItem("192");
        }

        updateIvState();
    }

    private void updateIvState() {
        boolean needIv = isCbcMode();

        ivTextArea.setEnabled(needIv);
        ivTextArea.setEditable(needIv);
        generateIvButton.setEnabled(needIv);

        if (!needIv) {
            ivTextArea.setText("");
            statusLabel.setText("Mode ECB không dùng IV");
        }
    }

    private void updateInputMode() {
        boolean textMode = textRadioButton.isSelected();

        inputTextArea.setEnabled(textMode);
        inputTextArea.setEditable(textMode);

        chooseFileButton.setEnabled(!textMode);
        filePathField.setEnabled(!textMode);

        if (textMode) {
            statusLabel.setText("Chế độ mã hóa văn bản");
        } else {
            statusLabel.setText("Chế độ mã hóa file");
        }
    }

    private void generateKey() {
        try {
            int keySize = Integer.parseInt((String) keySizeCombo.getSelectedItem());
            byte[] key = keyIvService.generateKey(keySize);

            keyTextArea.setText(keyIvService.keyToBase64(key));
            statusLabel.setText("Đã tạo key cho " + getSelectedAlgorithm());
        } catch (Exception e) {
            showError("Lỗi tạo key: " + e.getMessage());
        }
    }

    private void generateIv() {
        try {
            int ivSize = getIvSizeInBits();
            byte[] iv = keyIvService.generateIV(ivSize);

            ivTextArea.setText(keyIvService.keyToBase64(iv));
            statusLabel.setText("Đã tạo IV");
        } catch (Exception e) {
            showError("Lỗi tạo IV: " + e.getMessage());
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

    private void encrypt() {
        try {
            if (textRadioButton.isSelected()) {
                encryptText();
            } else {
                encryptFile();
            }
        } catch (Exception e) {
            showError("Lỗi mã hóa: " + e.getMessage());
        }
    }

    private void decrypt() {
        try {
            if (textRadioButton.isSelected()) {
                decryptText();
            } else {
                decryptFile();
            }
        } catch (Exception e) {
            showError("Lỗi giải mã: " + e.getMessage());
        }
    }

    private void encryptText() throws Exception {
        String input = inputTextArea.getText();

        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập văn bản cần mã hóa.");
        }

        byte[] key = getKeyFromInput();
        byte[] iv = getIvFromInputIfNeeded();

        String result = callEncryptService(input, key, iv);

        outputTextArea.setText(result);
        statusLabel.setText("Mã hóa văn bản thành công bằng " + getSelectedAlgorithm());
    }

    private void decryptText() throws Exception {
        String input = inputTextArea.getText();

        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập chuỗi Base64 cần giải mã.");
        }

        byte[] key = getKeyFromInput();
        byte[] iv = getIvFromInputIfNeeded();

        String result = callDecryptService(input, key, iv);

        outputTextArea.setText(result);
        statusLabel.setText("Giải mã văn bản thành công bằng " + getSelectedAlgorithm());
    }

    private void encryptFile() throws Exception {
        if (selectedFile == null) {
            throw new IllegalArgumentException("Vui lòng chọn file cần mã hóa.");
        }

        byte[] fileBytes = FileUtil.readFileAsBytes(selectedFile.getAbsolutePath());
        String fileBase64 = Base64.getEncoder().encodeToString(fileBytes);

        byte[] key = getKeyFromInput();
        byte[] iv = getIvFromInputIfNeeded();

        String encryptedBase64 = callEncryptService(fileBase64, key, iv);

        String outputPath = selectedFile.getAbsolutePath() + ".enc";
        FileUtil.writeFile(outputPath, encryptedBase64);

        outputTextArea.setText("Mã hóa file thành công.\nFile đã lưu tại:\n" + outputPath);
        statusLabel.setText("Mã hóa file thành công: " + new File(outputPath).getName());
    }

    private void decryptFile() throws Exception {
        if (selectedFile == null) {
            throw new IllegalArgumentException("Vui lòng chọn file .enc cần giải mã.");
        }

        String encryptedBase64 = FileUtil.readFile(selectedFile.getAbsolutePath()).trim();

        byte[] key = getKeyFromInput();
        byte[] iv = getIvFromInputIfNeeded();

        String decryptedFileBase64 = callDecryptService(encryptedBase64, key, iv);
        byte[] originalFileBytes = Base64.getDecoder().decode(decryptedFileBase64);

        String outputPath = buildDecryptOutputPath(selectedFile);
        FileUtil.writeFileAsBytes(outputPath, originalFileBytes);

        outputTextArea.setText("Giải mã file thành công.\nFile đã lưu tại:\n" + outputPath);
        statusLabel.setText("Giải mã file thành công: " + new File(outputPath).getName());
    }

    private String callEncryptService(String input, byte[] key, byte[] iv) throws Exception {
        String algorithm = getSelectedAlgorithm();

        if ("AES".equals(algorithm)) {
            if (isCbcMode()) {
                return cipherService.encryptAESCBC(input, key, iv);
            }
            return cipherService.encryptAESECB(input, key);
        }

        if (isCbcMode()) {
            return cipherService.encrypt3DESCBC(input, key, iv);
        }

        return cipherService.encrypt3DESECB(input, key);
    }

    private String callDecryptService(String input, byte[] key, byte[] iv) throws Exception {
        String algorithm = getSelectedAlgorithm();

        if ("AES".equals(algorithm)) {
            if (isCbcMode()) {
                return cipherService.decryptAESCBC(input, key, iv);
            }
            return cipherService.decryptAESECB(input, key);
        }

        if (isCbcMode()) {
            return cipherService.decrypt3DESCBC(input, key, iv);
        }

        return cipherService.decrypt3DESECB(input, key);
    }

    private byte[] getKeyFromInput() {
        String keyBase64 = keyTextArea.getText();

        if (keyBase64 == null || keyBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập key hoặc bấm Tạo key.");
        }

        byte[] key = keyIvService.base64ToKey(keyBase64.trim());

        validateKeyLength(key);

        return key;
    }

    private byte[] getIvFromInputIfNeeded() {
        if (!isCbcMode()) {
            return null;
        }

        String ivBase64 = ivTextArea.getText();

        if (ivBase64 == null || ivBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("Mode CBC cần IV. Vui lòng nhập IV hoặc bấm Tạo IV.");
        }

        byte[] iv = keyIvService.base64ToKey(ivBase64.trim());

        validateIvLength(iv);

        return iv;
    }

    private void validateKeyLength(byte[] key) {
        String algorithm = getSelectedAlgorithm();

        if ("AES".equals(algorithm)) {
            if (key.length != 16 && key.length != 24 && key.length != 32) {
                throw new IllegalArgumentException("AES key phải dài 16, 24 hoặc 32 byte.");
            }
            return;
        }

        if (key.length != 24) {
            throw new IllegalArgumentException("DESede/3DES key phải dài 24 byte.");
        }
    }

    private void validateIvLength(byte[] iv) {
        int expectedLength = getIvSizeInBits() / 8;

        if (iv.length != expectedLength) {
            throw new IllegalArgumentException("IV phải dài " + expectedLength + " byte.");
        }
    }

    private int getIvSizeInBits() {
        if ("AES".equals(getSelectedAlgorithm())) {
            return 128;
        }

        return 64;
    }

    private String buildDecryptOutputPath(File encryptedFile) {
        File parent = encryptedFile.getParentFile();
        String fileName = encryptedFile.getName();

        if (fileName.endsWith(".enc")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }

        String outputName = "decrypted_" + fileName;

        if (parent == null) {
            return outputName;
        }

        return new File(parent, outputName).getAbsolutePath();
    }

    private String getSelectedAlgorithm() {
        String selected = (String) algorithmCombo.getSelectedItem();

        if (selected == null) {
            return "AES";
        }

        if (selected.startsWith("AES")) {
            return "AES";
        }

        return "DESede";
    }

    private boolean isCbcMode() {
        String mode = (String) modeCombo.getSelectedItem();
        return "CBC".equals(mode);
    }

    private void saveResult() {
        String content = outputTextArea.getText();

        if (content == null || content.isEmpty()) {
            showError("Không có kết quả để lưu.");
            return;
        }

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                FileUtil.writeFile(file.getAbsolutePath(), content);
                statusLabel.setText("Đã lưu kết quả: " + file.getName());
            } catch (Exception e) {
                showError("Lỗi lưu kết quả: " + e.getMessage());
            }
        }
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

    private void clear() {
        selectedFile = null;

        filePathField.setText("");
        inputTextArea.setText("");
        outputTextArea.setText("");
        keyTextArea.setText("");
        ivTextArea.setText("");

        statusLabel.setText("Đã xóa dữ liệu");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Lỗi: " + message);
    }
}