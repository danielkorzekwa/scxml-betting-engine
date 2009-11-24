package dk.bot.bettingengine.statemachine;

import java.util.List;
import java.util.Map;

import dk.bot.bettingengine.statemachine.manager.StateMachineStat;

/**JMX management interface for state machine service.
 * 
 * @author daniel
 *
 */
public interface StateMachineServiceMBean {

	/** Amount of machines stored in memory.
	 * 
	 * @return
	 */
	public int getMachinesAmount();

	/** Overall statistic for all state machines. Key - stateMachineName, Value - Statistics object for state machine.*/
	public Map<String,StateMachineStat> getStateMachineStat();
	
	/**Returns list of stateMachineId for state machines.*/
	public List<String> getStateMachines();
	
	/** Returns list of state names for state machine.*/
	public List<String> getStates(String stateMachineId);
}
