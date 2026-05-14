package com.cryptotool.ui;

import com.cryptotool.util.FileUtil;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class ModernEncryptionPanel extends JPanel {
    private JComboBox<String> algorithmCombo;
    private JComboBox<String> modeCombo;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton clearButton;
    private JButton loadFileButton;
    private JButton saveFileButton;
    private JFileChooser fileChooser;
    private JLabel statusLabel;

    public ModernEncryptionPanel() {
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Tên Thuật toán"));

        algorithmCombo = new JComboBox<>(new String[]{
            "AES - Tiêu chuẩn Mã hóa Nâng cao",
            "DES - Tiêu chuẩn Mã hóa Dữ liệu",
            "3DES - Mã hóa Dữ liệu ba lần"
        });
        algorithmCombo.setPreferredSize(new Dimension(300, 30));
        algorithmCombo.addActionListener(e -> updateModes());

        modeCombo = new JComboBox<>(new String[]{
            "ECB - Sách Mã điện tử",
            "CBC - Chuỗi Khối Cipher",
            "CFB - Phản hồi Shift Cipher"
        });
        modeCombo.setPreferredSize(new Dimension(250, 30));

        panel.add(new JLabel("Thuật toán:"));
        panel.add(algorithmCombo);
        panel.add(new JLabel("Chế độ:"));
        panel.add(modeCombo);

        return panel;
    }

    private void updateModes() {
        String selected = (String) algorithmCombo.getSelectedItem();
        // ECB mode không được khuyến khích cho AES nhưng vẫn hỗ trợ
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
        showError("Chức năng này chưa được triển khai");
    }

    private void decrypt() {
        showError("Chức năng này chưa được triển khai");
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
