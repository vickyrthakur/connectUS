package com.walmart.connect.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Candidate {
    private String name;
    private String email;
    private Role role;
    private List<String> skills;
    private List<TimePair> availableTimeSlot;
    private Location location;
    private Double experience;
    private Department department;
    private Round round;


    @Override
    public String toString() {
        return "Candidate{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", skills=" + skills +
                ", availableTimeSlot=" + availableTimeSlot +
                ", location=" + location +
                ", experience=" + experience +
                ", department=" + department +
                ", round=" + round +
                '}';
    }
}
