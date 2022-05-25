package com.mgnstn.traderportfolio.model;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class TickerHolding {
    private String tickerId;
    private int noOfContracts;
    private BigDecimal value = BigDecimal.ZERO;

    public TickerHolding(String tickerId, int noOfContracts) {
        this();
        this.tickerId = tickerId;
        this.noOfContracts = noOfContracts;
    }

    public TickerHolding() {
        this.tickerId = "";
        this.noOfContracts = 0;
    }

    public String getTickerId() {
        return tickerId;
    }

    public int getNoOfContracts() {
        return noOfContracts;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}