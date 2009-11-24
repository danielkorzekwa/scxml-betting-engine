package dk.bot.bettingengine.statemachine.manager;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.scxml.model.CustomAction;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.bettingengine.statemachine.context.MarketRunnerCtxFactory;
import dk.bot.bettingengine.statemachine.executor.StateMachineExecutor;
import dk.bot.bettingengine.statemachine.manager.runnerstatecache.RunnerStateCache;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketDetailsRunner;
import dk.bot.marketobserver.model.MarketFilter;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.MarketRunners;
import dk.bot.marketobserver.model.RunnerPrice;
import dk.bot.marketobserver.tasks.bwin.BwinMarketPrices;

@RunWith(JMock.class)
public class StateMachineManagerImplTest {

	private Mockery mockery = new JUnit4Mockery();

	private StateMachineManagerImpl stateMachineManager;
	private URL stateMachineUrl;
	private List<MarketFilter> marketFilters = new ArrayList<MarketFilter>();
	
	private final RunnerStateCache runnerStateCache = mockery.mock(RunnerStateCache.class);
	private final MockMarketRunnerCtxFactory marketRunnerCtxFactory = new MockMarketRunnerCtxFactory();
	
	private Market completeMarket;
	private MarketRunner marketRunner;
	
	@Before
	public void setUp() throws MalformedURLException {
		
		stateMachineUrl = new File("src/test/resources/conf/statemachine/testmachine.scxml").toURI().toURL();
		stateMachineManager = new StateMachineManagerImpl("machineId",marketFilters,runnerStateCache,
				null,null,marketRunnerCtxFactory);
		
		completeMarket = createCompleteMarket(null);
		
		marketRunner = new MarketRunner(1,0,0,0,0,0,new ArrayList<RunnerPrice>());
	}

	private Market createCompleteMarket(BwinMarketPrices bwinMarketPrices) {
		MarketData marketData = new MarketData();
		marketData.setMarketId(10);
		marketData.setSuspendTime(new Date());
		marketData.setEventDate(new Date(0));
		marketData.setRunners(new ArrayList<MarketDetailsRunner>());
		Market market = new Market(marketData,null,bwinMarketPrices,null);
		market.setMarketRunners(new MarketRunners(10, new ArrayList<MarketRunner>(),0,new Date(0)));
		return market;
	}

	@Test
	public void testFireEvent() {
		final Sequence sequence = mockery.sequence("sequence");

		final RunnerStateMachine runnerStateMachine = new RunnerStateMachine(123,new StateMachineExecutor(stateMachineUrl, new ArrayList<CustomAction>()));
		
		mockery.checking(new Expectations() {
			{
				exactly(1).of(runnerStateCache).get(new StateMachineKey(10, 1, new Date(0)));
				will(returnValue(runnerStateMachine));inSequence(sequence);
			}
		});
		completeMarket.getMarketData().setEventHierarchy("/3/3/3/4/5/6/7");
		completeMarket.getMarketData().setMarketName("Match Odds");
		
		MarketFilter filter = new MarketFilter("rugby","/3/3/3","Match Odds",false,false);
		marketFilters.add(filter);
		
		ArrayList<Integer> bets = new ArrayList<Integer>();
		bets.add(1);
		marketRunnerCtxFactory.getCtx().put("runnerBets.mBets", 1);
		
		stateMachineManager.fireEvent(completeMarket, marketRunner,new Prediction());
		
		assertEquals("layMatched",runnerStateMachine.getStateMachine().getCurrentStateId());
	}
	
	/**Event is omitted, because of market filter is not matched - not invocations expected*/
	@Test
	public void testFireEventMarketFilterEventPathNotMatched() {
		
		completeMarket.getMarketData().setEventHierarchy("/1/31");
		completeMarket.getMarketData().setMarketName("Match Odds");
		
		MarketFilter filter = new MarketFilter("rugby","/3/3/3","unknown market name",false,false);
		marketFilters.add(filter);
		
		stateMachineManager.fireEvent(completeMarket, marketRunner,new Prediction());
	}
	
	/**Event is omitted, because of market filter is not matched - not invocations expected*/
	@Test
	public void testFireEventMarketFilterEventPathMatchedMarketNotMatched() {
		
		completeMarket.getMarketData().setEventHierarchy("/3/3/3/4/5/6/7");
		completeMarket.getMarketData().setMarketName("Match Odds");
		
		MarketFilter filter = new MarketFilter("rugby","/3/3/3","unknown market name",false,false);
		marketFilters.add(filter);
		
		stateMachineManager.fireEvent(completeMarket, marketRunner,new Prediction());
	}
	
