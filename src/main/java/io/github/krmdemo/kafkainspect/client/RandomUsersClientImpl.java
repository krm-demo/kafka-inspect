package io.github.krmdemo.kafkainspect.client;

import io.github.krmdemo.randomuser.RandomUser;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link RandomUsersClient} that is based on JDK HTTP-Client.
 */
public class RandomUsersClientImpl implements RandomUsersClient {

    private final String baseUrl;
    public RandomUsersClientImpl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public RandomUser getRandomUser() {
        return null;
    }

}
