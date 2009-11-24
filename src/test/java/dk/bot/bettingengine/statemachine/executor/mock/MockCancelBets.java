package dk.bot.bettingengine.statemachine.executor.mock;

import org.apache.commons.scxml.SCXMLExpressionException;

import dk.bot.bettingengine.statemachine.customaction.AbstractAction;


public class MockCancelBets extends AbstractAction{

	public static boolean executed=false;
	
	public MockCancelBets() {
	super();
	}

	@Override
	public void execute() throws SCXMLExpressionException {
		executed=true;
		
	}

	
}
