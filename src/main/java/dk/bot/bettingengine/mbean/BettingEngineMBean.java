package dk.bot.bettingengine.mbean;

import java.util.Map;

import dk.bot.bettingengine.regression.Prediction;
import dk.bot.bettingengine.regression.RegressionCacheInfo;

/**JMX management interface for betting engine component.
 * 
 * @author daniel
 *
 */
public interface BettingEngineMBean {
	
	/**Runner price predictions for a market runners
	 * 
	 * @param marketId
	 * @return key - selectionId, value - price prediction for runner
	 */
	public Map<Integer, Prediction> getRunnerPricePredictions(int marketId);
	
	public RegressionCacheInfo getRegressionCacheInfo();
	
}
