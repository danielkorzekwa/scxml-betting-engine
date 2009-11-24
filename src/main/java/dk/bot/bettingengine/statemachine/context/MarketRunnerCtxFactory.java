package dk.bot.bettingengine.statemachine.context;

import java.util.Map;
import java.util.Random;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketRunner;

public interface MarketRunnerCtxFactory {


	/**
	 * Create context which is used from Scxml state machine, e.g. in condition
	 * to check current odds
	 * 
	 * @return
	 * @throws BetFairException
	 */
	public Map<String, Object> createContext(int runnerStateId,Market compositeMarket,
			MarketRunner runner, Prediction prediction,BetApi betApi,BettingEngineDAO bettingEngineDao, long now);
	
	/**
	 * Create context which is used from Scxml state machine, e.g. in condition
	 * to check current odds. 
	 * 
	 * @return
	 * @throws BetFairException
	 */
	public Map<String, Object> createRandomContext(Random random);
}
