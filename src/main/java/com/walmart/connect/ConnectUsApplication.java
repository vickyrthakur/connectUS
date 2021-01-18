package com.walmart.connect;

import com.walmart.connect.converter.SlackMessageToCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConnectUsApplication {


	@Autowired
	SlackMessageToCandidate slackMessageToCandidate;
	public static void main(String[] args) {

		SpringApplication.run(ConnectUsApplication.class, args);

	}

}
