package com.walmart.connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.connect.converter.SlackMessageToCandidate;
import com.walmart.connect.model.Candidate;
import com.walmart.connect.response.Attachment;
import com.walmart.connect.response.SlackResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlackController {

    public static final Logger LOGGER = LoggerFactory.getLogger(SlackController.class);

   // private static final List<String> ALLOWED_CHANNELS = Stream.of("directmessage", "pingpong", "kicker", "privategroup").collect(Collectors.toList());

    @Autowired
    ObjectMapper mapper;

    @Autowired
    SlackMessageToCandidate slackMessageToCandidate;


    @RequestMapping(value = "/slack/slash",
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
        response.setText("status for :"+text+"is:");
        response.setResponseType("in_channel");

        Attachment attachment = new Attachment();
        attachment.setText("This is the attachment text");
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
        response.setText("The Tech Pannel Information is");
        response.setResponseType("in_channel");

        Attachment attachment = new Attachment();
        attachment.setText("This is the pannel  info");
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

        return response;
    }

    @RequestMapping(value = "/slack/test",

            method = RequestMethod.GET)
    public SlackResponse onTest( ) {
       Candidate candidate= slackMessageToCandidate.convert("Name: vicky thakur\n" +
                "             EmailId:  vthakurvicky@gmail.com\n" +
                "             Skills:  java,spring-boot, SQL, mongo\n" +
                "             Role:    IN3\n" +
                "             Time_Slot: 20-01-2021:14:00 to 20-01-2021:16:00,  21-01-2021:14:00 to 20-01-2021:16:00, 22-01-2021:14:00 to 20-01-2021:16:00\n" +
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

        response.attachments.add(attachment);

        return response;

    }
}
