package dk.bot.bettingengine.regression;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.MarketRunners;
import dk.bot.marketobserver.model.RunnerPrice;

public class MarketRunnerRegressionTest {

	@Test
	public void testCalculateRegression(){
		long now = System.currentTimeMillis();
		Date from = new Date(now - (1000*60*15));
		List<MarketRunners> runnersHistory = new ArrayList<MarketRunners>();
		runnersHistory.add(createMarketRunners(new Date(now - (1000*60*16)), 1, 2, 1.5d,1.5d)); //This observation is ignored - older than from date.
		runnersHistory.add(createMarketRunners(new Date(now - (1000*60*14)), 1, 2, 1.6d,1.6d));
		runnersHistory.add(createMarketRunners(new Date(now - (1000*60*13)), 1, 2, 1.7d,1.7d));
		runnersHistory.add(createMarketRunners(new Date(now - (1000*60*12)), 1, 2, 1.8d,1.8d));
		
		List<RunnerPrice> prices = new ArrayList<RunnerPrice>();
		prices.add(new RunnerPrice(1.9,2,0));
		prices.add(new RunnerPrice(1.9,0,2));
		MarketRunner lastRunnerState = new MarketRunner(1,0,2.03d,0,0,0,prices);
		Prediction regression = MarketRunnerRegression.calculateRegression(2,runnersHistory, from, TimeUnit.MINUTES,lastRunnerState, new Date(now - (1000*60*11)));
		
		assertEquals(73.08, regression.getSlope(),0.001);
		assertEquals(0.0013, regression.getSlopeErr(),0.001);
	}	
	
	/** Create marketRunners with one runner*/
	private MarketRunners createMarketRunners(Date observationDate,int marketId,int selectionId, double priceToBack, double priceToLay ) {
		
		MarketRunners marketRunners = new MarketRunners(1,new ArrayList<MarketRunner>(),5,observationDate);
		
		List<RunnerPrice> prices = new ArrayList<RunnerPrice>();
		prices.add(new RunnerPrice(priceToBack,2,0));
		prices.add(new RunnerPrice(priceToLay,0,2));
		MarketRunner marketRunner = new MarketRunner(selectionId,0,0,0,0,0,prices);
		marketRunners.getMarketRunners().add(marketRunner);
		
		return marketRunners;
	}

}
