package dk.bot.bettingengine.statemachine.executor;

import java.io.Serializable;
import java.util.List;

/**
 * Statistics for state machine: how many times state has been visited and how
 * many times transitions have been executed.
 * 
 * 
 * @author daniel
 * 
 */
public class StateStat implements Serializable{

	private int visits = 0;
	private final String stateName;
	private final List<TransitionStat> transitionStats;

	public StateStat(String stateName, List<TransitionStat> transitionStats) {
		this.stateName = stateName;
		this.transitionStats = transitionStats;
	}

	public int getVisits() {
		return visits;
	}

	public void addVisit() {
		this.visits = this.visits + 1;
	}

	public String getStateName() {
		return stateName;
	}

	public List<TransitionStat> getTransitionStats() {
		return transitionStats;
	}

	public TransitionStat getTransitionStat(String target, String condition) {
		condition = condition != null ? condition : "";

		for (TransitionStat stat : transitionStats) {
			String statCond = stat.getCondition() != null ? stat.getCondition() : "";
			if (stat.getTarget().equals(target) && statCond.equals(condition)) {
				return stat;
			}
		}
		return null;
	}

	/** Accumulate statistics */
	public void accumulate(StateStat stateStat) {
		this.visits = this.visits + stateStat.getVisits();

		for (TransitionStat transStatToAdd : stateStat.getTransitionStats()) {
			TransitionStat transitionStat = this.getTransitionStat(transStatToAdd.getTarget(), transStatToAdd
					.getCondition());
			if (transitionStat == null) {
				transitionStat = new TransitionStat(transStatToAdd.getTarget(), transStatToAdd.getCondition());
				transitionStat.addExecutions(transStatToAdd.getExecutions());
				this.transitionStats.add(transitionStat);
			}
			else {
				transitionStat.addExecutions(transStatToAdd.getExecutions());
			}
			
		}
	}

}
