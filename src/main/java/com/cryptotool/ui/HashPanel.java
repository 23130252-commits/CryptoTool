package com.cryptotool.ui;

import com.cryptotool.service.hash.HashService;
import com.cryptotool.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;

public class HashPanel extends JPanel {
    private JComboBox<String> algorithmCombo;

    private JRadioButton textRadioButton;
    private JRadioButton fileRadioButton;

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;

    private JTextField filePathField;

    private JButton hashButton;
    private JButton chooseFileButton;
    private JButton clearButton;
    private JButton copyButton;

    private JFileChooser fileChooser;
    private JLabel statusLabel;

    private File selectedFile;

    public HashPanel() {
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

        updateInputMode();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cấu hình băm"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        algorithmCombo = new JComboBox<>(new String[]{
                "MD5",
                "SHA-1",
                "SHA-256"
        });
        algorithmCombo.setPreferredSize(new Dimension(180, 30));

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
        panel.add(new JLabel("Kiểu dữ liệu:"), gbc);

        gbc.gridx = 3;
        panel.add(textRadioButton, gbc);

        gbc.gridx = 4;
        panel.add(fileRadioButton, gbc);

        return panel;
    }

    private JPanel createCenterPanel() {
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
        outputPanel.setBorder(BorderFactory.createTitledBorder("Kết quả băm"));

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

        hashButton = new JButton("Băm dữ liệu");
        hashButton.setPreferredSize(new Dimension(120, 35));
        hashButton.addActionListener(e -> computeHash());

        clearButton = new JButton("Xóa");
        clearButton.setPreferredSize(new Dimension(100, 35));
        clearButton.addActionListener(e -> clear());

        copyButton = new JButton("Sao chép");
        copyButton.setPreferredSize(new Dimension(100, 35));
        copyButton.addActionListener(e -> copyToClipboard());

        buttonPanel.add(hashButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(copyButton);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Sẵn sàng");
        statusPanel.add(statusLabel);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateInputMode() {
        boolean textMode = textRadioButton.isSelected();

        inputTextArea.setEnabled(textMode);
        inputTextArea.setEditable(textMode);

        chooseFileButton.setEnabled(!textMode);
        filePathField.setEnabled(!textMode);

        if (textMode) {
            statusLabel.setText("Chế độ băm văn bản");
        } else {
            statusLabel.setText("Chế độ băm file");
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

    private void computeHash() {
        String algorithm = (String) algorithmCombo.getSelectedItem();

        if (algorithm == null) {
            showError("Vui lòng chọn thuật toán băm.");
            return;
        }

        try {
            String hash;

            if (textRadioButton.isSelected()) {
                String input = inputTextArea.getText();

                if (input == null || input.isEmpty()) {
                    showError("Vui lòng nhập văn bản cần băm.");
                    return;
                }

                hash = hashText(input, algorithm);
                statusLabel.setText("Băm văn bản thành công bằng " + algorithm);
            } else {
                if (selectedFile == null) {
                    showError("Vui lòng chọn file cần băm.");
                    return;
                }

                byte[] fileBytes = FileUtil.readFileAsBytes(selectedFile.getAbsolutePath());
                hash = hashBytes(fileBytes, algorithm);
                statusLabel.setText("Băm file thành công: " + selectedFile.getName());
            }

            outputTextArea.setText(hash);
        } catch (Exception e) {
            showError("Lỗi khi băm dữ liệu: " + e.getMessage());
        }
    }

    private String hashText(String input, String algorithm) throws Exception {
        switch (algorithm) {
            case "MD5":
                return HashService.hashMD5(input);
            case "SHA-1":
                return HashService.hashSHA1(input);
            case "SHA-256":
                return HashService.hashSHA256(input);
            default:
                throw new IllegalArgumentException("Thuật toán không được hỗ trợ: " + algorithm);
        }
    }

    private String hashBytes(byte[] input, String algorithm) throws Exception {
        switch (algorithm) {
            case "MD5":
                return HashService.hashMD5(input);
            case "SHA-1":
                return HashService.hashSHA1(input);
            case "SHA-256":
                return HashService.hashSHA256(input);
            default:
                throw new IllegalArgumentException("Thuật toán không được hỗ trợ: " + algorithm);
        }
    }

    private void clear() {
        selectedFile = null;
        filePathField.setText("");
        inputTextArea.setText("");
        outputTextArea.setText("");
        statusLabel.setText("Đã xóa dữ liệu");
    }

    private void copyToClipboard() {
        String text = outputTextArea.getText();

        if (text == null || text.isEmpty()) {
            showError("Không có kết quả để sao chép.");
            return;
        }

        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        statusLabel.setText("Đã sao chép kết quả vào clipboard");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Lỗi: " + message);
    }
}