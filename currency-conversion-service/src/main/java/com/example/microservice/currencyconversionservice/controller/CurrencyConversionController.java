package com.example.microservice.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.microservice.currencyconversionservice.bean.CurrencyConversion;
import com.example.microservice.currencyconversionservice.proxy.CurrencyExchangeProxy;

@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration {
    
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

@RestController
public class CurrencyConversionController {

    private CurrencyExchangeProxy proxy;
    
    private RestTemplate restTemplate;

    public CurrencyConversionController(@Autowired CurrencyExchangeProxy proxy, @Autowired RestTemplate restTemplate) {
        this.proxy = proxy;
        this.restTemplate = restTemplate;
    }
    
    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion retrieveCurrencyConversion(
        @PathVariable String from, 
        @PathVariable String to, 
        @PathVariable BigDecimal quantity
    ) {

        HashMap<String, String> uriVariables = new HashMap<>();
        
        uriVariables.put("from", from);

        uriVariables.put("to", to);
        
        ResponseEntity<CurrencyConversion> responseEntity = restTemplate.getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);


        CurrencyConversion conversion = responseEntity.getBody();

        if(conversion == null) {
            throw new RuntimeException("Record is not found");
        }

        conversion.setId(10001L);

        conversion.setQuantity(quantity);

        conversion.setTotalCalculatedAmount(quantity.multiply(conversion.getConversionMultiple()));

        conversion.setEnvironment("");

        return conversion;

        //return new CurrencyConversion(10001L, from, to, quantity, BigDecimal.ONE, BigDecimal.ONE, "");
    }

    @GetMapping("/currency-conversion/feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion retrieveFeignCurrencyConversion(
        @PathVariable String from, 
        @PathVariable String to, 
        @PathVariable BigDecimal quantity
    ) {
        CurrencyConversion conversion = proxy.retrieveExchangeValue(from, to);

        if(conversion == null) {
            throw new RuntimeException("Record is not found");
        }

        conversion.setId(10001L);

        conversion.setQuantity(quantity);

        conversion.setTotalCalculatedAmount(quantity.multiply(conversion.getConversionMultiple()));

        conversion.setEnvironment("");

        return conversion;
    }

}