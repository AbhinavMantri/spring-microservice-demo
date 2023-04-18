package com.example.microservice.limit_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservice.limit_service.bean.Limit;
import com.example.microservice.limit_service.configuration.Configuration;

@RestController
public class LimitController {
    
    private Configuration configuration;

    public LimitController(@Autowired Configuration configuration) {
        this.configuration = configuration;
    }

    @GetMapping("/limits")
    public Limit retrieveLimit() {
        return new Limit(configuration.getMinimum(), configuration.getMaximum());
    }
}
