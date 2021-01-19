package com.walmart.connect.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;

@Setter
@Getter
@Builder
public class InterviewerAvailabilityResponse {

    private String name;
    private String email;
    private TimePair availableTimeSlot;
    private InterviewerStatus status;
    private Department department;
    private Role role;

    @Override
    public String toString() {
        return
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", availableTimeSlot=" + availableTimeSlot +
                ", status=" + status +
                ", department=" + department +
                ", role=" + role
                ;
    }
}
