package com.epam.task3;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtils {
    public static long time;

    public static void main(String[] args) throws IOException {
        String src = "C:\\Users\\Maxim_Mandzhiev\\Documents\\batch_actions.txt";
        String dest = "C:\\Users\\Maxim_Mandzhiev\\Pictures\\batch_actions.txt";
        copyFileStream(src, dest);
        copyFileStreamBuffer(src, dest);
        copyFileChannel(src, dest);
        copyFileUsingJava7Files(src, dest);
    }

    public static void copyFileStream(String src, String dest) {
        long start = System.currentTimeMillis();
        try (InputStream is = new FileInputStream(src);
             OutputStream os = new FileOutputStream(dest)) {
            int i;
            while ((i = is.read()) > 0) {
                os.write(i);
            }
            long end = System.currentTimeMillis();
            time = end - start;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyFileStreamBuffer(String src, String dest) {
        long start = System.currentTimeMillis();
        try (InputStream is = new FileInputStream(src);
             OutputStream os = new FileOutputStream(dest);) {
            byte[] buffer = new byte[100_000];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            long end = System.currentTimeMillis();
            time = end - start;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyFileChannel(String src, String dest) {
        long start = System.currentTimeMillis();
        try (FileInputStream fus = new FileInputStream(src); FileChannel sourceChannel = fus.getChannel();
             FileOutputStream fos = new FileOutputStream(dest); FileChannel destChannel = fos.getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            long end = System.currentTimeMillis();
            time = end - start;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyFileUsingJava7Files(String source, String destination) throws IOException {
        long start = System.currentTimeMillis();
        Path src = Path.of(source);
        Path dest = Path.of(destination);
        Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
        long end = System.currentTimeMillis();
        time = end - start;
    }
}
