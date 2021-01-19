package com.walmart.connect.service;

import com.walmart.connect.model.Candidate;
import com.walmart.connect.model.InterviewerAvailabilityResponse;
import com.walmart.connect.model.InterviewerStatus;

import java.util.List;

public interface MatchService {

    List<InterviewerAvailabilityResponse> findPanel(Candidate requirement);

    InterviewerStatus getInterviewerAvailabilityStatus(String email);
}
