package com.cryptotool.ui;

import java.security.SecureRandom;
import com.cryptotool.service.classic.AffineCipher;
import com.cryptotool.service.classic.CaesarCipher;
import com.cryptotool.service.classic.HillCipher;
import com.cryptotool.service.classic.SubstitutionCipher;
import com.cryptotool.service.classic.TranspositionCipher;
import com.cryptotool.service.classic.VigenereCipher;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;
import com.cryptotool.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;

public class TraditionalEncryptionPanel extends JPanel {
    private JComboBox<String> algorithmCombo;

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTextArea keyTextArea;

    private JTextField filePathField;

    private JButton encryptButton;
    private JButton decryptButton;
    private JButton clearButton;
    private JButton loadFileButton;
    private JButton saveFileButton;
    private JButton copyButton;
    private JButton generateKeyButton;

    private JFileChooser fileChooser;
    private JLabel statusLabel;
    private JLabel keyHintLabel;

    private File selectedFile;
    private final SecureRandom random = new SecureRandom();

    public TraditionalEncryptionPanel() {
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

        updateKeyArea();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Cấu hình mã hóa truyền thống"));

        JPanel optionPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        algorithmCombo = new JComboBox<>(new String[]{
                "Caesar",
                "Substitution",
                "Affine",
                "Vigenere",
                "Hill",
                "Transposition"
        });
        algorithmCombo.setPreferredSize(new Dimension(220, 30));
        algorithmCombo.addActionListener(e -> updateKeyArea());

        gbc.gridx = 0;
        gbc.gridy = 0;
        optionPanel.add(new JLabel("Thuật toán:"), gbc);

        gbc.gridx = 1;
        optionPanel.add(algorithmCombo, gbc);

        gbc.gridx = 2;
        optionPanel.add(new JLabel("Bảng chữ cái:"), gbc);

        gbc.gridx = 3;
        JLabel alphabetInfoLabel = new JLabel("Mặc định Mixed; Hill dùng English A-Z");
        alphabetInfoLabel.setForeground(new Color(80, 80, 80));
        optionPanel.add(alphabetInfoLabel, gbc);

        JPanel keyPanel = new JPanel(new BorderLayout(5, 5));
        keyPanel.setBorder(BorderFactory.createTitledBorder("Khóa"));

        keyTextArea = new JTextArea(3, 40);
        keyTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        keyTextArea.setLineWrap(true);
        keyTextArea.setWrapStyleWord(true);

        keyHintLabel = new JLabel(" ");
        keyHintLabel.setForeground(new Color(80, 80, 80));

        generateKeyButton = new JButton("Tạo key");
        generateKeyButton.addActionListener(e -> generateKey());

        JPanel keyTopPanel = new JPanel(new BorderLayout(5, 5));
        keyTopPanel.add(keyHintLabel, BorderLayout.CENTER);
        keyTopPanel.add(generateKeyButton, BorderLayout.EAST);

        keyPanel.add(keyTopPanel, BorderLayout.NORTH);
        keyPanel.add(new JScrollPane(keyTextArea), BorderLayout.CENTER);

        panel.add(optionPanel, BorderLayout.NORTH);
        panel.add(keyPanel, BorderLayout.CENTER);

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

        loadFileButton = new JButton("Chọn file text");
        loadFileButton.addActionListener(e -> loadFile());

        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(loadFileButton, BorderLayout.EAST);

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

        saveFileButton = new JButton("Lưu kết quả");
        saveFileButton.setPreferredSize(new Dimension(120, 35));
        saveFileButton.addActionListener(e -> saveFile());

        copyButton = new JButton("Sao chép");
        copyButton.setPreferredSize(new Dimension(100, 35));
        copyButton.addActionListener(e -> copyResult());

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveFileButton);
        buttonPanel.add(copyButton);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        statusLabel = new JLabel("Sẵn sàng");
        statusPanel.add(statusLabel);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateKeyArea() {
        if (keyTextArea == null || keyHintLabel == null || generateKeyButton == null) {
            return;
        }

        String algorithm = getSelectedAlgorithm();

        generateKeyButton.setEnabled(true);
        keyTextArea.setEditable(true);

        switch (algorithm) {
            case "Caesar":
                keyHintLabel.setText("Key Caesar: số dịch k. Dùng Mixed alphabet. Ví dụ: 3");
                keyTextArea.setText("3");
                break;

            case "Substitution":
                keyHintLabel.setText("Key Substitution: bảng thay thế Mixed. Bấm Tạo key để sinh tự động.");
                keyTextArea.setText("");
                break;

            case "Affine":
                keyHintLabel.setText("Key Affine: dạng a,b. Tạo key sẽ sinh a hợp lệ với Mixed alphabet.");
                keyTextArea.setText(generateAffineKey());
                break;

            case "Vigenere":
                keyHintLabel.setText("Key Vigenere: từ khóa nằm trong Mixed alphabet. Ví dụ: BaoMat");
                keyTextArea.setText("BaoMat");
                break;

            case "Hill":
                keyHintLabel.setText("Key Hill 2x2: dạng a,b,c,d. Chỉ hỗ trợ English A-Z. Ví dụ: 3,3,2,5");
                keyTextArea.setText("3,3,2,5");
                break;

            case "Transposition":
                keyHintLabel.setText("Key Transposition: số hàng Rail Fence, ví dụ 3; hoặc key cột dạng 3,1,4,2.");
                keyTextArea.setText("3");
                break;

            default:
                keyHintLabel.setText("Nhập key.");
                keyTextArea.setText("");
                break;
        }

        setStatus("Đã chọn thuật toán: " + algorithm);
    }

