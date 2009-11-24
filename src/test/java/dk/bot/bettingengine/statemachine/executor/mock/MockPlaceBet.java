package dk.bot.bettingengine.statemachine.executor.mock;

import org.apache.commons.scxml.SCXMLExpressionException;

import dk.bot.bettingengine.statemachine.customaction.AbstractAction;

public class MockPlaceBet extends AbstractAction{
	
	private String price;
	private static double priceValue;
	
	public MockPlaceBet() {
		super();
	}
	
	public void execute() throws SCXMLExpressionException {
		priceValue = (Double) getExprEval().eval(price);
		
	}
	
	public void setPrice(String price) {
		this.price = price;
	}

	public static double getPriceValue() {
		return priceValue;
	}
	
}
