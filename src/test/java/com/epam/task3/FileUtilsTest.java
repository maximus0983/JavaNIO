package com.epam.task3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.epam.task3.FileUtils.copyFileStream;
import static com.epam.task3.FileUtils.time;

class FileUtilsTest {
    private long average;
    private String src = "C:\\Users\\Maxim_Mandzhiev\\Documents\\batch_actions.txt";
    private String dest = "C:\\Users\\Maxim_Mandzhiev\\Pictures\\batch_actions.txt";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        FileUtils.time = 0;
    }

    @Test
    void copyFileStreamTest() {
        long fullTime = 0;
        for (int i = 0; i < 10; i++) {
            copyFileStream(src, dest);
            fullTime = fullTime + FileUtils.time;
        }
        System.out.println(fullTime / 10);
    }

    @Test
    void copyFileStreamBuffer() {
        double fullTime = 0;
        for (int i = 0; i < 10; i++) {
            FileUtils.copyFileStreamBuffer(src, dest);
            fullTime = fullTime + FileUtils.time;
        }
        System.out.println(fullTime / 10);
    }

    @Test
    void copyFileChannel() {
        long fullTime = 0;
        for (int i = 0; i < 10; i++) {
            FileUtils.copyFileChannel(src, dest);
            fullTime = fullTime + FileUtils.time;
        }
        System.out.println(fullTime / 10);

    }

    @Test
    void copyFileUsingJava7Files() throws IOException {
        long fullTime = 0;
        for (int i = 0; i < 10; i++) {
            FileUtils.copyFileUsingJava7Files(src, dest);
            fullTime = fullTime + FileUtils.time;
        }
        System.out.println(fullTime / 10);
    }
}