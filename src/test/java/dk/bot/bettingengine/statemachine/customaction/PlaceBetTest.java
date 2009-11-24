package dk.bot.bettingengine.statemachine.customaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.scxml.model.ModelException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.betapi.BetPlaceResult;
import dk.bot.bettingengine.statemachine.context.ContextObjectEnum;
import dk.bot.bettingengine.statemachine.context.ContextVariableEnum;
import dk.bot.marketobserver.model.BetType;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.RunnerPrice;

@RunWith(JMock.class)
public class PlaceBetTest {

	private PlaceBet placeBet = new PlaceBet();
	
	private Mockery mockery = new JUnit4Mockery();
	
	final BetApi betApi = mockery.mock(BetApi.class);
	
	private final MarketRunner marketRunner = new MarketRunner(2,0,0,0,0,0,new ArrayList<RunnerPrice>());
	
	private final int runnerStateId=99;
	
	private Map<String, Object> ctxParams = new HashMap<String, Object>();
	
	@Before
	public void setUp() {
		ctxParams.put(ContextObjectEnum.BET_API.getName(), betApi);
				
		ctxParams.put(ContextObjectEnum.RUNNER.getName(), marketRunner);	
		ctxParams.put(ContextVariableEnum.RUNNER_STATE_ID.getName(), runnerStateId);
	}
	
	@Test
	public void testExecuteSimpleBack() throws ModelException {
	
		placeBet.setPrice("2.0");
		placeBet.setSize("2.5");
		placeBet.setBetType("'B'");
		
		final BetPlaceResult betPlaceResult = new BetPlaceResult(1234, new Date(1000), "B", 2, 20, 5, 7);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeBet(1, 2, BetType.B, 2.0d, 2.5d,true);
				will(returnValue(betPlaceResult));		
			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
	}
	
	@Test
	public void testExecuteSimpleBackSizeLessThan2() throws  ModelException {
		
		placeBet.setPrice("2.0");
		placeBet.setSize("1.5");
		placeBet.setBetType("'B'");

		final BetPlaceResult betPlaceResult = new BetPlaceResult(1234, new Date(1000), "B", 2, 20, 5, 7);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeBet(1, 2, BetType.B, 2.0d, 2d,true);
				will(returnValue(betPlaceResult));
			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
	}

	@Test
	public void testExecuteSimpleLay() throws ModelException {
	
		placeBet.setPrice("2.0");
		placeBet.setSize("2.5");
		placeBet.setBetType("'L'");

		final BetPlaceResult betPlaceResult = new BetPlaceResult(1234, new Date(1000), "B", 2, 20, 5, 7);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeBet(1, 2, BetType.L, 2.0d, 2.5d,true);
				will(returnValue(betPlaceResult));
			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
		
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecutePriceNotSet() throws  ModelException {
		
		placeBet.setSize("2.5");
		placeBet.setBetType("B");

		Map<String, Object> ctxParams = new HashMap<String, Object>();
		ActionTester.executeAction(placeBet, ctxParams);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteSizeNotSet() throws ModelException {
		
		placeBet.setPrice("3.5");
		placeBet.setBetType("B");

		Map<String, Object> ctxParams = new HashMap<String, Object>();
		ActionTester.executeAction(placeBet, ctxParams);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteBetTypeNotSet() throws ModelException {
		
		placeBet.setSize("1.5");
		placeBet.setPrice("3.5");

		Map<String, Object> ctxParams = new HashMap<String, Object>();
		ActionTester.executeAction(placeBet, ctxParams);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteMinPriceBiggerThanMaxPriceException() throws ModelException {
		
		placeBet.setPrice("2.0");
		placeBet.setSize("2.5");
		placeBet.setMinPrice("3");
		placeBet.setMaxPrice("2");

		Map<String, Object> ctxParams = new HashMap<String, Object>();
		ActionTester.executeAction(placeBet, ctxParams);
	}

	@Test
	public void testExecuteMinPriceInUse() throws  ModelException {
	
		placeBet.setPrice("1.5");
		placeBet.setSize("2.5");
		placeBet.setMinPrice("3.0");
		placeBet.setMaxPrice("5.0");
		placeBet.setBetType("'L'");

	final BetPlaceResult betPlaceResult = new BetPlaceResult(1234, new Date(1000), "B", 3, 20, 5, 7);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeBet(1, 2, BetType.L, 3.0d, 2.5d,true);
				will(returnValue(betPlaceResult));

			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
	}

	@Test
	public void testExecuteMaxPriceInUse() throws ModelException {
		placeBet.setPrice("5.1");
		placeBet.setSize("2.5");
		placeBet.setMinPrice("3.0");
		placeBet.setMaxPrice("5.0");
		placeBet.setBetType("'L'");

		final BetPlaceResult betPlaceResult = new BetPlaceResult(1234, new Date(1000), "B", 5, 20, 5, 7);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeBet(1, 2, BetType.L, 5.0d, 2.5d,true);
				will(returnValue(betPlaceResult));
			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
	}
	
	@Test
	public void testExecuteSimpleBackCheckTxCounterIsFalse() throws  ModelException {
	
		placeBet.setPrice("2.0");
		placeBet.setSize("2.5");
		placeBet.setBetType("'B'");
		placeBet.setCheckTxCounter("false");
		
		final BetPlaceResult betPlaceResult = new BetPlaceResult(1234, new Date(1000), "B", 2, 20, 5, 7);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeBet(1, 2, BetType.B, 2.0d, 2.5d,false);
				will(returnValue(betPlaceResult));
			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
	}
	
	@Test
	public void testExecuteSimpleBackCheckTxCounterIsTrue() throws  ModelException {
	
		placeBet.setPrice("2.0");
		placeBet.setSize("2.5");
		placeBet.setBetType("'B'");
		placeBet.setCheckTxCounter("true");
		
		final BetPlaceResult betPlaceResult = new BetPlaceResult(1234, new Date(1000), "B", 2, 20, 5, 7);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeBet(1, 2, BetType.B, 2.0d, 2.5d,true);
				will(returnValue(betPlaceResult));
			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
	}


}
