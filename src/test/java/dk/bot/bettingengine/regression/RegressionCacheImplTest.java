package dk.bot.bettingengine.regression;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.MarketRunners;

public class RegressionCacheImplTest {

	private RegressionCacheImpl cache = new RegressionCacheImpl(60*10,3);
	private long now = System.currentTimeMillis();

	@Before
	public void setUp() {

		/** History for market 1 */
		MarketRunners marketRunners = new MarketRunners(1, new ArrayList<MarketRunner>(), 0, new Date(
				now - 1000 * 60 * 15));
		cache.putMarketRunners(marketRunners);
		marketRunners = new MarketRunners(1, new ArrayList<MarketRunner>(), 0, new Date(now - 1000 * 60 * 9));
		cache.putMarketRunners(marketRunners);
		marketRunners = new MarketRunners(1, new ArrayList<MarketRunner>(), 0, new Date(now - 1000 * 60 * 5));
		cache.putMarketRunners(marketRunners);

		/** History for market 2 */
		marketRunners = new MarketRunners(2, new ArrayList<MarketRunner>(), 0, new Date(now - 1000 * 60 * 12));
		cache.putMarketRunners(marketRunners);
		marketRunners = new MarketRunners(2, new ArrayList<MarketRunner>(), 0, new Date(now - 1000 * 60 * 11));
		cache.putMarketRunners(marketRunners);
		marketRunners = new MarketRunners(2, new ArrayList<MarketRunner>(), 0, new Date(now - 1000 * 60 * 3));
		cache.putMarketRunners(marketRunners);
	}

	@Test
	public void testGetHistoryForMarket1() {
		
		List<MarketRunners> history = cache.getMarketHistory(1);
		assertEquals(2, history.size());

		assertEquals(new Date(now - 1000 * 60 * 5), history.get(0).getTimestamp());
		assertEquals(new Date(now - 1000 * 60 * 9), history.get(1).getTimestamp());
	}

	@Test
	public void testGetHistoryForMarket2() {
		List<MarketRunners> history = cache.getMarketHistory(2);
		assertEquals(1, history.size());

		assertEquals(new Date(now - 1000 * 60 * 3), history.get(0).getTimestamp());
	}

	/**
	 * History for market older than number of seconds is removed on the time of adding a new market data into the
	 * cache.
	 */
	@Test
	public void testPutMarketRunnersCheckForRemovedHistory() {
		MarketRunners marketRunners = new MarketRunners(10, new ArrayList<MarketRunner>(), 0, new Date(
				now - 1000 * 60 * 25));
		cache.putMarketRunners(marketRunners);
		marketRunners = new MarketRunners(10, new ArrayList<MarketRunner>(), 0, new Date(now - 1000 * 60 * 24));
		cache.putMarketRunners(marketRunners);
		marketRunners = new MarketRunners(10, new ArrayList<MarketRunner>(), 0, new Date(now - 1000 * 60 * 9));
		cache.putMarketRunners(marketRunners);

		/** Only one element remains in the cache. */
		List<MarketRunners> history = cache.getMarketHistory(10);
		assertEquals(1, history.size());

		assertEquals(new Date(now - 1000 * 60 * 9), history.get(0).getTimestamp());

	}
	
	@Test
	public void testPutMarketRunnersCheckForMaxNumberDataPerMarket() {
		MarketRunners marketRunners = new MarketRunners(10, new ArrayList<MarketRunner>(), 0, new Date(
				now - 1000 * 60 * 5));
		cache.putMarketRunners(marketRunners);
		marketRunners = new MarketRunners(10, new ArrayList<MarketRunner>(), 0, new Date(now - 1000 * 60 * 4));
		cache.putMarketRunners(marketRunners);
		marketRunners = new MarketRunners(10, new ArrayList<MarketRunner>(), 0, new Date(now - 1000 * 60 * 3));
		cache.putMarketRunners(marketRunners);
		marketRunners = new MarketRunners(10, new ArrayList<MarketRunner>(), 0, new Date(now - 1000 * 60 * 2));
		cache.putMarketRunners(marketRunners);

		/** Only two elements are in cache because of maxNumOfDataPerMarket is 3. */
		List<MarketRunners> history = cache.getMarketHistory(10);
		assertEquals(3, history.size());
	}
	
	@Test
	public void testRemoveMarkets() {
		cache = new RegressionCacheImpl(3600*10,3);
		assertEquals(0, cache.getCacheInfo().getCacheSize());

		MarketRunners marketRunners = new MarketRunners(10, new ArrayList<MarketRunner>(), 0, new Date(
				now - 1000 * 3600 * 5));
		cache.putMarketRunners(marketRunners);
		
		marketRunners = new MarketRunners(11, new ArrayList<MarketRunner>(), 0, new Date(
				now - 1000 * 3600 * 4));
		cache.putMarketRunners(marketRunners);
		
		marketRunners = new MarketRunners(12, new ArrayList<MarketRunner>(), 0, new Date(
				now - 1000 * 3600 * 3));
		cache.putMarketRunners(marketRunners);
		
		assertEquals(3, cache.getCacheInfo().getCacheSize());
		
		int removed = cache.removeMarkets(3);
		assertEquals(2, removed);
		assertEquals(1, cache.getCacheInfo().getCacheSize());
		
		
	}
	
	@Test
	public void testGetCacheSize() {
		assertEquals(2,cache.getCacheInfo().getCacheSize());
		assertEquals(600,cache.getCacheInfo().getMaxAgeInSeconds());
	}
}
