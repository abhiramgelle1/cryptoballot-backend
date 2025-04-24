package com.example.cryptoballot.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter implements Filter {

    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    
    private Map<String, RequestCounter> ipRequestMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ipAddress = httpRequest.getRemoteAddr();
        
        RequestCounter counter = ipRequestMap.computeIfAbsent(ipAddress, k -> new RequestCounter());
        long now = System.currentTimeMillis();
        synchronized (counter) {
            if (now - counter.startTime > 60000) {
                counter.startTime = now;
                counter.count.set(0);
            }
            if (counter.count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                httpResponse.getWriter().write("Too many requests - Rate limit exceeded.");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private static class RequestCounter {
        volatile long startTime = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
    }
}
