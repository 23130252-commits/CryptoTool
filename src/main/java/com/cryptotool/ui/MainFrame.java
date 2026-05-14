package com.cryptotool.ui;

import com.cryptotool.ui.panel.CipherPanel;

import javax.swing.*;

public class MainFrame extends JFrame {
    private CipherPanel cipherPanel;

    public MainFrame() {
        setTitle("CryptoTool - Cryptography Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Tạo menu bar
        createMenuBar();

        // Tạo cipher panel
        cipherPanel = new CipherPanel();
        add(cipherPanel);
    }

    /**
     * Tạo menu bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu File
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Menu Help
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Hiển thị dialog About
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
                this,
                "CryptoTool v1.0\n\nA comprehensive cryptography tool for classic and modern ciphers.",
                "About CryptoTool",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
