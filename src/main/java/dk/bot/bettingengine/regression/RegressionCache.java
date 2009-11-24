package dk.bot.bettingengine.regression;

import java.util.List;

import dk.bot.marketobserver.model.MarketRunners;

/** Cache for a recent history of markets. Allows to calculate market runner regression.
 * 
 * @author daniel
 *
 */
public interface RegressionCache {

	/** Put marketRunners into the cache
	 * @param runnersSummary
	 */
	public void putMarketRunners(MarketRunners marketRunners);
	
	/**Get history for market
	 * @return First element in the list is the newest, last is the oldest.
	 */
	public List<MarketRunners> getMarketHistory(int marketId);
	
	public RegressionCacheInfo getCacheInfo();
	
	/** Remove markets from cache that the youngest time stamped data is older than num of hours.
	 * 
	 * @param olderThanNumOfHours
	 * @return number of returned markets
	 */
	public int removeMarkets(int olderThanNumOfHours);
}
