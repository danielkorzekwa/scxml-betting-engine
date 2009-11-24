package dk.bot.bettingengine.statemachine.context;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dk.bot.marketobserver.model.BetStatus;
import dk.bot.marketobserver.model.BetType;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.RunnerPrice;

public class MarketRunnerCtxHelperTest {

	@Test
	public void testGetMarketProfitTwoRunners() {

		List<MUBet> muBets = new ArrayList<MUBet>();
		MUBet bet1Lay = new MUBet();
		bet1Lay.setSelectionId(1);
		bet1Lay.setSize(2);
		bet1Lay.setPrice(1.5d);
		bet1Lay.setBetStatus(BetStatus.M);
		bet1Lay.setBetType(BetType.L);
		muBets.add(bet1Lay);
		
		MUBet bet2Lay = new MUBet();
		bet2Lay.setSelectionId(2);
		bet2Lay.setSize(2);
		bet2Lay.setPrice(1.5d);
		bet2Lay.setBetStatus(BetStatus.M);
		bet2Lay.setBetType(BetType.L);
		muBets.add(bet2Lay);
		
		assertEquals(1, MarketRunnerCtxHelper.getMarketProfit(muBets, 1), 0);
		assertEquals(1, MarketRunnerCtxHelper.getMarketProfit(muBets, 2), 0);
		
		bet1Lay.setPrice(4);
		bet2Lay.setPrice(3.5);
		assertEquals(-4, MarketRunnerCtxHelper.getMarketProfit(muBets, 1), 0);
		assertEquals(-3, MarketRunnerCtxHelper.getMarketProfit(muBets, 2), 0);
		
	}
	
	@Test
	public void testGetMarketProfitThreeRunners() {
		
		List<MUBet> muBets = new ArrayList<MUBet>();
		MUBet bet1Lay = new MUBet();
		bet1Lay.setSelectionId(1);
		bet1Lay.setSize(2);
		bet1Lay.setPrice(1.5d);
		bet1Lay.setBetStatus(BetStatus.M);
		bet1Lay.setBetType(BetType.L);
		muBets.add(bet1Lay);
		
		MUBet bet2Lay = new MUBet();
		bet2Lay.setSelectionId(2);
		bet2Lay.setSize(2);
		bet2Lay.setPrice(1.5d);
		bet2Lay.setBetStatus(BetStatus.M);
		bet2Lay.setBetType(BetType.L);
		muBets.add(bet2Lay);
		
		MUBet bet3Lay = new MUBet();
		bet3Lay.setSelectionId(3);
		bet3Lay.setSize(2);
		bet3Lay.setPrice(1.5d);
		bet3Lay.setBetStatus(BetStatus.M);
		bet3Lay.setBetType(BetType.L);
		muBets.add(bet3Lay);
		
		assertEquals(3, MarketRunnerCtxHelper.getMarketProfit(muBets, 1), 0);
		assertEquals(3, MarketRunnerCtxHelper.getMarketProfit(muBets, 2), 0);
		assertEquals(3, MarketRunnerCtxHelper.getMarketProfit(muBets, 3), 0);
	}

	@Test
	public void testIsMarketGreen() {
		List<MarketRunner> marketRunners = new ArrayList<MarketRunner>();
		MarketRunner runner1 = new MarketRunner(1,0,0,0,0,0,new ArrayList<RunnerPrice>());
		
		marketRunners.add(runner1);
		
		MarketRunner runner2 = new MarketRunner(2,0,0,0,0,0,new ArrayList<RunnerPrice>());
		marketRunners.add(runner2);
		
		MarketRunner runner3 = new MarketRunner(3,0,0,0,0,0,new ArrayList<RunnerPrice>());
		marketRunners.add(runner3);
		
		List<MUBet> muBets = new ArrayList<MUBet>();
		MUBet bet1Lay = new MUBet();
		bet1Lay.setSelectionId(1);
		bet1Lay.setSize(2);
		bet1Lay.setPrice(1.5d);
		bet1Lay.setBetStatus(BetStatus.M);
		bet1Lay.setBetType(BetType.L);
		muBets.add(bet1Lay);
		
		MUBet bet2Lay = new MUBet();
		bet2Lay.setSelectionId(2);
		bet2Lay.setSize(2);
		bet2Lay.setPrice(1.5d);
		bet2Lay.setBetStatus(BetStatus.M);
		bet2Lay.setBetType(BetType.L);
		muBets.add(bet2Lay);
		
		MUBet bet3Lay = new MUBet();
		bet3Lay.setSelectionId(3);
		bet3Lay.setSize(2);
		bet3Lay.setPrice(1.5d);
		bet3Lay.setBetStatus(BetStatus.M);
		bet3Lay.setBetType(BetType.L);
		muBets.add(bet3Lay);
		
		assertEquals(true, MarketRunnerCtxHelper.isMarketGreen(marketRunners,muBets));
		
		bet1Lay.setPrice(9);
		assertEquals(false, MarketRunnerCtxHelper.isMarketGreen(marketRunners,muBets));
		
		muBets.remove(0);
		assertEquals(true, MarketRunnerCtxHelper.isMarketGreen(marketRunners,muBets));
		
		muBets.clear();
		assertEquals(false, MarketRunnerCtxHelper.isMarketGreen(marketRunners,muBets));
			
	}
	
	@Test
	public void testIsRunnerGreen() {
				
		List<MUBet> muBets = new ArrayList<MUBet>();
		MUBet bet1Lay = new MUBet();
		bet1Lay.setSelectionId(2);
		bet1Lay.setSize(2);
		bet1Lay.setPrice(1.5d);
		bet1Lay.setBetStatus(BetStatus.M);
		bet1Lay.setBetType(BetType.L);
		muBets.add(bet1Lay);
		
		MUBet bet2Back = new MUBet();
		bet2Back.setSelectionId(2);
		bet2Back.setSize(2);
		bet2Back.setPrice(1.6d);
		bet2Back.setBetStatus(BetStatus.M);
		bet2Back.setBetType(BetType.B);
		muBets.add(bet2Back);
			
		assertEquals(true, MarketRunnerCtxHelper.isRunnerGreen(2,muBets));
		
		bet1Lay.setPrice(9);
		assertEquals(false, MarketRunnerCtxHelper.isRunnerGreen(2,muBets));
			
		muBets.clear();
		assertEquals(false, MarketRunnerCtxHelper.isRunnerGreen(2,muBets));	
	}
	
	

}
