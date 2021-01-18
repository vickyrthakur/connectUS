package com.walmart.connect.service;

import com.walmart.connect.model.Interviewer;
import com.walmart.connect.model.InterviewerAvailabilityResponse;
import com.walmart.connect.model.Requirement;
import javaslang.collection.List;

public interface MatchService {

    List<InterviewerAvailabilityResponse> find(Requirement requirement);
}
