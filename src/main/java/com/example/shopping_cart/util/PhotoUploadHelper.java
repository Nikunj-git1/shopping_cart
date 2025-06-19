package com.example.shopping_cart.util;

import org.springframework.beans.factory.annotation.Value;

public class PhotoUploadHelper {

    @Value("${shopping_cart.file_upload_path}")
    private String fileUploadPath;


    public static String cleanFileName(String fileName) {

        if (fileName == null || fileName.isEmpty()) {

            return System.currentTimeMillis() + "_" + "default_file";
        }

        String cleaned = fileName.replaceAll("[^a-zA-Z0-9._]", "_");

        while (cleaned.contains("__")) {
            cleaned = cleaned.replaceAll("__", "_");
        }

        cleaned = System.currentTimeMillis() + "_" + cleaned;

        // Split name and extension
        int dotIndex = cleaned.lastIndexOf('.');
        String namePart = cleaned;
        String extension = "";

        if (dotIndex > 0 && dotIndex < cleaned.length() - 1) {
            namePart = cleaned.substring(0, dotIndex);
            extension = cleaned.substring(dotIndex); // includes the dot
        }

        int maxLength = 30;
        int allowedNameLength = maxLength - extension.length();

        if (cleaned.length() > maxLength && allowedNameLength > 0) {
            namePart = namePart.substring(0, Math.min(namePart.length(), allowedNameLength));
        }

        return namePart + extension;
    }
}