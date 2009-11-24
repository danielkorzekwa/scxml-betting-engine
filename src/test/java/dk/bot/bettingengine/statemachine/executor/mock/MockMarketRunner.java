package dk.bot.bettingengine.statemachine.executor.mock;

import java.util.List;

import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.RunnerPrice;

public class MockMarketRunner extends MarketRunner {

	public double bestPriceToBack;

	public MockMarketRunner(int selectionId,double totalAmountMatched,double lastPriceMatched,double farSP,double nearSP,double actualSP,List<RunnerPrice> prices) {
		super(selectionId, totalAmountMatched, lastPriceMatched, farSP, nearSP, actualSP, prices);
	}
	
	@Override
	public double getPriceToBack() {
		return bestPriceToBack;
	}
}
