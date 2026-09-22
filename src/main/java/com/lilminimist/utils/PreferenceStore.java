package com.lilminimist.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class PreferenceStore {
    private final Path file = Paths.get("data", "preferences.properties");
    private final Map<Long, String> styles = new ConcurrentHashMap<>();

    public PreferenceStore() {
        load();
    }

    public String styleFor(long userId) {
        return styles.getOrDefault(userId, "mix");
    }

    public boolean hasPreference(long userId) {
        return styles.containsKey(userId);
    }

    public synchronized void setStyle(long userId, String style) {
        styles.put(userId, style);
        save();
    }

    public int size() {
        return styles.size();
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(file)) {
            properties.load(input);
            for (String key : properties.stringPropertyNames()) {
                try {
                    styles.put(Long.parseLong(key), properties.getProperty(key));
                } catch (NumberFormatException ignored) {
                    // Ignore malformed user keys rather than failing bot startup.
                }
            }
        } catch (IOException ignored) {
            // Preferences are optional; the bot still works with in-memory defaults.
        }
    }

    private void save() {
        try {
            Files.createDirectories(file.getParent());
            Properties properties = new Properties();
            styles.forEach((userId, style) -> properties.setProperty(String.valueOf(userId), style));
            try (OutputStream output = Files.newOutputStream(file)) {
                properties.store(output, "LilMinimist user preferences");
            }
        } catch (IOException ignored) {
            // Do not crash a command because local preference persistence is unavailable.
        }
    }
}