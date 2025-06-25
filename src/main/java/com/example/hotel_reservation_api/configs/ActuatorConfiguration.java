package com.example.hotel_reservation_api.configs;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.Info;

@Configuration
public class ActuatorConfiguration {
    @Bean
    public InfoContributor customInfoContributor() {
        return builder -> builder
                .withDetail("custom-message", "Hello from Actuator!")
                .build();
    }
}
