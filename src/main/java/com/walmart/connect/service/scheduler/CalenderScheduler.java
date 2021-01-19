package com.walmart.connect.service.scheduler;


import com.walmart.connect.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class CalenderScheduler {

    @Autowired
    private CalendarService calendarService;

    @Scheduled(cron = "${calender_scheduler_interval}")
    public void checkInterviewerCalender() {

        System.out.println("Triggered checkInterviewerCalender()");
    }
}
