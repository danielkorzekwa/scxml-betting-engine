package dk.bot.bettingengine.regression;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.MarketRunners;

/**
 * Calculate market runner linear regression (predict trend of price).
 * 
 * @author daniel
 * 
 */
public class MarketRunnerRegression {

	/**
	 * Calculate runner linear regression (predict trend of price based on average runner price)
	 * 
	 * @param selectionId
	 *            Which runner the regression is calculated for.
	 * @param runnersHistory
	 *            Recent history of runners used to calculate regression.
	 * @param from
	 *            Regression is based on a recent history of runners from the the given date.
	 * @param timeUnit
	 *            Calculate regression as y = f(x) where x is timeUnit, e.g what is the trend of price per minute.
	 * @param runner/runnerTimestamp
	 *            Latest runner state
	 * @return
	 */
	public static Prediction calculateRegression(int selectionId, List<MarketRunners> runnersHistory,
			Date from, TimeUnit timeUnit, MarketRunner runner, Date runnerTimestamp) {
		List<Observation> observations = new ArrayList<Observation>();
	
		for (int i = runnersHistory.size() - 1; i >= 0; i--) {
			MarketRunners marketRunners = runnersHistory.get(i);
			
			if (marketRunners.getTimestamp().getTime() >= from.getTime()) {
				MarketRunner marketRunner = marketRunners.getMarketRunner(selectionId);
				if (marketRunner != null) {
					observations.add(new Observation(marketRunners.getTimestamp(), marketRunner
							.getAvgPrice()));
				}
			}
		}
		observations.add(new Observation(runnerTimestamp, runner.getAvgPrice()));

		Prediction prediction = PriceLinearRegression.predict(observations, timeUnit);
		return prediction;
	}
}
