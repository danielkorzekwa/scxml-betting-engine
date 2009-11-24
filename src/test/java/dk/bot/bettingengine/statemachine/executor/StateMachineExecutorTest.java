package dk.bot.bettingengine.statemachine.executor;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.scxml.model.CustomAction;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.DateTimeUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.bettingengine.statemachine.context.MarketRunnerCtxFactoryImpl;
import dk.bot.bettingengine.statemachine.executor.mock.MockBackLMBets;
import dk.bot.bettingengine.statemachine.executor.mock.MockBwinMarketPrices;
import dk.bot.bettingengine.statemachine.executor.mock.MockCancelBets;
import dk.bot.bettingengine.statemachine.executor.mock.MockMarketRunner;
import dk.bot.bettingengine.statemachine.executor.mock.MockPlaceBet;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketDetailsRunner;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.MarketRunners;
import dk.bot.marketobserver.model.RunnerPrice;
import dk.bot.marketobserver.tasks.bwin.BetFairBwinRegionEnum;
import dk.bot.oddschecker.model.HorseWinMarket;

public class StateMachineExecutorTest {

	private static MarketData market;
	
	private static MockMarketRunner runner;
	private static MockBwinMarketPrices bwinMarketPrices;
	private static RunnerBet runnerBetLay;
	private static RunnerBet runnerBetBack;
		
	private static Map<String, Object> ctx;
	
	private static StateMachineExecutor machine;

	@BeforeClass
	public static void setUp() throws MalformedURLException {
		market = new MarketData();
		market.setSuspendTime(new Date(0));
		market.setRunners(new ArrayList<MarketDetailsRunner>());
		
		runner = new MockMarketRunner(2,0,0,0,0,0,new ArrayList<RunnerPrice>());
		
		
		MarketRunners marketRunners = new MarketRunners(1,new ArrayList<MarketRunner>(),5,new Date(0));
		marketRunners.getMarketRunners().add(runner);
		
		bwinMarketPrices = new MockBwinMarketPrices(1,new HashMap<Integer, Double>());
			
		Market compositeMarket = new Market(market,BetFairBwinRegionEnum.SOCCER_ARGENTINA,bwinMarketPrices, new HorseWinMarket());
		compositeMarket.setMarketRunners(marketRunners);
		compositeMarket.setMarketMUBets(new ArrayList<MUBet>());
		
		Mockery mockery = new Mockery();
		final BettingEngineDAO bettingEngineDao = mockery.mock(BettingEngineDAO.class); 
		mockery.checking(new Expectations() {
			{
			allowing(bettingEngineDao).findLastBet(with(any(Integer.class)), with(any(String.class)));
			}
		});
		ctx = new MarketRunnerCtxFactoryImpl().createContext(1,compositeMarket, runner, new Prediction(),null,bettingEngineDao,DateTimeUtils.currentTimeMillis());

		List<CustomAction> actions = new ArrayList<CustomAction>();

		CustomAction placeBet = new CustomAction("http://my.custom-actions/CUSTOM", "placeBet", MockPlaceBet.class);
		CustomAction cancelBets = new CustomAction("http://my.custom-actions/CUSTOM", "cancelBets",
				MockCancelBets.class);
		CustomAction backLMBets = new CustomAction("http://my.custom-actions/CUSTOM", "backLMBets",
				MockBackLMBets.class);

		actions.add(placeBet);
		actions.add(cancelBets);
		actions.add(backLMBets);

		machine = new StateMachineExecutor(new File("src/test/resources/conf/statemachine/testmachine.scxml").toURI().toURL(), actions);
	}

