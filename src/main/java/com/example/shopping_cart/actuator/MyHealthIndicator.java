package com.example.shopping_cart.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

// Optional- if we want to give custome mgs. to response

@Component
public class MyHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean healthy = true;

        if (healthy) {
            return Health.up().withDetail("CustomHealthCheck", "Everything is good!").build();
        } else {
            return Health.down().withDetail("CustomHealthCheck", "Something went wrong!").build();
        }
    }
}