package dk.bot.bettingengine.dao;

import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.bettingengine.dao.model.RunnerState;

/**
 * Provides persistent information required by a betting engine.
 * 
 * @author korzekwad
 * 
 */
public interface BettingEngineDAO {

	/**
	 * Returns a runner state object for a stateMachineId/marketId/selecttionId combination or creates a new one with
	 * defaultStateName if doesn't exist yet.
	 * 
	 * This method is called only once for each stateMachineId/marketId/selectionId combination, because it is cached by
	 * a betting engine.
	 * 
	 * @param stateMachineId
	 * @param marketId
	 * @param selectionId
	 * @param defaultStateName
	 *            If runner state doesn't exist in a data store then a new runner state should be created with a
	 *            defaultStateName
	 * @return
	 */
	public RunnerState findRunnerState(String stateMachineId, int marketId, int selectionId, String defaultStateName);
	
	/**Returns last bet (back or lay) placed by a state machine. Earlier bets are not returned.
	 * 
	 * @param betId
	 * @return null of bet not found
	 */
	public RunnerBet findBet(long betId);
	
	/**Returns last bet (back or lay) placed by a state machine. Earlier bets are not returned.
	 * 
	 * @param betId
	 * @return null of bet not found
	 */
	public RunnerBet findLastBet(int runnerStateId, String betType);
}
