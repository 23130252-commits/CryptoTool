package com.cryptotool;

import com.cryptotool.ui.MainFrame;

import javax.swing.*;

public class CryptoToolApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
