package com.epam.task2;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class DiskAnalyzer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path:");
        String dir = scanner.nextLine();
        String letter = null;
        if (!isValidPath(dir)) {
            System.out.println("Enter valid path:");
            dir = scanner.nextLine();
        }
        System.out.println("Enter method number:");
        int methodNumber = scanner.nextInt();
        if (methodNumber < 1 || methodNumber > 4) {
            System.out.println("Please, input method number between 1 - 4");
            methodNumber = scanner.nextInt();
        } else if (methodNumber == 4) {
            scanner.nextLine();
            System.out.println("Enter letter:");
            letter = scanner.nextLine();
        }
        try {
            switch (methodNumber) {
                case 1:
                    String res = search(dir);
                    System.out.println(res);
                    break;
                case 2:
                    topFiveLargestFiles(dir).forEach(p -> {
                        try {
                            System.out.println(p + " with size " + Files.size(p));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case 3:
                    System.out.println(averageFileSize(dir));
                    break;
                case 4:
                    numberOfFilesAndFolders(dir, letter).forEach((p, k) -> System.out.println(p + " " + k));
            }
        } catch (InvalidPathException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileCalculator<Path> findFileWithContainsMaxS() {
        return (path, list) -> {
            if (path.getFileName().toString().contains("s")) {
                list.add(path);
            }
            return list;
        };
    }

    public static FileCalculator<Path> addFile() {
        return (path, list) -> {
            list.add(path);
            return list;
        };
    }

    private static String search(String directory) throws IOException {
        Path path = Path.of(directory);
        List<Path> files = new ArrayList<>();
        extractFilesFromPath(path, files, findFileWithContainsMaxS());
        Optional<Path> max = files.stream().max(Comparator.comparing(path1 -> path1.getFileName().toString().chars().filter(ch -> ch == 's').count()));
        return max.map(path1 -> path1.getFileName().toString()).orElse("");
    }

    private static List<Path> topFiveLargestFiles(String dir) throws IOException {
        Path path = Path.of(dir);
        List<Path> files = new ArrayList<>();
        extractFilesFromPath(path, files, addFile());
        return files.stream().sorted(Comparator.comparing(path1 -> {
            try {
                return Files.size((Path) path1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).reversed()).limit(5).collect(Collectors.toList());
    }

    private static Map<String, Long> numberOfFilesAndFolders(String dir, String letter) throws IOException {
        Path path = Path.of(dir);
        Map<String, Long> resultMap = new HashMap<>();
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                Path parent = file.getParent();
                Path relativize = parent.relativize(file);
                if (relativize.toString().startsWith(letter)) {
                    resultMap.merge("Files", 1L, Long::sum);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path parent = dir.getParent();
                Path relativize = parent.relativize(dir);
                if (relativize.toString().startsWith(letter)) {
                    resultMap.merge("Folders", 1L, Long::sum);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                if (exc instanceof AccessDeniedException) {
                    return FileVisitResult.SKIP_SUBTREE;
                }

                return super.visitFileFailed(file, exc);
            }
        });
        return resultMap;
    }

    private static double averageFileSize(String dir) throws IOException {
        Path path = Path.of(dir);
        List<Path> files = new ArrayList<>();
        extractFilesFromPath(path, files, addFile());
        return files.stream().mapToDouble(p -> {
            try {
                return Files.size(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).average().orElse(0d);
    }

    private static void extractFilesFromPath(Path path, List<Path> files, FileCalculator<Path> fileCalculator) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                fileCalculator.get(file, files);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                if (exc instanceof AccessDeniedException) {
                    return FileVisitResult.SKIP_SUBTREE;
                }

                return super.visitFileFailed(file, exc);
            }
        });
    }

    public static boolean isValidPath(String path) {
        File file = new File(path);
        return (file.isDirectory());
    }
}
