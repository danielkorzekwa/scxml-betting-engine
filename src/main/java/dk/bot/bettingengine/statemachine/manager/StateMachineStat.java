package dk.bot.bettingengine.statemachine.manager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import dk.bot.bettingengine.statemachine.executor.VisitedStatesStat;

/**Statistics for a state machine
 * 
 * @author daniel
 *
 */
public class StateMachineStat implements Serializable{

	/**How many times states/transitions for all stat machine instances were visited.*/
	private VisitedStatesStat visitedStatesStat;
	
	/**Key - state name, value - number of state machine instances in a given state.*/
	private Map<String,Integer> currentStates = new HashMap<String, Integer>();

	public VisitedStatesStat getVisitedStatesStat() {
		return visitedStatesStat;
	}

	public void setVisitedStatesStat(VisitedStatesStat visitedStatesStat) {
		this.visitedStatesStat = visitedStatesStat;
	}

	public Map<String, Integer> getCurrentStates() {
		return currentStates;
	}

	public void setCurrentStates(Map<String, Integer> currentStates) {
		this.currentStates = currentStates;
	}
	
}
