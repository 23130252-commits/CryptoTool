package com.cryptotool.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    /**
     * Đọc nội dung file text
     */
    public static String readTextFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    /**
     * Đọc nội dung file nhị phân
     */
    public static byte[] readBinaryFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    /**
     * Ghi nội dung text vào file
     */
    public static void writeTextFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    /**
     * Ghi nội dung nhị phân vào file
     */
    public static void writeBinaryFile(String filePath, byte[] data) throws IOException {
        Files.write(Paths.get(filePath), data);
    }

    /**
     * Kiểm tra file có tồn tại không
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
}