    private void generateKey() {
        String algorithm = getSelectedAlgorithm();

        try {
            switch (algorithm) {
                case "Caesar":
                    keyTextArea.setText(generateCaesarKey());
                    setStatus("Đã tạo key Caesar ngẫu nhiên.");
                    break;

                case "Substitution":
                    SubstitutionCipher substitutionCipher = new SubstitutionCipher(AlphabetType.MIXED);
                    keyTextArea.setText(substitutionCipher.generateRandomKey());
                    setStatus("Đã tạo key Substitution ngẫu nhiên.");
                    break;

                case "Affine":
                    keyTextArea.setText(generateAffineKey());
                    setStatus("Đã tạo key Affine ngẫu nhiên hợp lệ.");
                    break;

                case "Vigenere":
                    keyTextArea.setText(generateVigenereKey());
                    setStatus("Đã tạo key Vigenere ngẫu nhiên.");
                    break;

                case "Hill":
                    keyTextArea.setText(generateHillKey());
                    setStatus("Đã tạo key Hill hợp lệ.");
                    break;

                case "Transposition":
                    keyTextArea.setText(generateTranspositionKey());
                    setStatus("Đã tạo key Transposition ngẫu nhiên.");
                    break;

                default:
                    showError("Không hỗ trợ tạo key cho thuật toán này.");
                    break;
            }
        } catch (Exception e) {
            showError("Lỗi tạo key: " + e.getMessage());
        }
    }

    private String generateAffineKey() {
        int n = AlphabetRepository.getAlphabet(AlphabetType.MIXED).size();

        int a;

        do {
            a = random.nextInt(n - 2) + 2;
        } while (gcd(a, n) != 1);

        int b = random.nextInt(n);

        return a + "," + b;
    }
    private String generateCaesarKey() {
        int n = AlphabetRepository.getAlphabet(AlphabetType.MIXED).size();

        int shift;

        do {
            shift = random.nextInt(n - 1) + 1;
        } while (shift == 3);

        return String.valueOf(shift);
    }

    private String generateVigenereKey() {
        String[] sampleKeys = {
                "BaoMat",
                "Crypto",
                "Java",
                "AnToan",
                "MaHoa",
                "KhoaHoc",
                "Security"
        };

        return sampleKeys[random.nextInt(sampleKeys.length)];
    }

    private String generateHillKey() {
        String[] validKeys = {
                "3,3,2,5",
                "5,8,17,3",
                "7,8,11,11",
                "9,4,5,7",
                "11,8,3,7"
        };

        return validKeys[random.nextInt(validKeys.length)];
    }

    private String generateTranspositionKey() {
        int rails = random.nextInt(4) + 2; // tạo số từ 2 đến 5
        return String.valueOf(rails);
    }

    private void encrypt() {
        process(true);
    }

    private void decrypt() {
        process(false);
    }

    private void process(boolean encryptMode) {
        String input = inputTextArea.getText();

        if (input == null || input.isEmpty()) {
            showError("Vui lòng nhập văn bản hoặc chọn file text.");
            return;
        }

        String key = keyTextArea.getText();

        try {
            String algorithm = getSelectedAlgorithm();
            String result;

            switch (algorithm) {
                case "Caesar":
                    result = processCaesar(input, key, encryptMode);
                    break;

                case "Substitution":
                    result = processSubstitution(input, key, encryptMode);
                    break;

                case "Affine":
                    result = processAffine(input, key, encryptMode);
                    break;

                case "Vigenere":
                    result = processVigenere(input, key, encryptMode);
                    break;

                case "Hill":
                    result = processHill(input, key, encryptMode);
                    break;

                case "Transposition":
                    result = processTransposition(input, key, encryptMode);
                    break;

                default:
                    throw new IllegalArgumentException("Thuật toán không được hỗ trợ: " + algorithm);
            }

            outputTextArea.setText(result);

            if (encryptMode) {
                setStatus("Mã hóa " + algorithm + " thành công.");
            } else {
                setStatus("Giải mã " + algorithm + " thành công.");
            }
        } catch (Exception e) {
            if (encryptMode) {
                showError("Lỗi mã hóa: " + e.getMessage());
            } else {
                showError("Lỗi giải mã: " + e.getMessage());
            }
        }
    }

    private String processCaesar(String input, String key, boolean encryptMode) {
        int shift = parseIntegerKey(key, "Key Caesar phải là số nguyên.");

        CaesarCipher cipher = new CaesarCipher(AlphabetType.MIXED);

        if (encryptMode) {
            return cipher.encrypt(input, shift);
        }

        return cipher.decrypt(input, shift);
    }

