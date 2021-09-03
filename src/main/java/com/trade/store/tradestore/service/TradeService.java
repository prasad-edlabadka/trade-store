package com.trade.store.tradestore.service;

import com.trade.store.tradestore.beans.Trade;

public interface TradeService {
    void addTrade(Trade trade) throws InvalidTradeException;
    Trade[] getTrades();
}
