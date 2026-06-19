package com.food.delivery.schedular;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@ConditionalOnProperty(
        prefix = "app.scheduler.keep-alive",
        name = "enabled",
        havingValue = "true"
)
public class KeepAliveScheduler {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.base-url}")
    private String baseUrl;

    @Scheduled(cron = "${app.scheduler.keep-alive.cron}")
    public void keepAlive() {
        try {
            String response = restTemplate.getForObject(
                    baseUrl + "/api/health",
                    String.class
            );

            log.info("Health Check : {}", response);

        } catch (Exception ex) {
            log.error("Health check failed", ex);
        }
    }
}