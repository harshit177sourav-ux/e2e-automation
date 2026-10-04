package com.harshitsourav.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.Properties;

public final class Config {

    private static final String ENV = System.getProperty("env", "qa");
    private static final Properties FILE = load(ENV);

    private Config() {

    }

    public static String env() {
        return ENV;
    }

    public static String apiBaseUrl() {
        return get("api.base.url");
    }

    public static String uiBaseUrl() {
        return get("ui.base.url");
    }

    public static String browser() {
        return get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static Duration explicitWait() {
        return Duration.ofSeconds(Long.parseLong(get("explicit.wait.second")));
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key.toUpperCase().replace('.', '_'));
        }
        if (value == null) {
            value = FILE.getProperty(key);
        }
        if (value == null) {
            throw new IllegalStateException(
                    "Missing Config key " + key + "for the ENV : " + ENV);
        }
        return value;
    }

    private static Properties load(String env) {
        String path = "/config/" + env + ".properties";
        try (InputStream in = Config.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("No config file found on class path" + path);
            }
            Properties props = new Properties();
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String secret(String env) {
        String value = System.getenv(env);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Required secret not set: environment variable" + env);
        }
        return value;
    }
}