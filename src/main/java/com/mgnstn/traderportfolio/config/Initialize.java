package com.mgnstn.traderportfolio.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.mgnstn.traderportfolio.model.Market;
import com.mgnstn.traderportfolio.model.Portfolio;
import com.mgnstn.traderportfolio.model.Ticker;
import com.mgnstn.traderportfolio.model.TickerHolding;
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
    private @Autowired Portfolio portfolio;

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
            Path path = Path.of("files/portfolio.csv");

            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                String line = reader.readLine();
                List<TickerHolding> tickerHoldings = new ArrayList<>();
                while (line != null) {
                    if (!line.matches("^#.*$")) {
                        String[] parts = line.split(",");
                        tickerHoldings.add(new TickerHolding(parts[0], Integer.parseInt(parts[1])));
                    }
                    line = reader.readLine();
                }
                portfolio.setHoldings(tickerHoldings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
