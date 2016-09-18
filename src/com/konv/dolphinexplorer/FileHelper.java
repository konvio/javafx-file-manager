package com.konv.dolphinexplorer;

import javafx.scene.control.Alert;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileHelper {

    public static void copy(List<Path> source, Path target) {
        for (Path path : source) {
            try {
                File sourceFile = path.toFile();
                if (sourceFile.isDirectory()) {
                    FileUtils.copyDirectoryToDirectory(sourceFile, target.toFile());
                } else {
                    FileUtils.copyFileToDirectory(sourceFile, target.toFile());
                }
            } catch (IOException e) {
                DialogHelper.showException(e);
            }
        }
    }

    public static void move(List<Path> source, Path targetDirectory) {
        for (Path path : source) {
            try {
                FileUtils.moveToDirectory(path.toFile(), targetDirectory.toFile(), false);
            } catch (Exception e) {
                DialogHelper.showException(e);
            }
        }
    }

    public static void delete(List<Path> source) {
        String title = "Delete";
        DialogHelper.showExpandableAlert(Alert.AlertType.INFORMATION, "Title", "Header", "Content", "ExpandableContent");
        for (Path path : source) {
            boolean confirmed = DialogHelper.showConfirmationDialog(title, null,
                    "Do you really want to delete " + path.getFileName().toString() + "?");
            if (confirmed) {
                try {
                    if (path.toFile().isDirectory()) {
                        FileUtils.deleteDirectory(path.toFile());
                    } else {
                        FileUtils.forceDelete(path.toFile());
                    }
                } catch (IOException e) {
                    DialogHelper.showException(e);
                }
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
                FileUtils.moveToDirectory(source.toFile(), target.toFile(), true);
            } catch (IOException e) {
                DialogHelper.showException(e);
            }
        }
    }
}
