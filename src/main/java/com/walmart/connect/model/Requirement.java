package com.walmart.connect.model;

import javafx.util.Pair;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
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
    private List<Pair<Date, Date>> timeSlots;
    private Department department;
    private Location location;
    private Round round;
    private Double experience;
}
