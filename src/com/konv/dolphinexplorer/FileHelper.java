package com.konv.dolphinexplorer;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileHelper {

    public static void copy(Path source, Path targetDirectory) {
        try {
            Files.copy(source, targetDirectory.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            DialogHelper.showException(e);
        }
    }

    public static void move(Path source, Path targetDirectory) {
        try {
            Files.move(source, targetDirectory.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            DialogHelper.showException(e);
        }
    }

    public static void delete(Path path) {
        String title = "Delete";
        boolean confirmed = DialogHelper.showConfirmationDialog(title, null,
                "Do you really want to delete " + path.getFileName().toString() + "?");
        if (confirmed) {
            try {
                Files.deleteIfExists(path);
            } catch (DirectoryNotEmptyException e) {
                try {
                    Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Files.deleteIfExists(file);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            Files.deleteIfExists(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException exception) {
                    DialogHelper.showException(exception);
                }
            } catch (IOException e) {
                DialogHelper.showException(e);
            } catch (SecurityException e) {
                DialogHelper.showException(e);
            }
        }
    }

    public static void createDirectory(Path parent) {
        String title = "Create Directory";
        String name = DialogHelper.showTextInputDialog(title, null, "Enter Name", "New Directory");
        if (name != null) {
            Path path = parent.resolve(name);
            try {
                Files.createDirectory(path);
            } catch (FileAlreadyExistsException e) {
                DialogHelper.showAlert(Alert.AlertType.INFORMATION, title, null, "Directory already exists");
            } catch (IOException e) {
                DialogHelper.showAlert(Alert.AlertType.ERROR, title, null, "Can`t create directory" + path.getFileName());
            } catch (SecurityException e) {
                DialogHelper.showAlert(Alert.AlertType.ERROR, title, null, "Security error");
            }
        }
    }

    public static void createFile(Path parent) {
        String title = "Create File";
        String name = DialogHelper.showTextInputDialog(title, null, "Enter Name", "New File.txt");
        if (name != null) {
            Path path = parent.resolve(name);
            try {
                Files.createFile(path);
            } catch (FileAlreadyExistsException e) {
                DialogHelper.showAlert(Alert.AlertType.INFORMATION, title, null, "File already exists");
            } catch (IOException e) {
                DialogHelper.showAlert(Alert.AlertType.ERROR, title, null, "Can`t create file" + path.getFileName());
            } catch (SecurityException e) {
                DialogHelper.showAlert(Alert.AlertType.ERROR, title, null, "Security error");
            }
        }
    }

    public static void rename(Path source) {
        String title = "Rename";
        String name = DialogHelper.showTextInputDialog(title, null, "Enter New Name", source.getFileName().toString());
        if (name != null) {
            Path target = source.getParent().resolve(name);
            try {
                Files.move(source, target);
            } catch (FileAlreadyExistsException e) {
                if (!source.equals(target)) {
                    DialogHelper.showAlert(Alert.AlertType.INFORMATION, title, null, "File already exists");
                }
            } catch (DirectoryNotEmptyException e) {

            } catch (IOException e) {

            } catch (SecurityException e) {

            }
        }
    }
}
