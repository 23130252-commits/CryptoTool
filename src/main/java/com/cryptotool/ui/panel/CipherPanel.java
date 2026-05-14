package com.cryptotool.ui.panel;

import com.cryptotool.service.classic.*;
import com.cryptotool.service.classic.alphabet.AlphabetType;

import javax.swing.*;
import java.awt.*;

public class CipherPanel extends JPanel {
    private JComboBox<String> cipherSelector;
    private JComboBox<AlphabetType> alphabetSelector;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTextField keyField;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton clearButton;

    public CipherPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tạo top panel (selectors)
        add(createTopPanel(), BorderLayout.NORTH);

        // Tạo center panel (input/output areas)
        add(createCenterPanel(), BorderLayout.CENTER);

        // Tạo bottom panel (buttons)
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * Tạo top panel
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Configuration"));

        // Cipher selector
        topPanel.add(new JLabel("Cipher Type:"));
        cipherSelector = new JComboBox<>(new String[]{
                "Caesar Cipher",
                "Substitution Cipher",
                "Affine Cipher",
                "Vigenère Cipher"
        });
        topPanel.add(cipherSelector);

        // Alphabet selector
        topPanel.add(new JLabel("Alphabet:"));
        alphabetSelector = new JComboBox<>(AlphabetType.values());
        topPanel.add(alphabetSelector);

        return topPanel;
    }

    /**
     * Tạo center panel
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Input area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Text"));
        inputTextArea = new JTextArea(20, 40);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane inputScroll = new JScrollPane(inputTextArea);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        // Output area
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Output Text"));
        outputTextArea = new JTextArea(20, 40);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputTextArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputTextArea);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        centerPanel.add(inputPanel);
        centerPanel.add(outputPanel);

        return centerPanel;
    }

    /**
     * Tạo bottom panel
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));

        // Key input
        JPanel keyPanel = new JPanel(new BorderLayout(5, 0));
        keyPanel.add(new JLabel("Key:"), BorderLayout.WEST);
        keyField = new JTextField(20);
        keyField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        keyPanel.add(keyField, BorderLayout.CENTER);

        bottomPanel.add(keyPanel, BorderLayout.WEST);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(e -> performEncrypt());
        buttonPanel.add(encryptButton);

        decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(e -> performDecrypt());
        buttonPanel.add(decryptButton);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearAllFields());
        buttonPanel.add(clearButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
     * Thực hiện mã hóa
     */
    private void performEncrypt() {
        try {
            String plaintext = inputTextArea.getText();
            String key = keyField.getText();
            String cipherType = (String) cipherSelector.getSelectedItem();
            AlphabetType alphabetType = (AlphabetType) alphabetSelector.getSelectedItem();

            if (plaintext.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter input text!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (key.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a key!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String result = "";

            if ("Caesar Cipher".equals(cipherType)) {
                int keyValue = Integer.parseInt(key);
                CaesarCipher cipher = new CaesarCipher(alphabetType);
                result = cipher.encrypt(plaintext, keyValue);
            } else if ("Substitution Cipher".equals(cipherType)) {
                SubstitutionCipher cipher = new SubstitutionCipher(alphabetType);
                result = cipher.encrypt(plaintext, key);
            } else if ("Affine Cipher".equals(cipherType)) {
                AffineCipher cipher = new AffineCipher(alphabetType);
                result = cipher.encrypt(plaintext, key);
            } else if ("Vigenère Cipher".equals(cipherType)) {
                VigenereCipher cipher = new VigenereCipher(alphabetType);
                result = cipher.encrypt(plaintext, key);
            }

            outputTextArea.setText(result);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid key format for Caesar Cipher! Key must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Thực hiện giải mã
     */
    private void performDecrypt() {
        try {
            String ciphertext = inputTextArea.getText();
            String key = keyField.getText();
            String cipherType = (String) cipherSelector.getSelectedItem();
            AlphabetType alphabetType = (AlphabetType) alphabetSelector.getSelectedItem();

            if (ciphertext.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter input text!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (key.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a key!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String result = "";

            if ("Caesar Cipher".equals(cipherType)) {
                int keyValue = Integer.parseInt(key);
                CaesarCipher cipher = new CaesarCipher(alphabetType);
                result = cipher.decrypt(ciphertext, keyValue);
            } else if ("Substitution Cipher".equals(cipherType)) {
                SubstitutionCipher cipher = new SubstitutionCipher(alphabetType);
                result = cipher.decrypt(ciphertext, key);
            } else if ("Affine Cipher".equals(cipherType)) {
                AffineCipher cipher = new AffineCipher(alphabetType);
                result = cipher.decrypt(ciphertext, key);
            } else if ("Vigenère Cipher".equals(cipherType)) {
                VigenereCipher cipher = new VigenereCipher(alphabetType);
                result = cipher.decrypt(ciphertext, key);
            }

            outputTextArea.setText(result);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid key format for Caesar Cipher! Key must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xóa tất cả các field
     */
    private void clearAllFields() {
        inputTextArea.setText("");
        outputTextArea.setText("");
        keyField.setText("");
    }
}
