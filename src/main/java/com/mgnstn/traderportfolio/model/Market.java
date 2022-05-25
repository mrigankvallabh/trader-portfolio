package com.mgnstn.traderportfolio.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

/**
 * Market
 * Market class represents a collection of Tickers being traded on the Stock
 * Market
 */
@Component
public class Market {

    private List<Ticker> tickers;
    private Map<String, Ticker> tickerMap;

    public Market(List<Ticker> tickers) {
        this();
        this.tickers = tickers;
        createTickerMap(tickers);
    }

    private Market() {
        tickers = new ArrayList<>();
        tickerMap = new HashMap<>();
    }

    public void setTickers(List<Ticker> tickers) {
        this.tickers = tickers;
        createTickerMap(tickers);
    }

    public List<Ticker> getTickers(List<String> tickerIds) {
        if (tickerIds.isEmpty() || tickerIds == null)
            return tickers;
        else {
            List<Ticker> result = new ArrayList<>();
            for (String tickerId : tickerIds) {
                result.add(tickerMap.get(tickerId));
            }
            return result;
        }
    }

    public Ticker getTickerById(String tickerId) {
        return tickerMap.get(tickerId);
    }

    private void createTickerMap(List<Ticker> tickers) {
        for (Ticker ticker : tickers) {
            tickerMap.put(ticker.getTickerId(), ticker);
        }
    }

    /**
     * The openMarket function starts a new TickerThread for each Stock.
     */
    public void openMarket() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            for (Ticker ticker : tickers) {
                executor.execute(new TickerThread(ticker));
            }
        } catch (Exception ignored) {
        }
        executor.shutdown();
    }

    /**
     * TickerThread
     * This is a private inner class used by Market class and it is Thread that updates the Stock 
     * randomly between 0.5s to 2.0s
     */
    private class TickerThread implements Runnable {
        private final Ticker ticker;
        private final Random rg = new Random();

        TickerThread(Ticker t) {
            this.ticker = t;
        }

        @Override
        public void run() {
            while (true) {
                double dt = rg.nextDouble() * (2.0 - 0.5) + 0.5;
                try {
                    Thread.sleep((long) (dt * 1000));
                    ticker.updatePrice(dt);
                } catch (InterruptedException ignored) {
                    break;
                }
            }
        }

    }

}
