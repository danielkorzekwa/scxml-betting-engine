package dk.bot.bettingengine.statemachine.executor;

import java.io.Serializable;

/**
 * Statistics for state transition: how many times transition has been executed.
 * 
 * @author daniel
 * 
 */
public class TransitionStat implements Serializable{

	private final String target;

	private final String condition;

	private int executions = 0;

	public TransitionStat(String target, String condition) {
		this.target = target;
		this.condition = condition;
	}

	public int getExecutions() {
		return executions;
	}

	public void addExecution() {
		this.executions = this.executions + 1;
	}

	public String getTarget() {
		return target;
	}

	public String getCondition() {
		return condition;
	}

	public void addExecutions(int executions) {
		this.executions = this.executions + executions;
	}
	
}
