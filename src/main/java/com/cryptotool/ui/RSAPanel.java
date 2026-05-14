package com.cryptotool.ui;

import com.cryptotool.util.FileUtil;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class RSAPanel extends JPanel {
    private JTextArea publicKeyArea;
    private JTextArea privateKeyArea;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton generateKeysButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton signButton;
    private JButton verifyButton;
    private JButton loadKeyButton;
    private JButton saveKeyButton;
    private JFileChooser fileChooser;
    private JLabel statusLabel;

    public RSAPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Panel Top: Tạo khóa
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Panel Center: 4 khu vực
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Panel Bottom: Nút điều khiển
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Quản lý Khóa RSA"));

        generateKeysButton = new JButton("Tạo cặp khóa");
        generateKeysButton.setPreferredSize(new Dimension(120, 35));
        generateKeysButton.addActionListener(e -> generateKeys());

        loadKeyButton = new JButton("Tải khóa");
        loadKeyButton.setPreferredSize(new Dimension(100, 35));
        loadKeyButton.addActionListener(e -> loadKey());

        saveKeyButton = new JButton("Lưu khóa");
        saveKeyButton.setPreferredSize(new Dimension(100, 35));
        saveKeyButton.addActionListener(e -> saveKey());

        panel.add(generateKeysButton);
        panel.add(loadKeyButton);
        panel.add(saveKeyButton);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        // Khóa công khai
        JPanel publicKeyPanel = new JPanel(new BorderLayout(5, 5));
        publicKeyPanel.setBorder(BorderFactory.createTitledBorder("Khóa Công khai"));
        publicKeyArea = new JTextArea();
        publicKeyArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        publicKeyArea.setLineWrap(true);
        publicKeyPanel.add(new JScrollPane(publicKeyArea), BorderLayout.CENTER);

        // Khóa bí mật
        JPanel privateKeyPanel = new JPanel(new BorderLayout(5, 5));
        privateKeyPanel.setBorder(BorderFactory.createTitledBorder("Khóa Bí mật"));
        privateKeyArea = new JTextArea();
        privateKeyArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        privateKeyArea.setLineWrap(true);
        privateKeyPanel.add(new JScrollPane(privateKeyArea), BorderLayout.CENTER);

        // Input
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Văn bản"));
        inputTextArea = new JTextArea();
        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inputTextArea.setLineWrap(true);
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Output
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBorder(BorderFactory.createTitledBorder("Văn bản trả lời"));
        outputTextArea = new JTextArea();
        outputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        panel.add(publicKeyPanel);
        panel.add(privateKeyPanel);
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

        signButton = new JButton("Ký");
        signButton.setPreferredSize(new Dimension(100, 35));
        signButton.addActionListener(e -> sign());

        verifyButton = new JButton("Xác minh");
        verifyButton.setPreferredSize(new Dimension(100, 35));
        verifyButton.addActionListener(e -> verify());

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(signButton);
        buttonPanel.add(verifyButton);

        // Status bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Sẵn sàng");
        statusPanel.add(statusLabel);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void generateKeys() {
        showInfo("Tạo cặp khóa RSA 2048-bit...\nChức năng này chưa được triển khai");
    }

    private void encrypt() {
        showError("Chức năng này chưa được triển khai");
    }

    private void decrypt() {
        showError("Chức năng này chưa được triển khai");
    }

    private void sign() {
        showError("Chức năng này chưa được triển khai");
    }

    private void verify() {
        showError("Chức năng này chưa được triển khai");
    }

    private void loadKey() {
        showError("Chức năng này chưa được triển khai");
    }

    private void saveKey() {
        showError("Chức năng này chưa được triển khai");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("✗ Lỗi");
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông tin", JOptionPane.INFORMATION_MESSAGE);
    }
}
