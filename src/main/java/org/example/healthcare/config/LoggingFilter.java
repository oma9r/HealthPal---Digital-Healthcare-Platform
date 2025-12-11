package org.example.healthcare.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class LoggingFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, 
                        jakarta.servlet.ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Log request
            logger.info("Request: {} {} from {}", 
                httpRequest.getMethod(), 
                httpRequest.getRequestURI(),
                httpRequest.getRemoteAddr());
            
            chain.doFilter(request, response);
            
            // Log response
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Response: {} {} - Status: {} - Duration: {}ms", 
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                httpResponse.getStatus(),
                duration);
        } finally {
            MDC.clear();
        }
    }
}

