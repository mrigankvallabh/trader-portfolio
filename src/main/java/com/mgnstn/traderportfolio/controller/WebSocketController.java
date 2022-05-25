package com.mgnstn.traderportfolio.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mgnstn.traderportfolio.service.PortfolioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private PortfolioService portfolioService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);

    /**
     * sendPrices is called when the client sends a message to /app/portfolio endpoint on WebSocket
     * WebSocket connection endpoint is /ws-message
     * the Client can then subscibe to /topic/message
     */
    @MessageMapping("/portfolio-nav")
    public void sendPrices() {
        scheduler.scheduleAtFixedRate(
                () -> {
                    template.convertAndSend("/topic/message", portfolioService.getPortfolio());
                },
                0,
                2,
                TimeUnit.SECONDS);
    }
}
