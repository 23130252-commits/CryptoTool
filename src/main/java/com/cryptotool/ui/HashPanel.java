package com.cryptotool.ui;

import com.cryptotool.service.hash.HashService;
import com.cryptotool.util.FileUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;

public class HashPanel extends JPanel {
    private JComboBox<String> algorithmCombo;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton computeButton;
    private JButton clearButton;
    private JButton loadFileButton;
    private JButton copyButton;
    private JFileChooser fileChooser;
    private JLabel statusLabel;

    public HashPanel() {
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
            "MD5",
            "SHA-1",
            "SHA-256",
            "SHA-384",
            "SHA-512"
        });
        algorithmCombo.setPreferredSize(new Dimension(200, 30));

        panel.add(new JLabel("Chọn thuật toán:"));
        panel.add(algorithmCombo);

        return panel;
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
        outputPanel.setBorder(BorderFactory.createTitledBorder("Mã Bắm"));
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

        computeButton = new JButton("Tính Bắm");
        computeButton.setPreferredSize(new Dimension(100, 35));
        computeButton.addActionListener(e -> compute());

        clearButton = new JButton("Xóa");
        clearButton.setPreferredSize(new Dimension(100, 35));
        clearButton.addActionListener(e -> clear());

        loadFileButton = new JButton("Tải tệp");
        loadFileButton.setPreferredSize(new Dimension(100, 35));
        loadFileButton.addActionListener(e -> loadFile());

        copyButton = new JButton("Sao chép");
        copyButton.setPreferredSize(new Dimension(100, 35));
        copyButton.addActionListener(e -> copyToClipboard());

        buttonPanel.add(computeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(loadFileButton);
        buttonPanel.add(copyButton);

        // Status bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Sẵn sàng");
        statusPanel.add(statusLabel);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void compute() {
        String input = inputTextArea.getText();
        if (input.isEmpty()) {
            showError("Vui lòng nhập văn bản");
            return;
        }

        try {
            String algorithm = (String) algorithmCombo.getSelectedItem();
            String hash = "";

            switch (algorithm) {
                case "MD5":
                    hash = HashService.hashMD5(input);
                    break;
                case "SHA-1":
                    hash = HashService.hashSHA1(input);
                    break;
                case "SHA-256":
                    hash = HashService.hashSHA256(input);
                    break;
                case "SHA-384":
                    hash = HashService.hashSHA384(input);
                    break;
                case "SHA-512":
                    hash = HashService.hashSHA512(input);
                    break;
            }

            outputTextArea.setText(hash);
            statusLabel.setText("✓ Tính Bắm thành công - " + algorithm);
        } catch (Exception e) {
            showError("Lỗi tính toán: " + e.getMessage());
        }
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

    private void copyToClipboard() {
        String text = outputTextArea.getText();
        if (text.isEmpty()) {
            showError("Không có dữ liệu để sao chép");
            return;
        }
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        statusLabel.setText("✓ Đã sao chép vào bộ nhớ tạm");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("✗ Lỗi: " + message);
    }
}
