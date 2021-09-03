package com.trade.store.tradestore.beans;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trades {

    private ArrayList<Trade> trades;

    private static Trades _instance;

    private Trades() {
        trades = new ArrayList<Trade>();
    }

    public static Trades Instance() {
        if(_instance == null){
            System.out.println("Creating new instance...");
            _instance = new Trades();
        }
        return _instance;
    }

    public void addTrade(Trade trade) {
        trades.add(trade);
    }

    public Trade[] getTrades() {
        return trades.toArray(new Trade[0]);
    }
}
