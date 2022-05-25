package com.mgnstn.traderportfolio.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mgnstn.traderportfolio.service.MarketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @Autowired SimpMessagingTemplate template;
    @Autowired MarketService marketService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);

    @MessageMapping("/stock-prices")
    public void sendPrices() {
        scheduler.scheduleAtFixedRate(
            () -> {
                template.convertAndSend("/topic/message", marketService.getTickers(null));
            },
            0,
            2,
            TimeUnit.SECONDS
        );
    }
}
