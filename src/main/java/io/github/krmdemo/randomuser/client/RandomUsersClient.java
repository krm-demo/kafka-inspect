package io.github.krmdemo.randomuser.client;

import io.github.krmdemo.httpclient.HttpClientKind;
import io.github.krmdemo.httpclient.HttpParamsClient;
import io.github.krmdemo.randomuser.RandomUser;
import io.github.krmdemo.randomuser.RandomUsersResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public interface RandomUsersClient {

    String BASE_API_URL__RANDOM_USERS = "https://randomuser.me/api/";

    default RandomUser getRandomUser() {
        RandomUsersResult result = getRandomUsersResult();
        return result.getRandomUsers().stream().findFirst().orElseThrow(
            () -> new IllegalStateException("no random users are available")
        );
    }

    RandomUsersResult getRandomUsersResult();

    /**
     * Creator (factory-method) for {@link Factory}
     *
     * @param kind a kind of low-level HTTP-client
     * @return an instance of {@link HttpParamsClient.HttpFactory} according to {@code kind}
     */
    static Factory kind(HttpClientKind kind) {
        return new Factory(kind);
    }

    @Getter
    @Setter
    @Accessors(fluent = true, chain = true)
    class Factory {
        private final HttpParamsClient.HttpFactory httpFactory;
        private Factory(HttpClientKind kind) {
            this.httpFactory = HttpParamsClient.httpKind(kind)
                .baseUrl(BASE_API_URL__RANDOM_USERS);
        }
        public RandomUsersClient create() {
            return new RandomUsersClientImpl(this);
        }
    }
}
