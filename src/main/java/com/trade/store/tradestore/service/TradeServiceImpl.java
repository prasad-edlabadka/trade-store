package com.trade.store.tradestore.service;

import java.time.LocalDate;

import com.trade.store.tradestore.beans.Trade;
import com.trade.store.tradestore.beans.Trades;

import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl implements TradeService {

    @Override
    public void addTrade(Trade trade) throws InvalidTradeException {
        // Check trade date if not in the past
        LocalDate dt = LocalDate.parse(trade.getMaturityDate());
        boolean overwriteTrade = false;
        int overwriteIndex = -1;
        if (LocalDate.now().isAfter(dt)) {
            throw new InvalidTradeException("Trade Maturity Date cannot be in past.");
        }
        Trades tradeInstane = Trades.Instance();
        Trade[] trades = tradeInstane.getTrades();
        for (int i = 0; i < trades.length; i++) {
            // Check if trade id exists.
            if (trade.getTradeId().equals(trades[i].getTradeId())) {
                // Found the trade. Validate version
                if (Integer.parseInt(trade.getVersion()) < Integer.parseInt(trades[i].getVersion())) {
                    // Found older version;
                    throw new InvalidTradeException("Trade Version value is less than available.");
                } else if (Integer.parseInt(trade.getVersion()) == Integer.parseInt(trades[i].getVersion())) {
                    // Found same version overwrite;
                    overwriteTrade = true;
                    overwriteIndex = i;
                }
            }
            // Check if
        }

        if (overwriteTrade) {
            trades[overwriteIndex] = trade;
        } else {
            tradeInstane.addTrade(trade);
        }
        updateFlag(Trades.Instance().getTrades());
        return;
    }

    @Override
    public Trade[] getTrades() {
        System.out.println("Returning Trades...");
        updateFlag(Trades.Instance().getTrades());
        return Trades.Instance().getTrades();
    }

    private void updateFlag(Trade[] trades) {
        for (int i = 0; i < trades.length; i++) {
            LocalDate dt = LocalDate.parse(trades[i].getMaturityDate());
            if (LocalDate.now().isAfter(dt)) {
                trades[i].setExpired("Y");
            }
        }
    }

}
