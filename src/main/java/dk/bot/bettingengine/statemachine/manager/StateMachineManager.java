package dk.bot.bettingengine.statemachine.manager;

import java.util.Date;
import java.util.List;

import dk.bot.bettingengine.regression.Prediction;
import dk.bot.bettingengine.statemachine.StateMachineResult;
import dk.bot.bettingengine.statemachine.executor.VisitedStatesStat;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketRunner;

/**
 * Manages a state machine for market runners.
 * 
 * @author daniel
 * 
 */
public interface StateMachineManager {

	/**
	 * Fires event on the state machine and updates the last state in the db
	 * 
	 * @param context
	 * @return Return result only if processing runner event had any effect, e.g. runner state was changed or bet was
	 *         placed/canceled, otherwise null is returned.
	 * 
	 */
	public StateMachineResult fireEvent(Market completeMarket, MarketRunner marketRunner, Prediction prediction);

	/**
	 * Amount of machines stored in memory.
	 * 
	 * @return
	 */
	public int getMachinesAmount();

	/**
	 * Remove state machine with marketTime older than marketTime parameter.
	 * 
	 * @param marketTime
	 * @return amount of removed machines
	 */
	public int removeMachines(Date marketTime);

	/** Overall statistic for state machines */
	public StateMachineStat getVisitedStates();

	/** Returns list of state names for state machine. */
	public List<String> getStates();

}
