package org.example.healthcare.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PublicHealthAlertService {
    
    private static final Logger logger = LoggerFactory.getLogger(PublicHealthAlertService.class);
    
    @Value("${health.alerts.api.enabled:false}")
    private boolean alertsApiEnabled;
    
    @Value("${health.alerts.api.url:}")
    private String alertsApiUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    // In-memory storage for alerts (in production, this would be in database)
    private final List<Map<String, Object>> alerts = new ArrayList<>();
    
    public List<Map<String, Object>> getActiveAlerts() {
        if (alertsApiEnabled && !alertsApiUrl.isEmpty()) {
            try {
                // Fetch from external API (WHO, local health authority, etc.)
                logger.info("Fetching alerts from external API");
                // Placeholder for actual API call
                return fetchAlertsFromExternalAPI();
            } catch (Exception e) {
                logger.error("Failed to fetch alerts from external API: {}", e.getMessage());
            }
        }
        
        // Return local alerts
        return alerts.stream()
            .filter(alert -> (Boolean) alert.getOrDefault("active", true))
            .toList();
    }
    
    public Map<String, Object> createAlert(String title, String description, String severity) {
        Map<String, Object> alert = new HashMap<>();
        alert.put("id", alerts.size() + 1);
        alert.put("title", title);
        alert.put("description", description);
        alert.put("severity", severity);
        alert.put("active", true);
        alert.put("createdAt", LocalDateTime.now());
        
        alerts.add(alert);
        logger.info("Created new health alert: {}", title);
        
        return alert;
    }
    
    private List<Map<String, Object>> fetchAlertsFromExternalAPI() {
        // Placeholder for actual API integration
        // This would make HTTP calls to external health alert APIs
        return new ArrayList<>();
    }
}

