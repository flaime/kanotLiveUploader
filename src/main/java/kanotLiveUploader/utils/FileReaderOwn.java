package kanotLiveUploader.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileReaderOwn {


    public static String readFile(String fileName)
            throws IOException {
        return Files.lines(Paths.get(fileName), StandardCharsets.UTF_8).map(s -> s + "\n").collect(Collectors.joining());
    }
}
