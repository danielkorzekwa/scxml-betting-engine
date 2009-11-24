package dk.bot.bettingengine.statemachine.manager.runnerstatecache;

import java.util.Date;
import java.util.List;

import dk.bot.bettingengine.statemachine.manager.RunnerStateMachine;
import dk.bot.bettingengine.statemachine.manager.StateMachineKey;
import dk.bot.bettingengine.statemachine.manager.StateMachineStat;

/** Cache for runner state machines. 
 * 
 * @author daniel
 *
 */
public interface RunnerStateCache {

	/** Gets runner state machine for market runner. Create new one if not exist.
	 * 
	 * @param machineKey
	 * @return
	 */
	public RunnerStateMachine get(StateMachineKey machineKey);
	
	/**Put runner state machine into a cache.*/
	public void put(StateMachineKey machineKey, RunnerStateMachine machine);
	

	/** Amount of machines stored in memory.
	 * 
	 * @return
	 */
	public int getMachinesAmount();
	
	/**Remove state machine with marketTime older than marketTime parameter.
	 * 
	 * @param marketTime
	 * @return amount of removed machines
	 */
	public int removeMachines(Date marketTime);
	
	/** Overall statistic for all state machine instances*/
	public StateMachineStat getVisitedStates();
	
	/** Returns list of all state names for state machine.
	 * 
	 * @return
	 */
	public List<String> getStates();
}
