package com.mgnstn.traderportfolio.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Component;

/**
 * Portfolio Class represents the Trader Ticker Holding and its NAV
 */
@Component
public class Portfolio {
    private List<TickerHolding> holdings = new ArrayList<>();
    private BigDecimal totalValue = BigDecimal.ZERO;
    public Portfolio() { }

    public void setHoldings(List<TickerHolding> holdings) {
        this.holdings = holdings;
    }

    public List<TickerHolding> getHoldings() {
        return holdings;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public String toString() {
        return "Portfolio [holdings=" + holdings + ", totalValue=" + totalValue.setScale(4, RoundingMode.HALF_UP) + "]";
    }

}
