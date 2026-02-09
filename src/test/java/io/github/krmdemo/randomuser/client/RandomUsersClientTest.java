package io.github.krmdemo.randomuser.client;

import io.github.krmdemo.httpclient.HttpClientKind;
import io.github.krmdemo.randomuser.RandomUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RandomUsersClientTest {

    @ParameterizedTest
    @EnumSource(names = {
        "JDK",
        "APACHE_HTTP",
        "OK_HTTP"
    })
    void testGetRandomUser(HttpClientKind httpClientKind) {
        RandomUsersClient client = RandomUsersClient.kind(httpClientKind).create();
        RandomUser randomUser = client.getRandomUser();
        log.info("randomUser --> {}", randomUser);
        assertThat(randomUser).isNotNull();
    }
}
