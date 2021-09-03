package com.trade.store.tradestore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import com.trade.store.tradestore.beans.Trade;
import com.trade.store.tradestore.beans.Trades;
import com.trade.store.tradestore.service.InvalidTradeException;
import com.trade.store.tradestore.service.TradeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TradeStoreApplicationTests {

	@Autowired
	TradeService service;

	@Test
	void shouldAddValidTrade() {
		int currentTrades = service.getTrades().length;
		Trade trade = new Trade();
		trade.setTradeId("T1");
		trade.setVersion("1");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("B1");
		trade.setMaturityDate(LocalDate.now().toString());
		trade.setCreatedDate(LocalDate.now().toString());
		trade.setExpired("N");
		try {
			service.addTrade(trade);
			assertEquals(currentTrades + 1, service.getTrades().length);
		} catch (InvalidTradeException e) {
			fail(e.getMessage());
		}
	}

	@Test
	void shouldRejectPreviousVersion() {
		Trade trade = new Trade();
		trade.setTradeId("T2");
		trade.setVersion("1");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("B1");
		trade.setMaturityDate(LocalDate.now().toString());
		trade.setCreatedDate(LocalDate.now().toString());
		trade.setExpired("N");
		try {
			service.addTrade(trade);
		} catch (InvalidTradeException e) {
			fail();
		}

		trade = new Trade();
		trade.setTradeId("T2");
		trade.setVersion("0");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("B1");
		trade.setMaturityDate(LocalDate.now().toString());
		trade.setCreatedDate(LocalDate.now().toString());
		trade.setExpired("N");
		int currentTrades = service.getTrades().length;
		try {
			service.addTrade(trade);
			fail();
		} catch (InvalidTradeException e) {
			assertEquals(currentTrades, service.getTrades().length);
		}
	}

	@Test
	void shouldOverwriteSameVersion() {
		Trade trade = new Trade();
		trade.setTradeId("T3");
		trade.setVersion("1");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("B1");
		trade.setMaturityDate(LocalDate.now().toString());
		trade.setCreatedDate(LocalDate.now().toString());
		trade.setExpired("N");
		try {
			service.addTrade(trade);
		} catch (InvalidTradeException e) {
			fail();
		}

		trade = new Trade();
		trade.setTradeId("T3");
		trade.setVersion("1");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("B1");
		trade.setMaturityDate(LocalDate.now().toString());
		trade.setCreatedDate(LocalDate.now().toString());
		trade.setExpired("N");
		int currentTrades = service.getTrades().length;
		try {
			service.addTrade(trade);
			assertEquals(currentTrades, service.getTrades().length);
		} catch (InvalidTradeException e) {
			fail();
		}
	}

	@Test
	void shouldRejectOldMaturityDate() {
		Trade trade = new Trade();
		trade.setTradeId("T4");
		trade.setVersion("1");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("B1");
		trade.setMaturityDate(LocalDate.now().minusDays(1).toString());
		trade.setCreatedDate(LocalDate.now().toString());
		trade.setExpired("N");
		int currentTrades = service.getTrades().length;
		try {
			service.addTrade(trade);
			fail();
		} catch (InvalidTradeException e) {
			assertEquals(currentTrades, service.getTrades().length, e.getMessage());
		}
	}

	@Test
	void shouldUpdateExpiredFlag() {
		Trade trade = new Trade();
		trade.setTradeId("T4");
		trade.setVersion("1");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("B1");
		trade.setMaturityDate(LocalDate.now().toString());
		trade.setCreatedDate(LocalDate.now().toString());
		trade.setExpired("N");
		try {
			service.addTrade(trade);
		} catch (InvalidTradeException e) {
			fail();
		}
		Trades.Instance().getTrades()[0].setMaturityDate(LocalDate.now().minusDays(1).toString());
		assertEquals("Y", service.getTrades()[0].getExpired());
	}

}
