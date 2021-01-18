package com.walmart.connect.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConnectUsConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


}
