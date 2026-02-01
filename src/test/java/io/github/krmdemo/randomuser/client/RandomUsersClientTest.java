package io.github.krmdemo.randomuser.client;

import io.github.krmdemo.httpclient.HttpClientKind;
import io.github.krmdemo.randomuser.RandomUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RandomUsersClientTest {

    @Test
    void testGetRandomUser() {
        RandomUsersClient client =
            RandomUsersClient.kind(HttpClientKind.JDK)
                .create();
        RandomUser randomUser = client.getRandomUser();
        log.info("randomUser --> {}", randomUser);
        assertThat(randomUser).isNotNull();
    }
}
