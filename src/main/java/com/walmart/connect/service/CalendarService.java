package com.walmart.connect.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.walmart.connect.model.CalendarEvent;
import com.walmart.connect.model.Candidate;
import javaslang.control.Try;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class CalendarService {
    @SuppressWarnings("deprecation")
    public GoogleCredential getCredentials() {
        GoogleCredential googleCredential = Try.of(() -> GoogleCredential.fromStream(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("credentials-free-busy.json"))
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
            if (freeBusyResponse != null && freeBusyCalendar != null && freeBusyCalendar.getBusy().isEmpty())
                return true;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public GoogleCredential getCredentialsToSendInvite() {
        try {
            GoogleCredential credential = GoogleCredential.fromStream(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("credentials-free-busy.json"))
                    .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
         //   System.out.println(credential.toString());
            credential.refreshToken();
         //   System.out.println(credential.getAccessToken());
            return credential;
            //getFreeBusyCalendarInfo(credential);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public CalendarEvent createEvent(String interviewerEmailId, Date from, Date to, Candidate candidate) {
        GoogleCredential credentials = getCredentialsToSendInvite();
        HttpTransport httpTransport = new ApacheHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
                .setApplicationName("applicationName").build();
        String subject = String.format("%s %s Interview Invite", candidate.getName(), candidate.getRole());
        Event event = new Event()
                .setSummary(subject)
                .setDescription("Thank you for providing your availability! Your interview has been confirmed for the following\n" +
                        "date(s) and time(s)." + "\n"
                        + "Please note that I have set up your videoconference as a Google Meet video session. Kindly follow the directions below to\n" +
                        "set up the session prior to your meeting and to test your connection. For the best quality, please connect with Wi-Fi\n" +
                        "and in a quiet location.");
        DateTime startDateTime = new DateTime(from);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Kolkata");
        event.setStart(start);
        DateTime endDateTime = new DateTime(to);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Kolkata");
        event.setEnd(end);
        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail(interviewerEmailId)
        };
        event.setAttendees(Arrays.asList(attendees));
        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);
        ConferenceSolutionKey conferenceSKey = new ConferenceSolutionKey();
        conferenceSKey.setType("hangoutsMeet"); // Non-G suite user
        CreateConferenceRequest createConferenceReq = new CreateConferenceRequest();
        createConferenceReq.setRequestId("3whatisup3"); // ID generated by you
        createConferenceReq.setConferenceSolutionKey(conferenceSKey);
        ConferenceData conferenceData = new ConferenceData();
        conferenceData.setCreateRequest(createConferenceReq);
        event.setConferenceData(conferenceData); // attach the meeting to your event
        String calendarId = "primary";
        try {
            event = service.events().insert(calendarId, event).setConferenceDataVersion(1).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Event created: %s\n", event.getId());
        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setEventId(event.getId());
        calendarEvent.setMeetingLink(event.getHangoutLink());
        return calendarEvent;
    }

    public String getEventStatus(String eventId) {
        GoogleCredential credentials = getCredentialsToSendInvite();
        HttpTransport httpTransport = new ApacheHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
                .setApplicationName("applicationName").build();
        Event event;
        try {
            event = service.events().get("primary", eventId).execute();
            List<EventAttendee> attendees = event.getAttendees();
            if (attendees != null && !attendees.isEmpty()) {
                if (!StringUtils.isEmpty(attendees.get(0).get("responseStatus"))) ;
                return attendees.get(0).get("responseStatus").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}















