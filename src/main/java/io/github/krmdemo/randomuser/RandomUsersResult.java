package io.github.krmdemo.randomuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * This class represents REST-data-object represents the whole HTTP-response
 * from <a href="https://randomuser.me/api/">Random-User API</a>, which in addition to
 * the list if {@link io.github.krmdemo.randomuser.RandomUser} contains the
 * {@link RandomUsersResult.Info information} about paging and random-seed.
 */
@Data
public class RandomUsersResult {

    /**
     * The list of random users (elements of type {@link RandomUser})
     */
    @JsonProperty("results") private List<RandomUser> randomUsers;

    /**
     * Additional technical information (random-seed, paging, API-version)
     */
    @JsonProperty("info") Info info;

    /**
     * This java-record represents the additional technical information
     * to the {@link #randomUsers list of random-users}
     *
     * @param seed a random-seed that allows to retrieve the predictable result
     * @param resultsCount total number of random-users records in the response
     * @param pageNumber the number of page
     * @param version the version of API (the current one must be something like {@code 1.4})
     */
    record Info(
        String seed,
        @JsonProperty("results") int resultsCount,
        @JsonProperty("page") int pageNumber,
        String version
    ) {}
}
