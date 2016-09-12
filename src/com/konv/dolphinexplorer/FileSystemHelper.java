package com.konv.dolphinexplorer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileSystemHelper {

    public static boolean copy(Path source, Path targetDirectory) {
        try {
            Files.copy(source, targetDirectory.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean move(Path source, Path targetDirectory) {
        try {
            Files.move(source, targetDirectory.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean delete(Path source) {
        try {
            Files.deleteIfExists(source);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean createDirectory(Path parent, String name) {
        try {
            Files.createDirectory(parent.resolve(name));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean createFile(Path parent, String name) {
        try {
            Files.createFile(parent.resolve(name));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
