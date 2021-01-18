package com.walmart.connect.service;

import com.walmart.connect.model.InterviewerAvailabilityResponse;
import com.walmart.connect.model.Requirement;

import java.util.List;

public interface MatchService {

    List<InterviewerAvailabilityResponse> findPanel(Requirement requirement);
}
