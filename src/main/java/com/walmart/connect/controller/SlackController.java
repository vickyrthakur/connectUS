package com.walmart.connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.connect.converter.SlackMessageToCandidate;
import com.walmart.connect.model.Candidate;
import com.walmart.connect.model.Department;
import com.walmart.connect.model.InterviewerAvailabilityResponse;
import com.walmart.connect.model.InterviewerStatus;
import com.walmart.connect.response.Attachment;
import com.walmart.connect.response.SlackResponse;
import com.walmart.connect.service.MatchService;
import com.walmart.connect.service.SlackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SlackController {

    public static final Logger LOGGER = LoggerFactory.getLogger(SlackController.class);

    // private static final List<String> ALLOWED_CHANNELS = Stream.of("directmessage", "pingpong", "kicker", "privategroup").collect(Collectors.toList());

    @Autowired
    ObjectMapper mapper;

    @Autowired
    SlackMessageToCandidate slackMessageToCandidate;

    @Autowired
    private MatchService matchService;

    @Autowired
    SlackService slackService;

    @Value("${webhook-conncetus}")
    String webhook;


    @RequestMapping(value = "/slack/status",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public SlackResponse onReceiveSlashCommand(@RequestParam("team_id") String teamId,
                                               @RequestParam("team_domain") String teamDomain,
                                               @RequestParam("channel_id") String channelId,
                                               @RequestParam("channel_name") String channelName,
                                               @RequestParam("user_id") String userId,
                                               @RequestParam("user_name") String userName,
                                               @RequestParam("command") String command,
                                               @RequestParam("text") String text,
                                               @RequestParam("response_url") String responseUrl) {
        SlackResponse response = new SlackResponse();
        response.setText("Status for :" + text + "is:");
        response.setResponseType("in_channel");

        Attachment attachment = new Attachment();
        attachment.setText("status response");
        attachment.setColor("#0000ff");

        response.attachments.add(attachment);

        return response;
    }


    @RequestMapping(value = "/slack/connect",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public SlackResponse onReceiveSlashCommandforConncet(@RequestParam("team_id") String teamId,
                                                         @RequestParam("team_domain") String teamDomain,
                                                         @RequestParam("channel_id") String channelId,
                                                         @RequestParam("channel_name") String channelName,
                                                         @RequestParam("user_id") String userId,
                                                         @RequestParam("user_name") String userName,
                                                         @RequestParam("command") String command,
                                                         @RequestParam("text") String text,
                                                         @RequestParam("response_url") String responseUrl) {
        SlackResponse response = new SlackResponse();
        Candidate candidate = slackMessageToCandidate.convert(text);
        response.setText("The Tech Pannel Information for " + candidate.getName() + "is :");
        response.setResponseType("in_channel");
        List<InterviewerAvailabilityResponse> interviewerAvailabilityResponses = matchService.findPanel(candidate);


        StringBuilder output = new StringBuilder();
        output.append("Tech Panel Information : ");
        for (InterviewerAvailabilityResponse resp : interviewerAvailabilityResponses) {
            output.append(resp.toString());
            output.append("\n");
        }
        Attachment attachment = new Attachment();
        attachment.setText(output.toString());
        attachment.setColor("#0000ff");

        response.attachments.add(attachment);

        return response;
    }

    @RequestMapping(value = "/slack/format",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public SlackResponse onReceiveSlashCommandformat(@RequestParam("team_id") String teamId,
                                                         @RequestParam("team_domain") String teamDomain,
                                                         @RequestParam("channel_id") String channelId,
                                                         @RequestParam("channel_name") String channelName,
                                                         @RequestParam("user_id") String userId,
                                                         @RequestParam("user_name") String userName,
                                                         @RequestParam("command") String command,
                                                         @RequestParam("text") String text,
                                                         @RequestParam("response_url") String responseUrl) {
        SlackResponse response = new SlackResponse();
        response.setText("The Format for Candidate information is : "+"\n" +
                "             Name:\n" +
                "             EmailId:\n" +
                "             Skills:\n" +
                "             Role:    IN1/IN2/IN3/IN4/IN5/IN6\n" +
                "             Time_Slot: YYYY-MM-DD HH:MM to YYYY-MM-DD HH:MM, YYYY-MM-DD HH:MM to YYYY-MM-DD HH:MM\n" +
                "             Team: GBS_FINTECH\n" +
                "             Location : BANGALORE/CHENNAI/US\n" +
                "             Round:  TECH_1/TECH_2/MANAGER/HR\n" +
                "             Experience: year.month");
        response.setResponseType("in_channel");

        StringBuilder output = new StringBuilder();
        output.append("Thank YOU");
        Attachment attachment = new Attachment();
        attachment.setText(output.toString());
        attachment.setColor("#0000ff");

        response.attachments.add(attachment);

        return response;
    }

    @RequestMapping(value = "/slack/slash1",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public SlackResponse onReceiveSlashCommandTest(@RequestParam("team_id") String teamId) {
        SlackResponse response = new SlackResponse();
        response.setText("This is the response text");
        response.setResponseType("in_channel");

        Attachment attachment = new Attachment();
        attachment.setText("This is the attachment text");
        attachment.setColor("#0000ff");

        response.attachments.add(attachment);


        // some service API , i/p -> candiate-> InterviewerAvailabilityResponse

        return response;
    }

    @RequestMapping(value = "/slack/test",

            method = RequestMethod.GET)
    public List<InterviewerAvailabilityResponse> onTest() throws JSONException {
        Candidate candidate = slackMessageToCandidate.convert("Name: vicky thakur\n" +
                "             EmailId:  vthakurvicky@gmail.com\n" +
                "             Skills:  java,spring-boot, SQL, mongo\n" +
                "             Role:    IN3\n" +
                "             Time_Slot: 2021-02-20 14:00 to 2021-02-20 16:00, 2021-02-21 14:00 to 2021-02-21 16:00, 2021-02-22 14:00 to 2021-02-22 16:00\n" +
                "             Team: GBS_FINTECH\n" +
                "             Location : BANGALORE\n" +
                "             Round:  TECH_1\n" +
                "             Experience:  2.6");


        SlackResponse response = new SlackResponse();
        response.setText("This is the response text");
        response.setResponseType("in_channel");

        Attachment attachment = new Attachment();
        attachment.setText(candidate.toString());
        attachment.setColor("#0000ff");

        String msg = InterviewerAvailabilityResponse.
                builder().department(Department.GBS_FINTECH)
                .email("12345@gmail.com")
                .name("vicky thakur")
                .status(InterviewerStatus.WAITING).toString();

        try {
            slackService.postMessage(msg, webhook);
        }catch (Exception e)
        {

        }

        return matchService.findPanel(candidate);

    }




}