	@Test
	public void testExecuteMachine() throws MalformedURLException {
//TODO create junit tests for StateMachine
//		assertEquals("noBets", machine.getCurrentStateId());
//
//		machine.fireEvent("runner", ctx);
//		assertEquals("noBets", machine.getCurrentStateId());
//		assertEquals(0d, MockPlaceBet.getPriceValue(), 0);
//
//		runner.bestPriceToBack = 2d;
//		bwinMarketPrices.odd = 2.1d;
//		MarketAllData marketAllData = new MarketAllData(market,marketDetails, new ArrayList<MarketRunner>(),new ArrayList<MUBet>(),bwinMarketPrices);
//		ctx = MarketRunnerCtxFactory.createContext(marketAllData, runner,runnerBetLay,runnerBetBack, null,System.currentTimeMillis());
//		machine.fireEvent("runner", ctx);
//		assertEquals("layPlaced", machine.getCurrentStateId());
//		assertEquals(2d, MockPlaceBet.getPriceValue(), 0);
//		assertEquals(false, MockCancelBets.executed);
//
//		runnerBets.muBets.add(new MUBet());
//		runnerBets.mBets.clear();
//		runnerBets.uBets.add(new MUBet());
//		marketAllData = new MarketAllData(market,marketDetails, new ArrayList<MarketRunner>(),new ArrayList<MUBet>(),bwinMarketPrices);
//		ctx = MarketRunnerCtxFactory.createContext(marketAllData, runner, runnerBetLay,runnerBetBack, null,System.currentTimeMillis());machine.fireEvent("runner", ctx);
//		assertEquals("layPlaced", machine.getCurrentStateId());
//		assertEquals(true, MockCancelBets.executed);
//
//		runnerBets.muBets.clear();
//		runnerBets.mBets.clear();
//		runnerBets.uBets.clear();
//		marketAllData = new MarketAllData(market,marketDetails, new ArrayList<MarketRunner>(),new ArrayList<MUBet>(),bwinMarketPrices);
//		ctx = MarketRunnerCtxFactory.createContext(marketAllData, runner, runnerBetLay,runnerBetBack, runnerBets, null,System.currentTimeMillis());machine.fireEvent("runner", ctx);
//		assertEquals("noBets", machine.getCurrentStateId());
//
//		runnerBets.muBets.clear();
//		runnerBets.mBets.clear();
//		runnerBets.uBets.clear();
//		marketAllData = new MarketAllData(market,marketDetails, new ArrayList<MarketRunner>(),new ArrayList<MUBet>(),bwinMarketPrices);
//		ctx = MarketRunnerCtxFactory.createContext(marketAllData, runner, runnerBetLay,runnerBetBack,runnerBets, null,System.currentTimeMillis());machine.fireEvent("runner", ctx);
//		assertEquals("layPlaced", machine.getCurrentStateId());
//
//		runnerBets.muBets.add(new MUBet());
//		runnerBets.mBets.add(new MUBet());
//		runnerBets.uBets.clear();
//		marketAllData = new MarketAllData(market,marketDetails, new ArrayList<MarketRunner>(),new ArrayList<MUBet>(),bwinMarketPrices);
//		ctx = MarketRunnerCtxFactory.createContext(marketAllData, runner, runnerBetLay,runnerBetBack,runnerBets, null,System.currentTimeMillis());machine.fireEvent("runner", ctx);
//		assertEquals("layMatched", machine.getCurrentStateId());
//		assertEquals(false, MockBackLMBets.executed);
//
//		GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
//		cal.setTimeInMillis(6000000l);
//		marketDetails = new MarketDetails(0,"",null,cal.getTime());
//		marketAllData = new MarketAllData(market,marketDetails, new ArrayList<MarketRunner>(),new ArrayList<MUBet>(),bwinMarketPrices);
//		ctx = MarketRunnerCtxFactory.createContext(marketAllData, runner, runnerBetLay,runnerBetBack,runnerBets, null,2000000l);
//		machine.fireEvent("runner", ctx);
//		assertEquals("backPlaced", machine.getCurrentStateId());
//		assertEquals(true, MockBackLMBets.executed);
//		assertEquals(6000000, MockBackLMBets.marketSuspendTime);
//
//		machine.fireEvent("runner", ctx);
//		assertEquals("backPlaced", machine.getCurrentStateId());

	}
}
