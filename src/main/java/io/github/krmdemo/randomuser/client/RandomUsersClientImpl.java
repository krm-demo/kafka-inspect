package io.github.krmdemo.randomuser.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.krmdemo.httpclient.HttpParamsClient;
import io.github.krmdemo.randomuser.RandomUser;
import io.github.krmdemo.randomuser.RandomUsersResult;
import lombok.extern.slf4j.Slf4j;
import org.krmdemo.techlabs.core.utils.JacksonUtils;

import java.util.Collections;

/**
 * Implementation of {@link RandomUsersClient} that is based on {@link HttpParamsClient}
 * without any high-level wrappers like Spring's {@code RestTemplate} or {@code RestClient}..
 */
@Slf4j
public class RandomUsersClientImpl implements RandomUsersClient {

    private final HttpParamsClient httpParamsClient;

    RandomUsersClientImpl(Factory factory) {
        this.httpParamsClient = factory.httpFactory().create();
    }

    @Override
    public RandomUsersResult getRandomUsersResult() {
        String responseBody = httpParamsClient.httpGetBodyString(Collections.emptyMap());
        log.info("getRandomUsersResult responseBody:\n---\n{}\n---\n", responseBody);
        return JacksonUtils.jsonValueFromString(responseBody, new TypeReference<>(){});
    }
}
