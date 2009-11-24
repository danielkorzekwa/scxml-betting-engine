package dk.bot.bettingengine.statemachine.executor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Which states and transitions of state machine were visited and how many
 * times.
 * 
 * @author daniel
 * 
 */
public class VisitedStatesStat implements Serializable{

	/** Statistic for state. Key - stateName */
	private LinkedHashMap<String, StateStat> visitedStates = new LinkedHashMap<String, StateStat>();

	public void put(String stateId, StateStat stateStat) {
		visitedStates.put(stateId, stateStat);
	}

	public StateStat get(String stateId) {
		return visitedStates.get(stateId);
	}

	/** Return all state stats. */
	public List<StateStat> getStateStats() {
		return new ArrayList<StateStat>(visitedStates.values());
	}

	/** Accumulate statistics */
	public void accumulate(VisitedStatesStat visitedStates) {
		for (StateStat statToAdd : visitedStates.getStateStats()) {
			StateStat stateStat = this.visitedStates.get(statToAdd.getStateName());
			if (stateStat == null) {
				stateStat = new StateStat(statToAdd.getStateName(), new ArrayList<TransitionStat>());
			}
			stateStat.accumulate(statToAdd);
			this.visitedStates.put(statToAdd.getStateName(), stateStat);
		}
	}
}
