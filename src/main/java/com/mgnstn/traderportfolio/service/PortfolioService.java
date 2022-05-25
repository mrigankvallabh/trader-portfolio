package com.mgnstn.traderportfolio.service;

import java.math.BigDecimal;

import com.mgnstn.traderportfolio.model.Portfolio;
import com.mgnstn.traderportfolio.model.TickerHolding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {
    @Autowired private Portfolio portfolio;
    @Autowired private MarketService marketService;
    private Logger log = LoggerFactory.getLogger(PortfolioService.class);
    public Portfolio getPortfolio() {
        double sum = 0.0;
        for (TickerHolding holding : portfolio.getHoldings()) {
            holding.setValue(
                marketService
                    .getTickerById(holding.getTickerId())
                    .getPrice()
                    .multiply(BigDecimal.valueOf(holding.getNoOfContracts()))
            );
            sum += holding.getValue().doubleValue();
        }
        portfolio.setTotalValue(BigDecimal.valueOf(sum));
        log.info(portfolio.toString());
        return portfolio;
    }
}
