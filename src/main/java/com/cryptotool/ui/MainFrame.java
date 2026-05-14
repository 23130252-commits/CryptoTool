package com.cryptotool.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel truyenThongPanel;
    private JPanel hienDaiPanel;
    private JPanel rsaPanel;
    private JPanel bamPanel;

    public MainFrame() {
        setTitle("Công cụ Mã hóa - CryptoTool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // Tab 1: Mã hóa Truyền thống
        truyenThongPanel = new TraditionalEncryptionPanel();
        tabbedPane.addTab("Truyền thống", truyenThongPanel);

        // Tab 2: Mã hóa Hiện đại
        hienDaiPanel = new ModernEncryptionPanel();
        tabbedPane.addTab("Hiện đại", hienDaiPanel);

        // Tab 3: RSA
        rsaPanel = new RSAPanel();
        tabbedPane.addTab("RSA", rsaPanel);

        // Tab 4: Bắm (Hash)
        bamPanel = new HashPanel();
        tabbedPane.addTab("Bắm", bamPanel);

        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
