package dk.bot.bettingengine;

import java.util.Date;
import java.util.Map;

import dk.bot.bettingengine.regression.Prediction;
import dk.bot.marketobserver.model.Market;

/**
 * Main bean of a betting engine
 * 
 * @author daniel
 * 
 */
public interface BettingEngine {

	/**
	 * Fire event on state machines
	 * 
	 * @param completeMarket
	 * @param runnersHistory
	 * @return Result of firing event, e.g. how the price predictions have been changed based on a new market data.
	 */
	public BettingEngineResult fireEvent(Market completeMarket);

	/**Runner price predictions for a market runners
	 * 
	 * @param marketId
	 * @return key - selectionId, value - price prediction for runner
	 */
	public Map<Integer, Prediction> getRunnerPricePredictions(int marketId);
	
	/**Remove state machine with marketTime older than marketTime parameter.
	 * 
	 * @param marketTime
	 * @return amount of removed machines
	 */
	public int removeMachines(Date marketTime);
	
	/** Remove markets from cache that the youngest time stamped data is older than num of hours.
	 * 
	 * @param olderThanNumOfHours
	 * @return Number of removed markets
	 */
	public int removeMarketsFromRegressionCache(int olderThanNumOfHours);
}
