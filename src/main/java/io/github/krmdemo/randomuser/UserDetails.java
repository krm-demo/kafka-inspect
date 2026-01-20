package io.github.krmdemo.randomuser;

import lombok.Data;

@Data
public class UserDetails {

    private String userSeed;
    private String userFullName;
    private Integer userAge;
    private Gender userGender;

    private String countryName;
}