	/**Event is processed, because of market filter is matched*/
	@Test
	public void testFireEventMarketFilterEventPathMatchedMarketNameIsNull() {
		
		final Sequence sequence = mockery.sequence("sequence");

		final RunnerStateMachine runnerStateMachine = new RunnerStateMachine(123,new StateMachineExecutor(stateMachineUrl, new ArrayList<CustomAction>()));
		
		mockery.checking(new Expectations() {
			{
				exactly(1).of(runnerStateCache).get(new StateMachineKey(10, 1, new Date(0)));
				will(returnValue(runnerStateMachine));inSequence(sequence);
			}
		});
		
		completeMarket.getMarketData().setEventHierarchy("/3/3/3/4/5/6/7");
		completeMarket.getMarketData().setMarketName("Match Odds");
		
		MarketFilter filter = new MarketFilter("rugby","/3/3/3",null,false,false);
		marketFilters.add(filter);
		
		stateMachineManager.fireEvent(completeMarket, marketRunner,new Prediction());
	}
	
	/**Event is processed, because of market filter is matched*/
	@Test
	public void testFireEventMarketFilterEventPathMatchedMarketNameIsMatched() {
		
		final Sequence sequence = mockery.sequence("sequence");

		final RunnerStateMachine runnerStateMachine = new RunnerStateMachine(123,new StateMachineExecutor(stateMachineUrl, new ArrayList<CustomAction>()));
		
		mockery.checking(new Expectations() {
			{
				exactly(1).of(runnerStateCache).get(new StateMachineKey(10, 1, new Date(0)));
				will(returnValue(runnerStateMachine));inSequence(sequence);
			}
		});
		
		completeMarket.getMarketData().setEventHierarchy("/3/3/3/4/5/6/7");
		completeMarket.getMarketData().setMarketName("Match Odds");
		
		MarketFilter filter = new MarketFilter("rugby","/3/3/3","Match Odds",false,false);
		marketFilters.add(filter);
		
		stateMachineManager.fireEvent(completeMarket, marketRunner,new Prediction());
	}
	
	/**Event is processed, because of market filter is matched*/
	@Test
	public void testFireEventMarketFilterBwinMatched() {
		
		final Sequence sequence = mockery.sequence("sequence");

		final RunnerStateMachine runnerStateMachine = new RunnerStateMachine(123,new StateMachineExecutor(stateMachineUrl, new ArrayList<CustomAction>()));
		
		mockery.checking(new Expectations() {
			{
				exactly(1).of(runnerStateCache).get(new StateMachineKey(10, 1, new Date(0)));
				will(returnValue(runnerStateMachine));inSequence(sequence);
			}
		});
		
		completeMarket = createCompleteMarket(new BwinMarketPrices(1,new HashMap<Integer, Double>()));
		completeMarket.getMarketData().setEventHierarchy("/3/3/3/4/5/6/7");
		completeMarket.getMarketData().setMarketName("Match Odds");
		
		MarketFilter filter = new MarketFilter("rugby","/3/3/3","Match Odds",true,false);
		marketFilters.add(filter);
		
		stateMachineManager.fireEvent(completeMarket, marketRunner,new Prediction());
	}
	
	/**Event is not processed - no market filters.*/
	@Test
	public void testFireEventMarketFiltersAreEmpty() {
		
		completeMarket.getMarketData().setEventHierarchy("/3/3/3/4/5/6/7");
		completeMarket.getMarketData().setMarketName("Match Odds");
		
		marketFilters.clear();
		
		stateMachineManager.fireEvent(completeMarket, marketRunner,new Prediction());
	}


	@Test
	public void testRemoveMachines() throws MalformedURLException {

		final Date marketTime = new Date(200);
		mockery.checking(new Expectations() {
			{
				one(runnerStateCache).removeMachines(marketTime);
				will(returnValue(4));
				
			}
		});

		assertEquals(4, stateMachineManager.removeMachines(marketTime));
	}
	
	private class MockMarketRunnerCtxFactory implements MarketRunnerCtxFactory {

		private Map<String, Object> ctx = new HashMap<String, Object>();

		public Map<String, Object> createContext(int runnerStateId,Market compositeMarket, MarketRunner runner,Prediction prediction,
				 BetApi betApi, BettingEngineDAO bettingEngineDao,
				long now) {
			return ctx;
		}

		public Map<String, Object> createRandomContext(Random random) {
			// TODO Auto-generated method stub
			return null;
		}
		
		public Map<String,Object> getCtx() {
			return ctx;
			
		}
		
	}

}
