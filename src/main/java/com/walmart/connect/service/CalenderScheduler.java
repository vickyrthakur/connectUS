package com.walmart.connect.service;


import com.walmart.connect.model.CalendarEvent;
import com.walmart.connect.model.Candidate;
import com.walmart.connect.model.InterviewerStatus;
import javaslang.Tuple3;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@EnableScheduling
@Service
@Slf4j
public class CalenderScheduler {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private SlackService slackService;

    @Value("${webhook-conncetus}")
    String webhook;

    @Scheduled(cron = "${calender_scheduler_interval}")
    public void checkInterviewerCalender() {
        log.info("Calender status updated");
        for (Map.Entry<String, Tuple3<CalendarEvent, InterviewerStatus, Candidate>> calenderStatusEntry :
                HireMatchService.calenderStatusMap.entrySet()) {
            if (calenderStatusEntry.getValue()._2() == InterviewerStatus.WAITING) {
                String eventId = calendarService.getEventStatus(calenderStatusEntry.getValue()._1().getEventId());
                if (!StringUtils.isEmpty(eventId) &&
                        "accepted".equals(calendarService.getEventStatus(calenderStatusEntry.getValue()._1().getEventId()))) {
                    HireMatchService.calenderStatusMap.replace(calenderStatusEntry.getKey(),
                            new Tuple3<>(calenderStatusEntry.getValue()._1(), InterviewerStatus.ACCEPTED, calenderStatusEntry.getValue()._3));
                    String responseMessage = String.join("","Candidate: ", calenderStatusEntry.getValue()._3.getName(),
                            " Interview Request is accepted by ", calenderStatusEntry.getKey(), ".", " Meeting invite Id is: ",
                            calenderStatusEntry.getValue()._1.getMeetingLink());
                    try {
                        slackService.postMessage(responseMessage, webhook);
                    } catch (JSONException ex) {
                        log.error("Failed to post interview schedule to slack");
                    }
                }
            }
        }
    }
}
