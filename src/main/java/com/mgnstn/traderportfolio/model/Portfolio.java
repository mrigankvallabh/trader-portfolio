package com.mgnstn.traderportfolio.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Component;

@Component
public class Portfolio {
    private List<TickerHolding> holdings = new ArrayList<>();
    private List<String> tickerList = new ArrayList<>();
    private BigDecimal totalValue = BigDecimal.ZERO;
    public Portfolio() {
        holdings.add(new TickerHolding("MGQ", 10000));
        holdings.add(new TickerHolding("INAKU", 12000));

        for (TickerHolding t : holdings) {
            tickerList.add(t.getTickerId());
            totalValue.add(t.getValue());
        }
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

}
