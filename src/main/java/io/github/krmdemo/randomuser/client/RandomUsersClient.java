package io.github.krmdemo.randomuser.client;

import io.github.krmdemo.httpclient.HttpClientKind;
import io.github.krmdemo.httpclient.HttpParamsClient;
import io.github.krmdemo.randomuser.RandomUser;
import io.github.krmdemo.randomuser.RandomUsersResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This interface represents a REST-API to {@value BASE_API_URL__RANDOM_USERS}.
 *
 * @see <a href="https://randomuser.me/api/">Random-User API</a>
 */
public interface RandomUsersClient {

    /**
     * Base (root) URL of <a href="https://randomuser.me/api/">Random-User API</a>
     */
    String BASE_API_URL__RANDOM_USERS = "https://randomuser.me/api/";

    /**
     * Getting the single random-user for the random (unpredictable) seed.
     *
     * @return a random-user as {@link RandomUser}
     * @throws IllegalStateException if no random-users were retuned
     */
    default RandomUser getRandomUser() {
        RandomUsersResult result = getRandomUsersResult();
        return result.getRandomUsers().stream().findFirst().orElseThrow(
            () -> new IllegalStateException("no random users are available")
        );
    }

    /**
     * Getting the single random-user result for the random (unpredictable) seed,
     * where the property {@link RandomUsersResult#getRandomUsers() randomUsers}
     * is the list of one {@link RandomUser} element.
     *
     * @return a random-user with additional information as {@link RandomUsersResult}
     */
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
