package ru.rosatom.documentflow.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String login;
    private String password;
    private String prefix;
    private String endpoint;
    private String localEndpoint;
}
