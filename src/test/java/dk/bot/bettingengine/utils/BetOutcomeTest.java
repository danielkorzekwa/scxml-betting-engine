package dk.bot.bettingengine.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.bot.marketobserver.model.BetType;

public class BetOutcomeTest {

	@Test
	public void testCalculateProfitBackWin() {
		assertEquals(4,BetOutcome.calculateProfit(BetType.B, 2, 3, true),0);
	}
	
	@Test
	public void testCalculateProfitBackLose() {
		assertEquals(-2,BetOutcome.calculateProfit(BetType.B, 2, 3, false),0);
	}
	
	@Test
	public void testCalculateProfitLayWin() {
		assertEquals(-4,BetOutcome.calculateProfit(BetType.L, 2, 3, true),0);
	}
	
	@Test
	public void testCalculateProfitLaylose() {
		assertEquals(2,BetOutcome.calculateProfit(BetType.L, 2, 3, false),0);
	}

}
