package ru.rosatom.e2e;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AppConfiguration {

    private final String baseUrl;

    private static AppConfiguration instance;
    private AppConfiguration() {
        this.baseUrl = System.getProperty(Properties.BASE_URL.getName(), Properties.BASE_URL.getDefaultValue());
    }

    public static AppConfiguration getInstance() {
        synchronized (AppConfiguration.class) {
            if (instance == null) {
                instance = new AppConfiguration();
            }
            return instance;
        }
    }

    @Getter
    @AllArgsConstructor
    private enum Properties {
        BASE_URL("target.url", "http://127.0.0.1:8080");

        private final String name;
        private final String defaultValue;

    }
}
