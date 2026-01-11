package io.github.krmdemo.randomuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO: debug and test ser/deser according to <a href="https://randomuser.me/api/">Random-User API</a>
 */
@Data
public class RandomUser {

    private Gender gender;
    private Name name;
    private Location location;
    private String email;
    @JsonProperty("dob") private DateOfBirth dateOfBirth;

    public enum Gender {
        MALE("male"),
        FEMALE("female");

        private final String stringValue;
        Gender(String stringValue) {
            this.stringValue = stringValue;
        }

        @JsonValue
        public String stringValue() {
            return stringValue;
        }
    }

    public record Name(
        String title,
        String first,
        String last
    ) {
        public String full() {
            return Stream.of(title, first, last)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(" "));
        }
    }

    public record Location(
        @JsonProperty("street") StreetHouse streetHouse,
        String city,
        String country,
        Integer postalCode
    ) {}

    public record StreetHouse(
        Integer number,
        @JsonProperty("name") String street
    ) {}

    public record DateOfBirth(
        Instant date,
        Integer age
    ) {}
}
