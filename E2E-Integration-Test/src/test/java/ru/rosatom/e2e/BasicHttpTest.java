package ru.rosatom.e2e;

import com.github.javafaker.Faker;
import lombok.Getter;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.rosatom.e2e.user.UserAuthorizationResponse;

import java.util.HashMap;

@SpringJUnitConfig
public abstract class BasicHttpTest {

    @Getter
    private final WebTestClient notAuthClient;

    protected Faker faker = Faker.instance();

    private final AppConfiguration appConfiguration = AppConfiguration.getInstance();

    private static final HashMap<String, Object> context = new HashMap<>();

    public BasicHttpTest() {
        notAuthClient = WebTestClient.bindToServer()
                .baseUrl(appConfiguration.getBaseUrl())
                .defaultHeader("Content-Type", "application/json").build();
    }

    protected WebTestClient withAuthClient(String clientName) {
        UserAuthorizationResponse userAuthorization = getContextValue(clientName);
        if (userAuthorization != null) {
            return notAuthClient.mutate()
                    .defaultHeader("Authorization", String.format("Bearer %s", userAuthorization.getToken()))
                    .build();
        }
        throw new NoClientWithCredentials(clientName);
    }

    protected WebTestClient withNotAuthClient() {
        return notAuthClient;
    }



    protected static <T> T getContextValue(String key) {
        return (T) context.get(key);
    }

    protected static void setContextValue(String key, Object value) {
        context.put(key, value);
    }
}
