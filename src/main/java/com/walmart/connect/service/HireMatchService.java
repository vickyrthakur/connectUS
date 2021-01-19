package com.walmart.connect.service;


import com.walmart.connect.model.Interviewer;
import com.walmart.connect.model.InterviewerAvailabilityResponse;
import com.walmart.connect.model.InterviewerStatus;
import com.walmart.connect.model.Requirement;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class HireMatchService implements MatchService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CalendarService calendarService;

    @Override
    public List<InterviewerAvailabilityResponse> findPanel(Requirement requirement) {

        String getPanelUrl = "https://connectuspython.herokuapp.com/panel/location/{locationId}/role/{roleId}/department/{departmentId}/round/{roundId}";
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("locationId", requirement.getLocation().getName());
        urlParams.put("roleId", requirement.getRole().name());
        urlParams.put("departmentId", requirement.getDepartment().name());
        urlParams.put("roundId", requirement.getRound().name());
        UriComponentsBuilder panelUrlBuilder = UriComponentsBuilder.fromUriString(getPanelUrl)
                .queryParam("skills", String.join(",", requirement.getSkills()));

        ResponseEntity<List<Interviewer>> matchingInterviewerResponse = restTemplate.exchange(
                panelUrlBuilder.buildAndExpand(urlParams).toUri(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Interviewer>>() {});
        List<Interviewer> matchingInterviewers = matchingInterviewerResponse.getBody();

        List<InterviewerAvailabilityResponse> interviewerAvailabilityResponses = new ArrayList<>();
        for (Interviewer interviewer : matchingInterviewers) {
            for (Pair<Date, Date> timeslot: requirement.getTimeSlots()) {
                if (calendarService.getFreeBusyCalendarInfo(interviewer.getEmail(), timeslot.getKey(), timeslot.getValue())) {
                    interviewerAvailabilityResponses.add(InterviewerAvailabilityResponse.builder()
                            .name(interviewer.getName())
                            .email(interviewer.getEmail())
                            .role(interviewer.getRole())
                            .status(InterviewerStatus.WAITING)
                            .department(requirement.getDepartment())
                            .availableTimeSlot(timeslot)
                            .build());
                    break;
                }
            }
        }
        return interviewerAvailabilityResponses;
    }

    //TODO Stub method to be removed
    private boolean isTimeSlotAvailable(String email, LocalDateTime from, LocalDateTime to) {
        return true;
    }
}
