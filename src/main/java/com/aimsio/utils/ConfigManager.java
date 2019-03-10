package com.aimsio.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager configManager;
    private static Properties properties;

    private ConfigManager() {
        String configFil = "config.properties";
        properties = new Properties();

        InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(configFil);
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static ConfigManager getInstance() {
        if (configManager == null) {
            configManager = new ConfigManager();
            return configManager;
        }
        return configManager;
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }
}
