package ru.rosatom.e2e;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashMap;

@SpringJUnitConfig
public abstract class BasicHttpTest {

    protected WebTestClient client;
    protected Faker faker = Faker.instance();

    private static final HashMap<String, Object> context = new HashMap<>();

    @BeforeEach
    void setUpClient() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://127.0.0.1:8080")
                .defaultHeader("Content-Type", "application/json").build();
    }

    protected static <T> T getContextValue(String key) {
        return (T) context.get(key);
    }

    protected static void setContextValue(String key, Object value) {
        context.put(key, value);
    }
}
