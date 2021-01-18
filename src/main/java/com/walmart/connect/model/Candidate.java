package com.walmart.connect.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Candidate {
    private String name;
    private String email;
    private Role role;
    private List<String> skills;
    private List<Pair<LocalDateTime, LocalDateTime>> availableTimeSlot;
    private Location location;
    private Double experience;
    private Team team;
    private Round round;

}
