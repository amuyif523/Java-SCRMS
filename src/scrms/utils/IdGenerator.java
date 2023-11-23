package scrms.utils;

import java.util.UUID;

/**
 * Utility responsible for generating human readable identifiers.
 */
public final class IdGenerator {

    private IdGenerator() {
    }

    /**
     * Generates a new identifier with the provided prefix.
     *
     * @param prefix identifier prefix such as STD or CRS
     * @return generated identifier
     */
    public static String newId(String prefix) {
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + "-" + uuid;
    }
}
