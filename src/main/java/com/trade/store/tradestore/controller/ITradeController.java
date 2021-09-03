package com.trade.store.tradestore.controller;

import com.trade.store.tradestore.beans.Trade;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ITradeController {
    ResponseEntity<String> addTrade(@RequestBody Trade trade);
    ResponseEntity<Trade[]> getTrades();
}
