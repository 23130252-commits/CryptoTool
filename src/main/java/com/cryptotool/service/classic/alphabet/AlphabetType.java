package com.cryptotool.service.classic.alphabet;

public enum AlphabetType {
    ENGLISH(
            "English",
            buildEnglishAlphabet()
    ),

    VIETNAMESE(
            "Vietnamese",
            buildVietnameseAlphabet()
    ),

    MIXED(
            "Mixed",
            buildVietnameseAlphabet() + buildExtraEnglishAlphabet()
    );

    private final String displayName;
    private final String alphabet;

    AlphabetType(String displayName, String alphabet) {
        this.displayName = displayName;
        this.alphabet = alphabet;
        validateNoDuplicateCharacters(alphabet, displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public int getSize() {
        return alphabet.length();
    }

    private static String buildEnglishAlphabet() {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz";
    }

    private static String buildVietnameseAlphabet() {
        String lower =
                "aáàảãạăắằẳẵặâấầẩẫậ" +
                        "bcdđ" +
                        "eéèẻẽẹêếềểễệ" +
                        "ghiklmnoóòỏõọôốồổỗộơớờởỡợ" +
                        "pqrstuúùủũụưứừửữự" +
                        "vxyýỳỷỹỵ";

        String upper =
                "AÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬ" +
                        "BCDĐ" +
                        "EÉÈẺẼẸÊẾỀỂỄỆ" +
                        "GHIKLMNOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢ" +
                        "PQRSTUÚÙỦŨỤƯỨỪỬỮỰ" +
                        "VXYÝỲỶỸỴ";

        return lower + upper;
    }

    private static String buildExtraEnglishAlphabet() {
        // Tiếng Việt chuẩn không có F, J, W, Z.
        // Thêm các ký tự này cho bảng Mixed để hỗ trợ từ như Java, File, AES, RSA...
        return "fFjJwWzZ";
    }

    private static void validateNoDuplicateCharacters(String alphabet, String displayName) {
        for (int i = 0; i < alphabet.length(); i++) {
            char current = alphabet.charAt(i);

            for (int j = i + 1; j < alphabet.length(); j++) {
                if (current == alphabet.charAt(j)) {
                    throw new IllegalArgumentException(
                            "Bảng chữ cái " + displayName + " bị trùng ký tự: " + current
                    );
                }
            }
        }
    }
}