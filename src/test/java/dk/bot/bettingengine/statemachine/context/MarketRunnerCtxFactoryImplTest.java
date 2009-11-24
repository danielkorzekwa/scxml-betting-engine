package dk.bot.bettingengine.statemachine.context;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketDetailsRunner;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.MarketRunners;
import dk.bot.marketobserver.model.RunnerPrice;
import dk.bot.marketobserver.tasks.bwin.BetFairBwinRegionEnum;
import dk.bot.marketobserver.tasks.bwin.BwinMarketPrices;
import dk.bot.oddschecker.model.HorseWinMarket;

@RunWith(JMock.class)
public class MarketRunnerCtxFactoryImplTest {

	private Mockery mockery = new JUnit4Mockery();
	
	private final BettingEngineDAO bettingEngineDAO = mockery.mock(BettingEngineDAO.class);
	private final BetApi betApi = mockery.mock(BetApi.class);
	
	
	@Test
	public void testCreateContext() {
		mockery.checking(new Expectations() {
			{
				one(bettingEngineDAO).findLastBet(1, "B");
				one(bettingEngineDAO).findLastBet(1, "L");
			}
		});
		
		HashMap<Integer, Double> bwinPricesMap = new HashMap<Integer, Double>();
		bwinPricesMap.put(new Integer(0), 2d);
		BwinMarketPrices bwinMarketPrices = new BwinMarketPrices(1,bwinPricesMap);

		MarketData market = new MarketData();
		market.setMarketName("To Be Placed");
		market.setSuspendTime(new Date(0));
		market.setRunners(new ArrayList<MarketDetailsRunner>());
		Market compositeMarket = new Market(market,	BetFairBwinRegionEnum.SOCCER_ARGENTINA, bwinMarketPrices,new HorseWinMarket());
		
		MarketRunners marketRunners = new MarketRunners(1,new ArrayList<MarketRunner>(),5,new Date(0));
		MarketRunner marketRunner = new MarketRunner(2,0,0,0,0,0,new ArrayList<RunnerPrice>());
		marketRunners.getMarketRunners().add(marketRunner);
		
		compositeMarket.setMarketRunners(marketRunners);
		compositeMarket.setMarketMUBets(new ArrayList<MUBet>());
		
		Map<String, Object> context = new MarketRunnerCtxFactoryImpl().createContext(1,compositeMarket, marketRunner,new Prediction(),
				betApi, bettingEngineDAO,0);

		for (ContextObjectEnum ctxEnum : ContextObjectEnum.values()) {
			assertNotNull("No context found for: " + ctxEnum.getName(), context.get(ctxEnum.getName()));
		}

		for (ContextVariableEnum ctxEnum : ContextVariableEnum.values()) {
			assertNotNull("No context found for: " + ctxEnum.getName(), context.get(ctxEnum.getName()));
		}
	}

	@Test
	public void testCreateRandomContext() {
		Map<String, Object> context = new MarketRunnerCtxFactoryImpl()
				.createRandomContext(new Random(DateTimeUtils.currentTimeMillis()));

		for (ContextObjectEnum ctxEnum : ContextObjectEnum.values()) {
			assertNotNull("No context found for: " + ctxEnum.getName(), context.get(ctxEnum.getName()));
		}

		for (ContextVariableEnum ctxEnum : ContextVariableEnum.values()) {
			assertNotNull("No context found for: " + ctxEnum.getName(), context.get(ctxEnum.getName()));
		}
	}

}
