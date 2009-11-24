package dk.bot.bettingengine.statemachine.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtils;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.betapi.BetCancelResult;
import dk.bot.bettingengine.betapi.BetPlaceResult;
import dk.bot.bettingengine.betapi.BetResult;
import dk.bot.bettingengine.betapi.SPBetPlaceResult;
import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.bettingengine.statemachine.StateMachineResult;
import dk.bot.bettingengine.statemachine.context.MarketRunnerCtxFactory;
import dk.bot.bettingengine.statemachine.executor.VisitedStatesStat;
import dk.bot.bettingengine.statemachine.manager.runnerstatecache.RunnerStateCache;
import dk.bot.marketobserver.model.BetType;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketFilter;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.util.MarketFilterMatcher;

/**
 * Manages a state machine for market runners.
 * 
 * I am thread-safe.
 * 
 * @author daniel
 * 
 */
public class StateMachineManagerImpl implements StateMachineManager {

	private final Log log = LogFactory.getLog(StateMachineManagerImpl.class.getSimpleName());

	private ReentrantLock lock = new ReentrantLock();

	
	private final BetApi betApi;

	private final MarketRunnerCtxFactory marketRunnerCtxFactory;

	private final RunnerStateCache runnerStateCache;

	private final List<MarketFilter> marketFilters;

	private final String stateMachineId;

	private final BettingEngineDAO bettingEngineDao;

	public StateMachineManagerImpl(String stateMachineId, List<MarketFilter> marketFilters,
			RunnerStateCache runnerStateCache, BetApi betApi,BettingEngineDAO bettingEngineDao,
			MarketRunnerCtxFactory marketRunnerCtxFactory) {
		this.stateMachineId = stateMachineId;
		this.marketFilters = marketFilters;
		this.runnerStateCache = runnerStateCache;
		this.betApi = betApi;
		this.bettingEngineDao = bettingEngineDao;
		this.marketRunnerCtxFactory = marketRunnerCtxFactory;

	}

	public StateMachineResult fireEvent(Market completeMarket, MarketRunner marketRunner, Prediction prediction) {
		lock.lock();
		try {
			/** check market filters */
			boolean matched = MarketFilterMatcher.match(completeMarket.getMarketData().getEventHierarchy(),
					completeMarket.getMarketData().getMarketName(), completeMarket.getBwinPrices(), completeMarket.getMarketRunners().getInPlayDelay()>0,marketFilters);
			if (matched) {
				long now = DateTimeUtils.currentTimeMillis();

				StateMachineKey machineKey = new StateMachineKey(completeMarket.getMarketData()
						.getMarketId(), marketRunner.getSelectionId(), new Date(completeMarket.getMarketData().getEventDate().getTime()));

				RunnerStateMachine machine = runnerStateCache.get(machineKey);

				/**
				 * Extends betApi with a logging of all bet operations executed by a state machine, e.g. placeBet,
				 * cancelBet.
				 */
				BetApiWrapper betApiWrapper = new BetApiWrapper(betApi);

				Map<String, Object> context = marketRunnerCtxFactory.createContext(machine.getRunnerStateId(),
						completeMarket, marketRunner, prediction, betApiWrapper, bettingEngineDao, now);

				/** What is the machine state before firing event. */
				String inputStateName = machine.getStateMachine().getCurrentStateId();
				machine.getStateMachine().fireEvent("runnerEvent", context);
				/** What is the machine state after firing event. */
				String outputStateName = machine.getStateMachine().getCurrentStateId();

				/**
				 * Return result only if processing runner event had any effect, e.g. runner state was changed or bet
				 * was placed/canceled.
				 */
				if (!inputStateName.equals(outputStateName) || betApiWrapper.getBetResults().size() > 0) {
					StateMachineResult stateMachineResult = new StateMachineResult(machine.getRunnerStateId(),
							stateMachineId, completeMarket.getMarketRunners().getMarketId(), marketRunner, prediction,
							betApiWrapper.getBetResults(), inputStateName, outputStateName);
					return stateMachineResult;
				} else {
					return null;
				}

			} else {
				return null;
			}

		} finally {
			lock.unlock();
		}

	}

	public int getMachinesAmount() {
		lock.lock();
		try {
			return runnerStateCache.getMachinesAmount();
		} finally {
			lock.unlock();
		}

	}

	public int removeMachines(Date marketTime) {
		lock.lock();
		try {
			return runnerStateCache.removeMachines(marketTime);

		} finally {
			lock.unlock();
		}

	}

	public StateMachineStat getVisitedStates() {
		lock.lock();
		try {
			return runnerStateCache.getVisitedStates();
		} finally {
			lock.unlock();
		}
	}

	/** Returns list of state names for state machine. */
	public List<String> getStates() {
		return runnerStateCache.getStates();
	}

	/**
	 * Logs bet operations executed by a state machine.
	 * 
	 * @author daniel
	 * 
	 */
	private class BetApiWrapper implements BetApi {

		private final BetApi betApi;
		private List<BetResult> betResults = new ArrayList<BetResult>();

		public List<BetResult> getBetResults() {
			return betResults;
		}

		public BetApiWrapper(BetApi betApi) {
			this.betApi = betApi;
		}

		public BetCancelResult cancelBet(long betId) {
			BetCancelResult result = betApi.cancelBet(betId);
			betResults.add(result);
			return result;
		}

		public BetPlaceResult placeBet(int marketId, int selectionId, BetType betType, double price, double size,
				boolean checkTxLimit) {
			BetPlaceResult result = betApi.placeBet(marketId, selectionId, betType, price, size, checkTxLimit);
			betResults.add(result);
			return result;
		}

		public SPBetPlaceResult placeSPBet(int marketId, int selectionId, BetType betType, double liability,
				Double limit) {
			SPBetPlaceResult result = betApi.placeSPBet(marketId, selectionId, betType, liability, limit);
			betResults.add(result);
			return result;
		}

	}
}
