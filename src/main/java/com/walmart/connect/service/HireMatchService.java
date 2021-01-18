package com.walmart.connect.service;


import com.walmart.connect.model.Interviewer;
import com.walmart.connect.model.InterviewerAvailabilityResponse;
import com.walmart.connect.model.Requirement;
import javaslang.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HireMatchService implements MatchService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<InterviewerAvailabilityResponse> find(Requirement requirement) {

        ResponseEntity<List<Interviewer>> interviewers = restTemplate.exchange(
                "url", HttpMethod.GET, null, new ParameterizedTypeReference<List<Interviewer>>() {});
        List<Interviewer> interviewerList = interviewers.getBody();

        //TODO logic for filtering



        return List.empty();
    }
}
