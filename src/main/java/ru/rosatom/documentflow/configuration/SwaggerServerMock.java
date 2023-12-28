package ru.rosatom.documentflow.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "swagger", name = "server-mock")
@Configuration
public class SwaggerServerMock {
    @Value("${swagger.server-mock}")
    private String serverMock;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().addServersItem(getServer());
    }

    private Server getServer() {
        Server server = new Server();
        server.setUrl(serverMock);
        return server;
    }
}
