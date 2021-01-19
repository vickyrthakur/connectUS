package com.walmart.connect.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
                "name='" + name + '\'' +   "\n"+
                ", email='" + email + '\'' +   "\n"+
                ", availableTimeSlot=" + availableTimeSlot +   "\n"+
                ", status=" + status +   "\n"+
                ", department=" + department +   "\n"+
                ", role=" + role +  "\n"
                ;
    }
}
