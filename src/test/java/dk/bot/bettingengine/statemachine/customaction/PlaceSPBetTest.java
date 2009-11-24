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
import dk.bot.bettingengine.betapi.SPBetPlaceResult;
import dk.bot.bettingengine.statemachine.context.ContextObjectEnum;
import dk.bot.bettingengine.statemachine.context.ContextVariableEnum;
import dk.bot.marketobserver.model.BetType;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.RunnerPrice;

@RunWith(JMock.class)
public class PlaceSPBetTest {

	private PlaceSPBet placeBet = new PlaceSPBet();
	private Mockery mockery = new JUnit4Mockery();
	
	private final BetApi betApi = mockery.mock(BetApi.class);
	
	private final MarketRunner marketRunner = new MarketRunner(2,0,0,0,0,0,new ArrayList<RunnerPrice>());
	
	private final int runnerStateId=99;
		
	private Map<String, Object> ctxParams = new HashMap<String, Object>();
	
	@Before
	public void setUp() {
		ctxParams.put(ContextObjectEnum.BET_API.getName(), betApi);
		ctxParams.put(ContextVariableEnum.RUNNER_STATE_ID.getName(), runnerStateId);
		
		ctxParams.put(ContextObjectEnum.RUNNER.getName(), marketRunner);
	}
	
	@Test
	public void testExecuteSimpleBackLoC() throws  ModelException {

		placeBet.setPrice("2.0");
		placeBet.setLiability("2.5");
		placeBet.setBetType("'B'");

		final SPBetPlaceResult spBetResult = new SPBetPlaceResult(3,new Date(2000),"B",2d,2.5d);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeSPBet(1, 2, BetType.B, 2.5d, 2d);
				will(returnValue(spBetResult));
				
				
			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
		
	}
	@Test
	public void testExecuteSimpleBackMoC() throws  ModelException {

		placeBet.setLiability("2.5");
		placeBet.setBetType("'B'");

		final SPBetPlaceResult spBetResult = new SPBetPlaceResult(3,new Date(2000),"B",0d,2.5d);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeSPBet(1, 2, BetType.B, 2.5d, null);
				will(returnValue(spBetResult));
				
			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
	}
	
	@Test
	public void testExecuteSimpleBackLiabilityLessThan2() throws ModelException {

		placeBet.setPrice("2.5");
		placeBet.setLiability("1.5");
		placeBet.setBetType("'B'");

		final SPBetPlaceResult spBetResult = new SPBetPlaceResult(3,new Date(2000),"B",2d,2.5d);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeSPBet(1, 2, BetType.B, 2d, 2.5d);
				will(returnValue(spBetResult));
				
			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
	}

	@Test
	public void testExecuteSimpleLayLoC() throws ModelException {

		placeBet.setPrice("2.0");
		placeBet.setLiability("2.5");
		placeBet.setBetType("'L'");

		final SPBetPlaceResult spBetResult = new SPBetPlaceResult(3,new Date(2000),"B",2d,2.5d);
		mockery.checking(new Expectations() {
			{
				one(betApi).placeSPBet(1, 2, BetType.L, 2.5d, 2d);
				will(returnValue(spBetResult));
				
			}
		});

		ActionTester.executeAction(placeBet, ctxParams);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteLiabilityNotSet() throws ModelException {
		
		placeBet.setPrice("3.5");
		placeBet.setBetType("B");

		ActionTester.executeAction(placeBet, ctxParams);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteBetTypeNotSet() throws ModelException {
		
		placeBet.setLiability("2.5");
		placeBet.setPrice("3.5");

		ActionTester.executeAction(placeBet, ctxParams);
	}

}
