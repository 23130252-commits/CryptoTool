package com.cryptotool.service.classic.alphabet;

public enum AlphabetType {
    ENGLISH("English", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
    VIETNAMESE("Vietnamese", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾế ẞßẸẸẺẺẼẼẾẾèẻẽếêầẨẩẪẫẬậũửữơưỀềỄễỆệỈỉỊịỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬữỮữỰựỲỳỴỵỶỷỸỹ"),
    MIXED("Mixed", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzfFjJwWzZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾế ẞßẸẸẺẺẼẼẾẾèẻẽếêầẨẩẪẫẬậũửữơưỀềỄễỆệỈỉỊịỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬữỮữỰựỲỳỴỵỶỷỸỹ");

    private String displayName;
    private String alphabet;

    AlphabetType(String displayName, String alphabet) {
        this.displayName = displayName;
        this.alphabet = alphabet;
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
}
