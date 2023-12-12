package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CloudUser {

    @JsonProperty
    public String firstName;

    @JsonProperty
    public String lastName;

    @JsonProperty
    public short age;

    @JsonProperty
    public String email;

    @JsonProperty
    public String provider;

    @JsonProperty
    public String tier;

    @JsonProperty
    public short montlyConsumption;

    public CloudUser() {
    }

    public CloudUser(String firstName, String lastName, short age, String email, String provider, String tier, short montlyConsumption) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.provider = provider;
        this.tier = tier;
        this.montlyConsumption = montlyConsumption;
    }

    @Override
    public String toString() {
        return "CloudUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", provider='" + provider + '\'' +
                ", tier='" + tier + '\'' +
                ", montlyConsumption=" + montlyConsumption +
                '}';
    }
}
