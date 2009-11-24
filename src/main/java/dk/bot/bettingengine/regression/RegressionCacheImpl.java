package dk.bot.bettingengine.regression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import dk.bot.marketobserver.model.MarketRunners;

/**
 * Cache for a recent history of markets. Allows to calculate market runner regression.
 * 
 * I'm not a thread-safe
 * 
 * @author daniel
 * 
 */
public class RegressionCacheImpl implements RegressionCache {

	/** Key - marketId, Value - history for market. */
	private Map<Integer, LinkedList<MarketRunners>> cache = new HashMap<Integer, LinkedList<MarketRunners>>();
	private final int maxAgeInSeconds;
	private final int maxNumOfDataPerMarket;

	/**
	 * History for market older than number of seconds is removed on the time of adding a new market data into the
	 * cache.
	 * 
	 * @param maxAgeInSeconds
	 * @param maxNumOfDataPerMarket Maximum number of time stamped data per market
	 */
	public RegressionCacheImpl(int maxAgeInSeconds, int maxNumOfDataPerMarket) {
		this.maxAgeInSeconds = maxAgeInSeconds;
		this.maxNumOfDataPerMarket = maxNumOfDataPerMarket;
	}

	public List<MarketRunners> getMarketHistory(int marketId) {
		
		LinkedList<MarketRunners> marketCache = cache.get(marketId);
		if (marketCache == null) {
			marketCache = new LinkedList<MarketRunners>();
		}
		return marketCache;
	}

	public void putMarketRunners(MarketRunners marketRunners) {
			LinkedList<MarketRunners> marketCache = cache.get(marketRunners.getMarketId());
			if (marketCache == null) {
				marketCache = new LinkedList<MarketRunners>();
				cache.put(marketRunners.getMarketId(), marketCache);
			}

			/** Put into a cache only if marketRunners is newer than the newest marketRunners in a cache. */
			if (marketCache.size() == 0
					|| marketRunners.getTimestamp().getTime() > marketCache.getFirst().getTimestamp().getTime()) {
				marketCache.addFirst(marketRunners);
			}

			/**Keep only a max number of elements in a cache*/
			while(marketCache.size()>maxNumOfDataPerMarket) {
				marketCache.remove(3);
			}
			
			/** Remove expired elements from cache */
			long now = DateTimeUtils.currentTimeMillis();
			while (marketCache.size() > 0
					&& (now - marketCache.getLast().getTimestamp().getTime()) / 1000 > maxAgeInSeconds) {
				marketCache.removeLast();
			}
	}
	
	public int removeMarkets(int olderThanNumOfHours) {
		
		List<Integer> toRemove = new ArrayList<Integer>();
		
		for(Integer marketId: cache.keySet()) {
			LinkedList<MarketRunners> marketCache = cache.get(marketId);
			if(marketCache.size()==0 || (new DateTime().toDate().getTime() - marketCache.getFirst().getTimestamp().getTime())/(3600*1000)>olderThanNumOfHours) {
				toRemove.add(marketId);
			}
		}
		
		for(Integer marketId: toRemove) {
			cache.remove(marketId);
		}
		return toRemove.size();
	}
	
	public RegressionCacheInfo getCacheInfo() {
		return new RegressionCacheInfo(this.maxAgeInSeconds, cache.size());
	}

}
