package com.walmart.connect.model;

import javafx.util.Pair;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
@Builder
public class InterviewerAvailabilityResponse {

    private String name;
    private String email;
    private Pair<Date, Date> availableTimeSlot;
    private InterviewerStatus status;
    private Department department;
    private Role role;
}
