package com.walmart.connect.service;


import com.walmart.connect.model.*;
import javaslang.Tuple3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static com.walmart.connect.model.InterviewerStatus.WAITING;

@Service
public class HireMatchService implements MatchService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CalendarService calendarService;

    static HashMap<String, Tuple3<CalendarEvent, InterviewerStatus, Candidate>> calenderStatusMap = new HashMap<>();

    @Override
    public List<InterviewerAvailabilityResponse> findPanel(Candidate candidate) {

        String getPanelUrl = "https://connectuspython.herokuapp.com/panel/location/{locationId}/role/{roleId}/department/{departmentId}/round/{roundId}";
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("locationId", candidate.getLocation().getName());
        urlParams.put("roleId", candidate.getRole().name());
        urlParams.put("departmentId", candidate.getDepartment().name());
        urlParams.put("roundId", candidate.getRound().name());
        UriComponentsBuilder panelUrlBuilder = UriComponentsBuilder.fromUriString(getPanelUrl)
                .queryParam("skills", String.join(",", candidate.getSkills()));

        ResponseEntity<List<Interviewer>> matchingInterviewerResponse = restTemplate.exchange(
                panelUrlBuilder.buildAndExpand(urlParams).toUri(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Interviewer>>() {});
        List<Interviewer> matchingInterviewers = matchingInterviewerResponse.getBody();

        List<InterviewerAvailabilityResponse> interviewerAvailabilityResponses = new ArrayList<>();
        List<InterviewerAvailabilityResponse> allResponses = new ArrayList<>();
        for (Interviewer interviewer : matchingInterviewers) {
            allResponses.add(InterviewerAvailabilityResponse.builder()
                    .name(interviewer.getName())
                    .email(interviewer.getEmail())
                    .role(interviewer.getRole())
                    .status(WAITING)
                    .department(candidate.getDepartment())
                    .availableTimeSlot(candidate.getAvailableTimeSlot().get(0))
                    .build());
            for (TimePair timeslot: candidate.getAvailableTimeSlot()) {
                if (calendarService.getFreeBusyCalendarInfo(interviewer.getEmail(), timeslot.getKey(), timeslot.getValue())) {
                    CalendarEvent calendarEvent = calendarService.createEvent(
                            interviewer.getEmail(), timeslot.getKey(), timeslot.getValue(), candidate);
                    calenderStatusMap.put(interviewer.getEmail(), new Tuple3<>(calendarEvent, WAITING, candidate));
                    interviewerAvailabilityResponses.add(InterviewerAvailabilityResponse.builder()
                            .name(interviewer.getName())
                            .email(interviewer.getEmail())
                            .role(interviewer.getRole())
                            .status(WAITING)
                            .department(candidate.getDepartment())
                            .availableTimeSlot(timeslot)
                            .build());
                    break;
                }
            }
        }
        return interviewerAvailabilityResponses.isEmpty() ? allResponses : interviewerAvailabilityResponses;
    }

    @Override
    public InterviewerStatus getInterviewerAvailabilityStatus(String email) {
        if (calenderStatusMap.containsKey(email)) {
            return calenderStatusMap.get(email)._2();
        }
        return WAITING;
    }
}
