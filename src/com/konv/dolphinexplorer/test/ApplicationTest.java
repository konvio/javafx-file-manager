package com.konv.dolphinexplorer.test;

import com.konv.dolphinexplorer.FileHelper;
import com.konv.dolphinexplorer.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;

public class ApplicationTest {
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
        try {
            File file = new File("D:/DolphinExplorerUnitTest/Source/testFile.txt");
            file.createNewFile();
            List<Path> selection = new ArrayList<>();
            selection.add(file.toPath());
            FileHelper.copy(selection, mTargetDirectory.toPath());
            String[] targetFiles = mTargetDirectory.list();
            assertThat(targetFiles, arrayContaining("testFile.txt"));
            assertThat(file.exists(), equalTo(true));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void nonEmptyDirectoryDeletion() {
        try {
            File file = new File("D:/DolphinExplorerUnitTest/Source/testFile.txt");
            file.createNewFile();
            deleteDirectory(mSourceDirectory);
            assertThat(mSourceDirectory.exists(), equalTo(false));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void splitWordsTest() {
        String text = "Split,, me properly.Please,";
        String[] words = StringHelper.getWords(text);
        assertThat(words, arrayContaining("split", "me", "properly", "please"));
    }

    @Test
    public void containsWordTest() {
        String text = "fjaljflj ,jl aflj !!find,,,djfald; ";
        String word = "find";
        assertThat(StringHelper.containsWord(text, word), equalTo(true));
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
