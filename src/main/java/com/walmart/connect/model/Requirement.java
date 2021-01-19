package com.walmart.connect.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@Builder
public class Requirement {

    private String name;
    private String email;
    private List<String> skills;
    private Role role;
    private List<TimePair> timeSlots;
    private Department department;
    private Location location;
    private Round round;
    private Double experience;
}
