package com.cryptotool.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    /**
     * Đọc file thành chuỗi
     */
    public static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

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
     * Đọc file thành mảng byte
     */
    public static byte[] readFileAsBytes(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    /**
     * Ghi chuỗi vào file
     */
    public static void writeFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    /**
     * Ghi nội dung text vào file
     */
    public static void writeTextFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    /**
     * Ghi chuỗi vào file (append mode)
     */
    public static void appendToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(content);
            writer.newLine();
        }
    }

    /**
     * Ghi mảng byte vào file
     */
    public static void writeFileAsBytes(String filePath, byte[] bytes) throws IOException {
        Files.write(Paths.get(filePath), bytes);
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

    /**
     * Xóa file
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.delete();
    }

    /**
     * Tạo folder nếu chưa tồn tại
     */
    public static boolean createDirectory(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }

    /**
     * Lấy kích thước file (bytes)
     */
    public static long getFileSize(String filePath) {
        return new File(filePath).length();
    }

    /**
     * Kiểm tra file có phải folder không
     */
    public static boolean isDirectory(String filePath) {
        return new File(filePath).isDirectory();
    }

    /**
     * Lấy danh sách file trong folder
     */
    public static File[] listFiles(String dirPath) {
        File directory = new File(dirPath);
        if (directory.isDirectory()) {
            return directory.listFiles();
        }
        return null;
    }

    /**
     * Lấy phần mở rộng file
     */
    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
            return filePath.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * Lấy tên file không bao gồm phần mở rộng
     */
    public static String getFileNameWithoutExtension(String filePath) {
        File file = new File(filePath);
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return name.substring(0, lastDotIndex);
        }
        return name;
    }

    /**
     * Copy file
     */
    public static void copyFile(String sourceFile, String destFile) throws IOException {
        Files.copy(Paths.get(sourceFile), Paths.get(destFile));
    }

    /**
     * Rename file
     */
    public static boolean renameFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        return oldFile.renameTo(newFile);
    }
}
