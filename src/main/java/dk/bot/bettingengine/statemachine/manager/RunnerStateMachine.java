package dk.bot.bettingengine.statemachine.manager;

import dk.bot.bettingengine.statemachine.executor.StateMachineExecutor;

/** Represents state machine for runner
 * 
 * @author daniel
 *
 */
public class RunnerStateMachine {

	/**Unique id of runner state machine*/
	private final int runnerStateId;
	
	/**State machine for runner.*/
	private final StateMachineExecutor stateMachine;
	
	public RunnerStateMachine(int runnerStateId,StateMachineExecutor stateMachine) {
		this.runnerStateId=runnerStateId;
		this.stateMachine=stateMachine;
	}

	public int getRunnerStateId() {
		return runnerStateId;
	}

	public StateMachineExecutor getStateMachine() {
		return stateMachine;
	}
}
