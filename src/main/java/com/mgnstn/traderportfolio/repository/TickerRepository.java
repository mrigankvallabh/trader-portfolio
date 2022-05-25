package com.mgnstn.traderportfolio.repository;

import com.mgnstn.traderportfolio.model.Ticker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TickerRepository extends JpaRepository<Ticker, String> { }
