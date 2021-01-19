package com.walmart.connect.service;

import com.walmart.connect.model.Candidate;
import com.walmart.connect.model.InterviewerAvailabilityResponse;
import com.walmart.connect.model.Requirement;

import java.util.List;

public interface MatchService {

    List<InterviewerAvailabilityResponse> findPanel(Candidate requirement);

    InterviewerAvailabilityResponse selectPanel(List<InterviewerAvailabilityResponse> panel);
}
