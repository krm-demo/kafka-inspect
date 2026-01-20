package io.github.krmdemo.kafkainspect.config;

import io.github.krmdemo.httpclient.HttpClientKind;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Configuration
public class ClientConfig {

    @Value("${app.client.factory.random-users}")
    private EnumSet<HttpClientKind> httpClientKinds;

    @Value("${app.client.endpoints.random-users}")
    private String randomUserEndpoint;


}
