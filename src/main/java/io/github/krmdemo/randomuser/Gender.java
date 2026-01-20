package io.github.krmdemo.randomuser;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This java-enumeration represents the user's gender
 */
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