    private String processSubstitution(String input, String key, boolean encryptMode) throws Exception {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập key Substitution hoặc bấm Tạo key.");
        }

        SubstitutionCipher cipher = new SubstitutionCipher(AlphabetType.MIXED);

        if (encryptMode) {
            return cipher.encrypt(input, key.trim());
        }

        return cipher.decrypt(input, key.trim());
    }

    private String processAffine(String input, String key, boolean encryptMode) throws Exception {
        AffineCipher cipher = new AffineCipher(AlphabetType.MIXED);

        if (encryptMode) {
            return cipher.encrypt(input, key);
        }

        return cipher.decrypt(input, key);
    }

    private String processVigenere(String input, String key, boolean encryptMode) throws Exception {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập key Vigenere.");
        }

        VigenereCipher cipher = new VigenereCipher(AlphabetType.MIXED);

        if (encryptMode) {
            return cipher.encrypt(input, key.trim());
        }

        return cipher.decrypt(input, key.trim());
    }

    private String processHill(String input, String key, boolean encryptMode) throws Exception {
        int[][] matrix = parseHillMatrix(key);

        HillCipher cipher = new HillCipher(AlphabetType.ENGLISH);
        String normalizedInput = input.toUpperCase();

        if (encryptMode) {
            return cipher.encrypt(normalizedInput, matrix);
        }

        return cipher.decrypt(normalizedInput, matrix);
    }

    private String processTransposition(String input, String key, boolean encryptMode) throws Exception {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập key Transposition.");
        }

        TranspositionCipher cipher = new TranspositionCipher(AlphabetType.MIXED);

        String trimmedKey = key.trim();

        if (trimmedKey.contains(",")) {
            int[] keySequence = parseKeySequence(trimmedKey);

            if (encryptMode) {
                return cipher.encryptRowColumn(input, keySequence);
            }

            return cipher.decryptRowColumn(input, keySequence);
        }

        int rails = parseIntegerKey(
                trimmedKey,
                "Key Transposition phải là số hàng >= 2 hoặc dãy cột dạng 3,1,4,2."
        );

        if (encryptMode) {
            return cipher.encryptRailFence(input, rails);
        }

        return cipher.decryptRailFence(input, rails);
    }

    private int[][] parseHillMatrix(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key Hill không được để trống.");
        }

        String[] parts = key.trim().split(",");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Key Hill phải có 4 số dạng a,b,c,d. Ví dụ: 3,3,2,5.");
        }

        try {
            return new int[][]{
                    {Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())},
                    {Integer.parseInt(parts[2].trim()), Integer.parseInt(parts[3].trim())}
            };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Key Hill chỉ được chứa số nguyên.");
        }
    }

    private int[] parseKeySequence(String key) {
        String[] parts = key.trim().split(",");
        int[] sequence = new int[parts.length];
        boolean[] seen = new boolean[parts.length + 1];

        try {
            for (int i = 0; i < parts.length; i++) {
                int value = Integer.parseInt(parts[i].trim());

                if (value < 1 || value > parts.length) {
                    throw new IllegalArgumentException("Key cột phải là hoán vị từ 1 đến " + parts.length + ".");
                }

                if (seen[value]) {
                    throw new IllegalArgumentException("Key cột bị trùng giá trị: " + value);
                }

                seen[value] = true;
                sequence[i] = value;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Key cột chỉ được chứa số nguyên.");
        }

        return sequence;
    }

    private int parseIntegerKey(String key, String errorMessage) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }

        try {
            return Integer.parseInt(key.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private String getSelectedAlgorithm() {
        String selected = (String) algorithmCombo.getSelectedItem();

        if (selected == null) {
            return "Caesar";
        }

        return selected;
    }

    private void loadFile() {
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            try {
                String content = FileUtil.readFile(selectedFile.getAbsolutePath());
                inputTextArea.setText(content);
                filePathField.setText(selectedFile.getAbsolutePath());
                setStatus("Đã tải file text: " + selectedFile.getName());
            } catch (Exception e) {
                showError("Lỗi tải file: " + e.getMessage());
            }
        }
    }

    private void saveFile() {
        String content = outputTextArea.getText();

        if (content == null || content.isEmpty()) {
            showError("Không có kết quả để lưu.");
            return;
        }

        if (selectedFile != null) {
            fileChooser.setSelectedFile(new File(selectedFile.getName() + ".out.txt"));
        }

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                FileUtil.writeFile(file.getAbsolutePath(), content);
                setStatus("Đã lưu kết quả: " + file.getName());
            } catch (Exception e) {
                showError("Lỗi lưu file: " + e.getMessage());
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

        setStatus("Đã sao chép kết quả.");
    }

    private void clear() {
        selectedFile = null;

        filePathField.setText("");
        inputTextArea.setText("");
        outputTextArea.setText("");

        setStatus("Đã xóa dữ liệu.");
    }

    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        setStatus("Lỗi: " + message);
    }

    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}