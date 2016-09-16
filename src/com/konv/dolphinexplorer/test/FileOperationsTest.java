package com.konv.dolphinexplorer.test;

import com.konv.dolphinexplorer.FileHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.fail;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;

public class FileOperationsTest {
    private File mDirectory;
    private File mSourceDirectory;
    private File mTargetDirectory;

    @Before
    public void setupTestDirectory() {
        mDirectory = new File("D:/DolphinExplorerUnitTest");
        mDirectory.mkdir();
        mSourceDirectory = new File("D:/DolphinExplorerUnitTest/Source");
        mSourceDirectory.mkdir();
        mTargetDirectory = new File("D:/DolphinExplorerUnitTest/Target");
        mTargetDirectory.mkdir();
    }

    @Test
    public void singleFileCopyToOtherDirectory() {
        File file = new File("D:/DolphinExplorerUnitTest/Source/testFile.txt");
        try {
            file.createNewFile();
            FileHelper.copy(file.toPath(), mTargetDirectory.toPath());
            String[] targetFiles = mTargetDirectory.list();
            assertThat(targetFiles, arrayContaining("testFile.txt"));
            assertThat(file.exists(), equalTo(true));
        } catch (Exception e) {
            fail();
        }
    }

    @After
    public void deleteTestDirectory() {
        try {
            deleteDirectory(mDirectory);
        } catch (IOException e) {
            fail();
        }
    }
}
