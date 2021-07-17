package kanotLiveUploader.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriterOwn {

    public static void writeFile(String fileName, String fileContent, String filePath)
            throws IOException {
        Path directory = getDirectoryFixed(filePath);

        writeFIle(fileContent, directory.resolve(fileName));
    }

    private static Path getDirectoryFixed(String filePath) {
        String filePathFixed = filePath.trim();
        if (filePathFixed.length() > 0 && !filePathFixed.endsWith("/"))
            filePathFixed = filePathFixed + "/";

        Path directory = createDirectory(filePathFixed);
        return directory;
    }


    private static void writeFIle(String content, Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.append(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void appendRow(String content, String fileName, String filePath) throws IOException {
        Path file = getDirectoryFixed(filePath).resolve(fileName);

        String fileContent = "";
        try {
            fileContent += FileReaderOwn.readFile(file.toAbsolutePath().toString());
        } catch (NoSuchFileException ignored) {
        }

        fileContent += content;

        writeFIle(fileContent, file);
    }


    private static Path createDirectory(String directory) {
        try {
            Path path = Paths.get(directory).toAbsolutePath();
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Path created");
            } else
                System.out.println("file path already exist");
            return path;
        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
            return null;
        }
    }
}