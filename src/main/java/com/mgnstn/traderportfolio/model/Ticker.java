package com.mgnstn.traderportfolio.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.mgnstn.traderportfolio.config.MarketConstants;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ticker
 * Ticker class represents a Ticker on the Stock Market. It is an Entity that can be persisted at the Database.
 * There are three types of Tickers:
 * Common Stock: S (The TickerId, InitialPrice, ExpectedReturn, and Volatility are required fields)
 * Call Option: C (In addition to above - Strike Price and Expiration in Months are required)
 * Put Option: P (In addition to above - Strike Price and Expiration in Months are required)
 */
@Entity
public class Ticker {
    
    private final @Transient Logger log = LoggerFactory.getLogger(Ticker.class);
    private @Id @Column(nullable = false, unique = true) String tickerId;
    private String tickerType = "S";
    private BigDecimal initialPrice = BigDecimal.ZERO;
    private double mu;
    private double sigma;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal strikePrice = BigDecimal.ZERO;
    private Integer expirationInMonths = null;

    /**
     * Generic Ticker Constructor
     * @param tickerId
     * @param tickerType S: Stock, C: Call Option, P: Put Option
     * @param initialPrice Start of the day price
     * @param mu Expected Return [0, 1]
     * @param sigma Volatility [0, 1]
     * @param strikePrice Required for Options
     * @param expirationInMonths Expiration period for Options: Required for Options
     * @throws Exception
     */
    public Ticker(String tickerId, String tickerType, double initialPrice, double mu, double sigma,
            Double strikePrice, Integer expirationInMonths) throws Exception {

        this(tickerId, initialPrice, mu, sigma);
        try {
            this.tickerType = tickerType;
            if(!(tickerType.equals("S") || tickerType.equals("C") || tickerType.equals("P"))) {
                log.info("Received Invalid TickerType, default to Common Stock (S)");
                this.tickerType = "S";
                tickerType = "S";
            }
            this.strikePrice = BigDecimal.valueOf(strikePrice);
            this.expirationInMonths = expirationInMonths;
            if(!tickerType.equals("S")) {
                if((strikePrice == null || expirationInMonths == null))
                    throw new RuntimeException("strikePrice and expiration required for Options");
                this.price = ( tickerType.equals("C") ? getCallOptionPrice() : getPutOptionPrice() );
            }
        } catch (Exception e) {
            processError(e);
        }
    
    }

    /**
     * Common Stock Constructor: TickerType defauled to "S"
     * @param tickerId
     * @param initialPrice Start of the day price
     * @param mu Expected Return [0, 1]
     * @param sigma Volatility [0, 1]
     * @throws Exception
     */
    public Ticker(String tickerId, double initialPrice, double mu, double sigma) throws Exception {

        this();
        this.initialPrice.setScale(4, RoundingMode.HALF_UP);
        this.strikePrice.setScale(4, RoundingMode.HALF_UP);
        this.price.setScale(4, RoundingMode.HALF_UP);
        try {
            if (tickerId == null) throw new RuntimeException("TickerId required");
            this.tickerId = tickerId;
            this.initialPrice = BigDecimal.valueOf(initialPrice);
            this.price = this.initialPrice;
            this.mu = mu;
            this.sigma = sigma;
        } catch (Exception e) {
            processError(e);
        }
    
    }

    
    public String getTickerId() {
        return tickerId;
    }

    public String getTickerType() {
        return tickerType;
    }
    
    /* Uncomment these Getter if you want to get full Object

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public double getMu() {
        return mu;
    }

    public double getSigma() {
        return sigma;
    }

    public BigDecimal getStrikePrice() {
        return strikePrice;
    }

    public Integer getExpirationInMonths() {
        return expirationInMonths;
    }
    */
    public BigDecimal getPrice() {
        return price;
    }


    private void processError(Exception e) throws Exception {
        log.error(e.getMessage());
        throw e;
    }

    // No Args Constructor - Required if using JPA
    private Ticker() { }

    @Override
    public String toString() {
        return "Ticker [tickerId=" + tickerId + ", tickerType=" + tickerType + ", initialPrice=" + initialPrice
                + ", mu=" + mu + ", sigma=" + sigma + ", strikePrice=" + strikePrice + ", expirationInMonths="
                + expirationInMonths + "]";
    }

    public void updatePrice(double dt) {
        NormalDistribution snd = new NormalDistribution(0, 1);
        double d = dt / 7257600.0;
        double eps = snd.sample();
        double dp = mu * d + sigma * eps * Math.sqrt(d);
        price = price.multiply(BigDecimal.valueOf(1 + dp));
    }

    private BigDecimal getCallOptionPrice() {
        NormalDistribution snd = new NormalDistribution(0, 1);
        double d1 = d1();
        double d2 = d2(d1);
        double nd1 = snd.cumulativeProbability(d1);
        double nd2 = snd.cumulativeProbability(d2);

        return
            price
                .multiply(BigDecimal.valueOf(nd1))
                .subtract(
                    strikePrice
                        .multiply(BigDecimal.valueOf(nd2))
                        .multiply(BigDecimal.valueOf(Math.pow(Math.E, -MarketConstants.R * timeInYears())))
                )
        ;
    }

    private BigDecimal getPutOptionPrice() {
        NormalDistribution snd = new NormalDistribution(0, 1);
        double d1 = d1();
        double d2 = d2(d1);
        double nd1 = snd.cumulativeProbability(-d1);
        double nd2 = snd.cumulativeProbability(-d2);
        // return k * nd2 * Math.pow(Math.E, -MarketConstants.R * t) - price * nd1;
        return
            strikePrice
                .multiply(BigDecimal.valueOf(nd2))
                .multiply(BigDecimal.valueOf(Math.pow(Math.E, -MarketConstants.R * timeInYears())))
                .subtract(
                    price.multiply(BigDecimal.valueOf(nd1))
                )
        ;
    }

    private double d1() {
        return
            ( ( Math.log(price.divide(strikePrice, 6, RoundingMode.HALF_UP).doubleValue()) ) + ( (MarketConstants.R + sigma * sigma / 2.0) * timeInYears() ) )
            /
            (sigma * Math.sqrt(timeInYears())
        );
    }

    private double timeInYears() {
        return expirationInMonths / 12.0;
    }

    private double d2 (double d1) {
        return d1 - sigma * Math.sqrt(timeInYears());
    }
}
