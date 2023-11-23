package scrms.data;

import scrms.utils.FileUtils;
import scrms.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Generic JSON file repository able to store and read typed entities.
 *
 * @param <T> entity type
 */
public class DataStore<T> {

    private final Path filePath;
    private final Function<String, T> fromJson;
    private final Function<T, String> toJson;

    /**
     * Creates a new repository bound to a specific file inside the data directory.
     *
     * @param fileName target file, e.g. students.json
     * @param fromJson converter from JSON string to entity
     * @param toJson   converter from entity to JSON string
     */
    public DataStore(String fileName, Function<String, T> fromJson, Function<T, String> toJson) {
        this.filePath = Paths.get("data", fileName);
        this.fromJson = fromJson;
        this.toJson = toJson;
    }

    /**
     * Loads all entities from the file.
     *
     * @return list of entities
     */
    public synchronized List<T> load() {
        String raw = FileUtils.readFile(filePath);
        List<T> entities = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return entities;
        }
        for (String object : JsonUtils.splitJsonArray(raw)) {
            if (!object.isBlank()) {
                entities.add(fromJson.apply(object));
            }
        }
        return entities;
    }

    /**
     * Persists the provided entities into the file.
     *
     * @param entities entities to write
     */
    public synchronized void save(List<T> entities) {
        List<String> serialized = new ArrayList<>();
        for (T entity : entities) {
            serialized.add(toJson.apply(entity));
        }
        FileUtils.writeFile(filePath, JsonUtils.wrapArray(serialized));
    }

    /**
     * @return file backing this store
     */
    public Path getFilePath() {
        return filePath;
    }
}
