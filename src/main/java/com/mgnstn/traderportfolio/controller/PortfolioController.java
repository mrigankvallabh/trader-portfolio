package com.mgnstn.traderportfolio.controller;

import com.mgnstn.traderportfolio.model.Portfolio;
import com.mgnstn.traderportfolio.service.PortfolioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortfolioController {

    @Autowired PortfolioService portfolioService;
    
    @GetMapping("/portfolio")
    @CrossOrigin(origins = "*")
    public Portfolio getPorfolio() {
        return portfolioService.getPortfolio();
    }
}
