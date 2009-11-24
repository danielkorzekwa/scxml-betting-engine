package dk.bot.bettingengine.statemachine.executor.mock;

import java.util.Map;

import dk.bot.marketobserver.tasks.bwin.BwinMarketPrices;

public class MockBwinMarketPrices extends BwinMarketPrices{

	public double odd;
	
	public MockBwinMarketPrices(int marketId,Map<Integer, Double> runnerPrices) {
		super(marketId,runnerPrices);
	}

	@Override
	public Double getRunnerPrice(int selectionId) {
		return odd;
	}
}
