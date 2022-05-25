package com.mgnstn.traderportfolio.controller;

import java.util.List;

import com.mgnstn.traderportfolio.model.Ticker;
import com.mgnstn.traderportfolio.service.MarketService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketController {

    private final Logger log = LoggerFactory.getLogger(MarketController.class);
    @Autowired private MarketService marketService;
    
    @GetMapping("/tickers")
    @CrossOrigin(origins = "*")
    public List<Ticker> getTickers(@RequestParam(required = false, defaultValue = "") List<String> tickerIds) {
        log.info("Fetching tickerIds {}" , tickerIds);
        return marketService.getTickers(tickerIds);
    }

    @GetMapping("/tickers/{tickerId}")
    @CrossOrigin(origins = "*")
    public Ticker getTickerById(@PathVariable String tickerId) {
        return marketService.getTickerById(tickerId);
    }
}
