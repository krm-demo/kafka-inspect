package io.github.krmdemo.randomuser.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.krmdemo.httpclient.HttpParamsClient;
import io.github.krmdemo.randomuser.RandomUser;
import io.github.krmdemo.randomuser.RandomUsersResult;
import org.krmdemo.techlabs.core.utils.JacksonUtils;

import java.util.Collections;

/**
 * Implementation of {@link RandomUsersClient} that is based on {@link HttpParamsClient}
 * without any high-level wrappers like Spring's {@code RestTemplate} or {@code RestClient}..
 */
public class RandomUsersClientImpl implements RandomUsersClient {

    private final HttpParamsClient httpParamsClient;

    RandomUsersClientImpl(Factory factory) {
        this.httpParamsClient = factory.httpFactory().create();
    }

    @Override
    public RandomUsersResult getRandomUsersResult() {
        String responseBody = httpParamsClient.httpGetBodyString(Collections.emptyMap());
        return JacksonUtils.jsonValueFromString(responseBody, new TypeReference<>(){});
    }
}
