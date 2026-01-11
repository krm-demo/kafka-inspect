package io.github.krmdemo.randomuser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The data-class represents all hierarchical properties of <b>Random-User</b>
 *
 * @see  <a href="https://randomuser.me/api/">Random-User API</a>
 */
@Data
public class RandomUser {

    private Gender gender;
    private Name name;
    private Location location;
    private String email;
    @JsonProperty("login") Credentials credentials;
    @JsonProperty("dob") private DateAge dateOfBirth;
    @JsonProperty("registered") private DateAge registeredAt;
    @JsonProperty("phone") private String phoneNumber;
    @JsonProperty("cell") private String cellPhoneNum;
    NameValue id;
    @JsonProperty("picture") PictureLinks pictureLnks;
    @JsonProperty("nat") String nationality;

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
        String state,
        Integer postcode,
        GeoCoordinates coordinates,
        TimeZone timezone
    ) {}

    public record StreetHouse(
        Integer number,
        @JsonProperty("name") String street
    ) {}

    public record GeoCoordinates(
        Double latitude,  // TODO: try to address trailing zero using << pattern = "#.####" >>
        Double longitude
    ) {}

    public record TimeZone(
        String offset,
        String description
    ) {
        // TODO: it make sense to add some useful methods to get the native Java DateTime values
    }

    @SuppressWarnings("SpellCheckingInspection")  // <-- IDEA does not like date-time-format spelling
    public record DateAge(
        @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",  // <-- 'T' and 'Z' are surraounded with single quotes!
            timezone = "UTC")
        Instant date,
        Integer age
    ) {}

    public record NameValue(
        String name,
        String value
    ) {}

    public record PictureLinks(
        String large,
        String medium,
        String thumbnail
    ) {}

    public record Credentials(
        UUID uuid,
        String username,
        String password,
        String salt,
        String md5,
        String sha1,
        String sha256
    ) {}
}
