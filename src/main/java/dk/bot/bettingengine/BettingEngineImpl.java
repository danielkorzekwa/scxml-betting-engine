package dk.bot.bettingengine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtils;

import dk.bot.bettingengine.regression.MarketRunnerRegression;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.bettingengine.regression.RegressionCache;
import dk.bot.bettingengine.statemachine.StateMachineResult;
import dk.bot.bettingengine.statemachine.StateMachineService;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.MarketRunners;

/**
 * I'm not a thread safe.
 * 
 * @author daniel
 * 
 */
public class BettingEngineImpl implements BettingEngine {

	private final Log log = LogFactory.getLog(BettingEngineImpl.class.getSimpleName());

	private StateMachineService stateMachineService;
	
	private final RegressionCache regressionCache;

	/** Key - marketId, Value - map: key - selectionId, value - price prediction for runner. */
	private Map<Integer, Map<Integer, Prediction>> marketRunnerPricePredictions = new HashMap<Integer, Map<Integer, Prediction>>();

	public BettingEngineImpl(StateMachineService stateMachineService, RegressionCache regressionCache) {
		this.stateMachineService = stateMachineService;
		this.regressionCache = regressionCache;
	}

	/**Regression stuff is commented - not using it now and it's a has a big memory usage.*/
	public BettingEngineResult fireEvent(Market completeMarket) {

		//Map<Integer, Prediction> runnerPricePredictionMap = new HashMap<Integer, Prediction>(completeMarket
		//		.getMarketRunners().getMarketRunners().size());

		List<StateMachineResult> stateMachineResults = new ArrayList<StateMachineResult>();
		/** Send runner events to stateMachinesService */
		for (MarketRunner marketRunner : completeMarket.getMarketRunners().getMarketRunners()) {
			try {
				long now = DateTimeUtils.currentTimeMillis();
			//	List<MarketRunners> marketHistory = regressionCache.getMarketHistory(completeMarket.getMarketRunners()
			//			.getMarketId());
			//	Prediction prediction = MarketRunnerRegression.calculateRegression(marketRunner.getSelectionId(),
			//			marketHistory, new Date(now - 1000 * 300), TimeUnit.SECONDS, marketRunner, completeMarket
			//					.getMarketRunners().getTimestamp());

				List<StateMachineResult> runnerBetResults = stateMachineService.fireEvent(completeMarket, marketRunner,
						new Prediction());
				stateMachineResults.addAll(runnerBetResults);
			//	runnerPricePredictionMap.put(marketRunner.getSelectionId(), prediction);
			} catch (Exception e) {
				log.error("Fire event error" + e);
			}
		}
	//	regressionCache.putMarketRunners(completeMarket.getMarketRunners());
	//	marketRunnerPricePredictions.put(completeMarket.getMarketRunners().getMarketId(), runnerPricePredictionMap);

		BettingEngineResult result = new BettingEngineResult();
		result.setBetOperations(stateMachineResults);
		return result;
	}

	public Map<Integer, Prediction> getRunnerPricePredictions(int marketId) {
		return marketRunnerPricePredictions.get(marketId);
	}

	public int removeMachines(Date marketTime) {
		return stateMachineService.removeMachines(marketTime);
	}
	
	public int removeMarketsFromRegressionCache(int olderThanNumOfHours) {
		return regressionCache.removeMarkets(olderThanNumOfHours);
	}
}
