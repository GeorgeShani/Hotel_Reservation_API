package com.example.hotel_reservation_api.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.info.InfoContributor;

@Configuration
public class ActuatorConfiguration {
    @Bean
    public InfoContributor customInfoContributor() {
        return builder -> builder
                .withDetail("custom-message", "Hello from Actuator!")
                .build();
    }
}
