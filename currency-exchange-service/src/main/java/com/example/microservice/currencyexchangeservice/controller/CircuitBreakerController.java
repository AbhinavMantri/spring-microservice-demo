package com.example.microservice.currencyexchangeservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
// import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	@GetMapping("/sample-api")
	// @Retry(name = "sample-api", fallbackMethod = "hardCodeResponse")
	@CircuitBreaker(name = "sample-api", fallbackMethod = "hardCodeResponse")
	@RateLimiter(name = "default")
	public String sampleApi() {
		logger.info("Sample api has called");
		// ResponseEntity<String> res = new RestTemplate().getForEntity("http://localhost:7000/api", String.class);
		return "Sample API";
	}
	
	public String hardCodeResponse(Exception ex) {
		return "fallback Response";
	}
}
