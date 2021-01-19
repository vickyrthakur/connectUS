package com.walmart.connect.service;


import com.walmart.connect.model.Interviewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class SlackService {

    @Autowired
    private RestTemplate restTemplate;

    public void postMessage(String message, String webHookURl) throws JSONException {

        HttpHeaders    headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject postMessage = new JSONObject();
        postMessage.put("text",message);


        HttpEntity<String> entity = new HttpEntity<String>(postMessage.toString(), headers);

        // send request and parse result
        ResponseEntity<String> response = restTemplate
                .exchange(webHookURl, HttpMethod.POST, entity, String.class);


    }


}
