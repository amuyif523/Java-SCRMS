package scrms.utils;

import scrms.exceptions.PersistenceException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * File utility helpers for reading and writing JSON resources.
 */
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Ensures the parent directory of the provided path exists.
     *
     * @param path file path
     */
    public static void ensureParent(Path path) {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new PersistenceException("Unable to create directory for " + path, e);
        }
    }

    /**
     * Reads the complete file into a string. Missing files yield an empty string.
     *
     * @param path file path
     * @return file content
     */
    public static String readFile(Path path) {
        try {
            if (!Files.exists(path)) {
                return "";
            }
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenceException("Unable to read file " + path, e);
        }
    }

    /**
     * Writes the provided content into the file.
     *
     * @param path    file path
     * @param content file content
     */
    public static void writeFile(Path path, String content) {
        try {
            ensureParent(path);
            Files.writeString(path, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenceException("Unable to write file " + path, e);
        }
    }
}
