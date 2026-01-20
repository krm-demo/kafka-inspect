package io.github.krmdemo.randomuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RandomUsersResponse {

    @JsonProperty("results") private List<RandomUser> randomUsers;
    @JsonProperty("info")ResponseInfo responseInfo;

    record ResponseInfo(
        String seed,
        @JsonProperty("results") int resultsCount,
        @JsonProperty("page")int pageNumber,
        String version
    ) {}
}
