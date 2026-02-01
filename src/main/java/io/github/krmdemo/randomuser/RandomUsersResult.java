package io.github.krmdemo.randomuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RandomUsersResult {

    @JsonProperty("results") private List<RandomUser> randomUsers;
    @JsonProperty("info") Info info;

    record Info(
        String seed,
        @JsonProperty("results") int resultsCount,
        @JsonProperty("page")int pageNumber,
        String version
    ) {}
}
