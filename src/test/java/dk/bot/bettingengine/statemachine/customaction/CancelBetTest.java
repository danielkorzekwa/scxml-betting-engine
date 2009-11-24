package dk.bot.bettingengine.statemachine.customaction;

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
import dk.bot.bettingengine.betapi.BetCancelResult;
import dk.bot.bettingengine.betapi.BetCancelResultEnum;
import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.statemachine.context.ContextObjectEnum;
import dk.bot.bettingengine.statemachine.context.ContextVariableEnum;

@RunWith(JMock.class)
public class CancelBetTest {

	private Mockery mockery = new JUnit4Mockery();
	private final BetApi betApi = mockery.mock(BetApi.class);
	private final BettingEngineDAO bettingEngineDao = mockery.mock(BettingEngineDAO.class);
	private final Map<String, Object> ctxParams = new HashMap<String, Object>();
	private final int runnerStateId=99;
	
	@Before
	public void setUp() {
		ctxParams.put(ContextObjectEnum.BET_API.getName(), betApi);
		ctxParams.put(ContextObjectEnum.BETTING_ENGINE_DAO.getName(), bettingEngineDao);
		ctxParams.put(ContextVariableEnum.RUNNER_STATE_ID.getName(), runnerStateId);
	}
	
	@Test
	public void testExecuteSimple() throws ModelException {
		CancelBet cancelBet = new CancelBet();
		
		cancelBet.setBetId("1234");
		
		final BetCancelResult betCancelResult = new BetCancelResult(1234,10,20,BetCancelResultEnum.OK);
		mockery.checking(new Expectations() {
			{
				one(betApi).cancelBet(1234);
				will(returnValue(betCancelResult));
				
				one(bettingEngineDao).findBet(1234);
			}
		});
		
		ActionTester.executeAction(cancelBet,ctxParams);
		mockery.assertIsSatisfied();	
	}
	

	
	@Test(expected=IllegalArgumentException.class)
	public void testExecuteBetIdNotSet() throws ModelException {
		CancelBet cancelBet = new CancelBet();
	
		Map<String, Object> ctxParams = new HashMap<String, Object>();
		ActionTester.executeAction(cancelBet,ctxParams);
		
	}
}
