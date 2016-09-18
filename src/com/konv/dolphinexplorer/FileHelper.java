package com.konv.dolphinexplorer;

import javafx.scene.control.Alert;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    public static void copy(List<Path> source, Path target) {
        List<Path> uncopiable = new ArrayList<>();
        for (Path path : source) {
            try {
                File sourceFile = path.toFile();
                if (sourceFile.isDirectory()) {
                    FileUtils.copyDirectoryToDirectory(sourceFile, target.toFile());
                } else {
                    FileUtils.copyFileToDirectory(sourceFile, target.toFile());
                }
            } catch (Exception e) {
                uncopiable.add(path);
            }
        }
        if (uncopiable.size() > 0) {
            String sourceDirectory = uncopiable.get(0).getParent().toString();
            String content = "";
            for (Path path : uncopiable) {
                content += path.toString() + System.lineSeparator();
            }
            String message = "Some files were not copied properly";
            DialogHelper.showAlert(Alert.AlertType.INFORMATION, sourceDirectory, message, content);
        }
    }

    public static void move(List<Path> source, Path targetDirectory) {
        List<Path> unmovable = new ArrayList<>();
        for (Path path : source) {
            try {
                FileUtils.moveToDirectory(path.toFile(), targetDirectory.toFile(), false);
            } catch (Exception e) {
                unmovable.add(path);
            }
        }
        if (unmovable.size() > 0) {
            String sourceDirectory = unmovable.get(0).getParent().toString();
            String content = "";
            for (Path path : unmovable) {
                content += path.toString() + System.lineSeparator();
            }
            String message = "Some files were not moved properly";
            DialogHelper.showAlert(Alert.AlertType.INFORMATION, sourceDirectory, message, content);
        }
    }

    public static void delete(List<Path> source) {
        String sourceDirectory = source.get(0).getParent().toString();

        String filesToDelete = "";
        for (Path path : source) filesToDelete += path.toString() + System.lineSeparator();
        boolean isConfirmed = DialogHelper.showExpandableConfirmationDialog(sourceDirectory, "Delete",
                "Do you really want to delete selected files?", filesToDelete);

        if (isConfirmed) {
            List<Path> undeleted = new ArrayList<>();
            for (Path path : source) {
                try {
                    if (path.toFile().isDirectory()) {
                        FileUtils.deleteDirectory(path.toFile());
                    } else {
                        FileUtils.forceDelete(path.toFile());
                    }
                } catch (Exception e) {
                    undeleted.add(path);
                }
            }
            if (undeleted.size() > 0) {
                String content = "";
                for (Path path : undeleted) content += path.toString() + System.lineSeparator();
                String message = "Some files were not deleted";
                DialogHelper.showAlert(Alert.AlertType.INFORMATION, sourceDirectory, message, content);
            }
        }
    }

    public static void createDirectory(Path parent) {
        String title = parent.toString();
        String name = DialogHelper.showTextInputDialog(title, null, "New Directory", "My Directory");
        if (name != null) {
            Path path = parent.resolve(name);
            try {
                Files.createDirectory(path);
            } catch (FileAlreadyExistsException e) {
                DialogHelper.showAlert(Alert.AlertType.INFORMATION, title, "Directory already exists", path.toString());
            } catch (Exception e) {
                DialogHelper.showAlert(Alert.AlertType.INFORMATION, title, "Directory was not created", path.toString());
            }
        }
    }

    public static void createFile(Path parent) {
        String title = parent.toString();
        String name = DialogHelper.showTextInputDialog(title, null, "New File", "Text File.txt");
        if (name != null) {
            Path path = parent.resolve(name);
            try {
                Files.createFile(path);
            } catch (FileAlreadyExistsException e) {
                DialogHelper.showAlert(Alert.AlertType.INFORMATION, title, "File already exists", path.toString());
            } catch (Exception e) {
                DialogHelper.showAlert(Alert.AlertType.ERROR, title, "File was not created", path.toString());
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
            } catch (Exception e) {
                DialogHelper.showAlert(Alert.AlertType.INFORMATION, source.getParent().toString(), "File was not renamed",
                        source.toString());
            }
        }
    }
}
