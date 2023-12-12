package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlatformUser {

    @JsonProperty
    public String firstName;

    @JsonProperty
    public String lastName;

    @JsonProperty
    public short age;

    @JsonProperty
    public String email;

    @JsonProperty
    public String tier;

    public PlatformUser() {
    }

    public PlatformUser(String firstName, String lastName, short age, String email, String tier) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.tier = tier;
    }

    @Override
    public String toString() {
        return "PlatformUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", tier='" + tier + '\'' +
                '}';
    }
}
