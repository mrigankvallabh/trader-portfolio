package com.mgnstn.traderportfolio.controller;

import java.io.BufferedWriter;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;

import com.mgnstn.traderportfolio.model.Portfolio;
import com.mgnstn.traderportfolio.model.TickerHolding;
import com.mgnstn.traderportfolio.service.PortfolioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortfolioController {

    @Autowired PortfolioService portfolioService;
    
    @GetMapping("/portfolio")
    @CrossOrigin(origins = "*")
    public Portfolio getPorfolio(@RequestParam(value = "toFile",required = false, defaultValue = "") String toFile) {
        Portfolio portfolio = portfolioService.getPortfolio();
        if(!(toFile.isBlank() || toFile.isEmpty()))
            writeToFile(portfolio);;
        
        return portfolio;
    }

    private void writeToFile(Portfolio portfolio) {
        Path path = Path.of("files/navReport.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("TickerId,NoOfContracts,Value");
            writer.newLine();
            for (TickerHolding t : portfolio.getHoldings()) {
                writer.write(t.getTickerId() + "," + t.getNoOfContracts() + "," + t.getValue().setScale(4, RoundingMode.HALF_UP));;
                writer.newLine();
            }
            writer.write("TotalValue," + portfolio.getTotalValue().setScale(4, RoundingMode.HALF_UP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
