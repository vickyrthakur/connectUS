package com.walmart.connect.model;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
public class Requirement {

    private String name;
    private String email;
    private List<String> skills;
    private Role role;
    private List<Pair<LocalDateTime, LocalDateTime>> timeSlots;
    private Team team;
    private Location location;
    private Round round;
    private Double experience;
}
