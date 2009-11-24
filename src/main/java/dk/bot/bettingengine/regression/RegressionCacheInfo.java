package dk.bot.bettingengine.regression;

import java.io.Serializable;

/**
 * Information about regression cache.
 * 
 * @author daniel
 * 
 */
public class RegressionCacheInfo implements Serializable{
	/**
	 * Keep runners history in cache for last number of seconds. Default is 15 mins.
	 */
	private final int maxAgeInSeconds;

	/** How many RunnersSummary object are stored in a cache. */
	private final int cacheSize;

	public RegressionCacheInfo(int maxAgeInSeconds, int cacheSize) {
		this.maxAgeInSeconds = maxAgeInSeconds;
		this.cacheSize = cacheSize;
	}

	public int getMaxAgeInSeconds() {
		return maxAgeInSeconds;
	}

	public int getCacheSize() {
		return cacheSize;
	}
}