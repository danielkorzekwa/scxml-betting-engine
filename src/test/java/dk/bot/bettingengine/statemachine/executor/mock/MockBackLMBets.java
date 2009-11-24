package dk.bot.bettingengine.statemachine.executor.mock;

import org.apache.commons.scxml.SCXMLExpressionException;

import dk.bot.bettingengine.statemachine.customaction.AbstractAction;

public class MockBackLMBets extends AbstractAction {

	public static boolean executed = false;

	public static long marketSuspendTime;

	public MockBackLMBets() {
		super();
	}

	@Override
	public void execute() throws SCXMLExpressionException {
		executed = true;

		marketSuspendTime = getMarket().getSuspendTime().getTime();
	}
	
}
