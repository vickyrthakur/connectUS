package com.walmart.connect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Interviewer {

    private String name;
    private String email;
    private Role role;
    private String skills;
//    private Double experience;
    private Location location;
    private Department department;
}
