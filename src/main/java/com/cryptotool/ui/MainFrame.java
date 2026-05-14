package com.cryptotool.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String APP_TITLE = "CryptoTool Java - Công cụ mã hóa";

    private JTabbedPane tabbedPane;

    public MainFrame() {
        initFrame();
        initComponents();
    }

    private void initFrame() {
        setTitle(APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        tabbedPane = createTabbedPane();

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        panel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel(APP_TITLE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel subTitleLabel = new JLabel("Truyền thống | Hiện đại | RSA | Băm");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitleLabel.setForeground(new Color(90, 90, 90));
        subTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(subTitleLabel, BorderLayout.EAST);

        return panel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabs.addTab("Truyền thống", createTraditionalTab());
        tabs.addTab("Hiện đại", createModernTab());
        tabs.addTab("RSA", createRSATab());
        tabs.addTab("Băm", createHashTab());

        tabs.setToolTipTextAt(0, "Caesar, Substitution, Affine, Vigenere, Hill, Transposition");
        tabs.setToolTipTextAt(1, "AES và DESede/3DES");
        tabs.setToolTipTextAt(2, "RSA 256-bit demo và RSA 1024/2048-bit");
        tabs.setToolTipTextAt(3, "MD5, SHA-1, SHA-256");

        return tabs;
    }

    private JPanel createTraditionalTab() {
        return new TraditionalEncryptionPanel();
    }

    private JPanel createModernTab() {
        return new ModernEncryptionPanel();
    }

    private JPanel createRSATab() {
        return new RSAPanel();
    }

    private JPanel createHashTab() {
        return new HashPanel();
    }
}