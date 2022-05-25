package com.mgnstn.traderportfolio.service;

import java.util.List;

import com.mgnstn.traderportfolio.model.Market;
import com.mgnstn.traderportfolio.model.Ticker;
import com.mgnstn.traderportfolio.repository.TickerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketService {

    private @Autowired Market market;
    @SuppressWarnings("unused") private @Autowired TickerRepository tickerRepository; // Not used but can be used to persist data

    public List<Ticker> getTickers(List<String> tickerIds) {
        return market.getTickers(tickerIds);
    }

    public Ticker getTickerById(String tickerId) {
        return market.getTickerById(tickerId);
    }

    public void testHello() {
        System.out.println("Market Service available");
    }
}
