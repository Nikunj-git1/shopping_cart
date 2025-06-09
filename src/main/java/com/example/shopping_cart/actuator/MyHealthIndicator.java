package com.example.shopping_cart.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MyHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean healthy = true; // या कोई कस्टम लॉजिक

        if (healthy) {
            return Health.up().withDetail("CustomHealthCheck", "Everything is good!").build();
        } else {
            return Health.down().withDetail("CustomHealthCheck", "Something went wrong!").build();
        }
    }
}
