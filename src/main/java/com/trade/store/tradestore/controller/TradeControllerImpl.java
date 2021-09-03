package com.trade.store.tradestore.controller;

import com.trade.store.tradestore.beans.Trade;
import com.trade.store.tradestore.service.InvalidTradeException;
import com.trade.store.tradestore.service.TradeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeControllerImpl implements ITradeController {

    @Autowired
    TradeService tradeService;

    @Override
    @PostMapping(path = "trades", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTrade(@RequestBody Trade trade) {
        try {
            tradeService.addTrade(trade);
        } catch (InvalidTradeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = "trades", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Trade[]> getTrades() {
        return new ResponseEntity<>(tradeService.getTrades(), HttpStatus.OK);
    }

}
