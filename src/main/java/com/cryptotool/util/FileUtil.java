package com.cryptotool.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    private FileUtil() {
    }

    public static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }

    public static byte[] readFileAsBytes(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    public static void writeFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8));
    }

    public static void writeFileAsBytes(String filePath, byte[] bytes) throws IOException {
        Files.write(Paths.get(filePath), bytes);
    }
}