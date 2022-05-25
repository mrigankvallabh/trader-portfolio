package com.mgnstn.traderportfolio.config;

import java.util.ArrayList;
import java.util.List;

import com.mgnstn.traderportfolio.model.Market;
import com.mgnstn.traderportfolio.model.Ticker;
import com.mgnstn.traderportfolio.repository.TickerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Initialize {
    private final Logger log = LoggerFactory.getLogger(Initialize.class);
    private @Autowired Market market;
    private @Autowired TickerRepository tickerRepository;

    @Bean
    CommandLineRunner initApp() {
        return args -> {
            tickerRepository.save(new Ticker("MGQ", 14.57, 0.17, 0.12)); // Regular Stock
            tickerRepository.save(new Ticker("RAF", 17.44, 0.08, 0.06)); // Regular Stock
            tickerRepository.save(new Ticker("ANT", 7.21, 0.23, 0.19)); // Regular Stock
            
            // 6 months Call Option on INA (initial price of 14.68, mu = 0.18, sigma = 0.12) at Strike Price = 15.00
            tickerRepository.save(new Ticker("INAKU", "C", 14.68, 0.18, 0.12, 15.00, 6)); 
            
            // 9 months Put Option on GKV (initial price of 16.44, mu = 0.15, sigma = 0.08) at Strike Price = 16.00
            tickerRepository.save(new Ticker("GKVQV", "P", 16.44, 0.15, 0.08, 16.00, 9));
            
            List<Ticker> tickers = new ArrayList<>();
            tickerRepository.findAll().forEach(ticker -> {
                log.info("Preloaded " + ticker);
                tickers.add(ticker);
            });
            market.setTickers(tickers);
            market.openMarket();
        };
    }
}
