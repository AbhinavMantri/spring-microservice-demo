package com.example.microservice.currencyexchangeservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservice.currencyexchangeservice.bean.CurrencyExchange;
import com.example.microservice.currencyexchangeservice.repository.CurrencyExchangeRepository;

@RestController
public class CurrencyExchangeController {
	
	private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    private Environment environment;

    private CurrencyExchangeRepository repository;

    public CurrencyExchangeController(
        @Autowired Environment environment, 
        @Autowired CurrencyExchangeRepository repository
    ) {
        this.environment = environment;
        this.repository = repository;
    }
    
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(
        @PathVariable String from, @PathVariable String to
    ) {
        //CurrencyExchange currencyExchange = new CurrencyExchange(1000L, from, to, BigDecimal.valueOf(50));
    	// INFO [currency-exchange,b469127fe336b0125b77393daa49810e,641ab3f0e7d4ed9f] 11925 --- [nio-8000-exec-1] c.e.m.c.c.CurrencyExchangeController     : received a called for exchange which call with USD to INR
    	logger.info("received a called for exchange which call with {} to {}", from, to);
    	
        CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);

        if(currencyExchange == null) {
            throw new RuntimeException("Unable to find the data");
        }

        String port = environment.getProperty("local.server.port");

        currencyExchange.setEnvironment(port);

        return currencyExchange;
    }
}
