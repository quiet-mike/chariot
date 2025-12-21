package com.example.pinewood.model;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class Racer {
    private String id = UUID.randomUUID().toString();

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String number; // entered manually

    private String carName;

    public Racer() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }

    public String displayName() {
        return firstName + " " + lastName + " (#" + number + ")";
    }
}
