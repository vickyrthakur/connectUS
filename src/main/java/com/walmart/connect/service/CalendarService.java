package com.walmart.connect.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import javaslang.control.Try;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.FreeBusyCalendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;

@Service
public class CalendarService {

    @SuppressWarnings("deprecation")
    public GoogleCredential getCredentials() {
        GoogleCredential googleCredential = Try.of(() -> GoogleCredential.fromStream(
                new FileInputStream(this.getClass().getClassLoader().getResource("credentials.json").getPath()))
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR)))
                .getOrElseThrow((ex) -> new RuntimeException("Cannot get google credentials", ex));
        Try.of(googleCredential::refreshToken).getOrElseThrow((ex) -> new RuntimeException("Cannot refresh token", ex));
        return googleCredential;
    }

    //TODO: We can add timezone as well
    public boolean getFreeBusyCalendarInfo(String email, Date from, Date to) {

        GoogleCredential credentials = getCredentials();
        DateTime timeMin = new DateTime(from);
        DateTime timeMax = new DateTime(to);

        HttpTransport httpTransport = new ApacheHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
                .setApplicationName("applicationName").build();
        FreeBusyResponse freeBusyResponse = null;
        try {
            FreeBusyRequest freeBusyRequest = new FreeBusyRequest();
            freeBusyRequest.setTimeMax(timeMax);
            freeBusyRequest.setTimeMin(timeMin);
            List<FreeBusyRequestItem> freeBusyRequestItems = new ArrayList<FreeBusyRequestItem>();
            FreeBusyRequestItem freeBusyRequestItem = new FreeBusyRequestItem();
            freeBusyRequestItem.put("id", email);
            freeBusyRequestItems.add(freeBusyRequestItem);
            freeBusyRequest.setItems(freeBusyRequestItems);

            freeBusyResponse = service.freebusy().query(freeBusyRequest).execute();
            FreeBusyCalendar freeBusyCalendar = freeBusyResponse.getCalendars().get(email);
            if(freeBusyResponse != null && freeBusyCalendar!=null && freeBusyCalendar.getBusy().isEmpty())
                return true;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
