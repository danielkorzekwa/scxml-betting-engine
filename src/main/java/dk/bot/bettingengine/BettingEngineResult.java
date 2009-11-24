package dk.bot.bettingengine;

import java.util.List;

import dk.bot.bettingengine.statemachine.StateMachineResult;

/** Result of calling betting engine */
public class BettingEngineResult {

	/** List of bet operations executed by a betting engine as a result of processing market event, e.g. placeBet, cancelBet */
	private List<StateMachineResult> betOperations;

	public List<StateMachineResult> getBetOperations() {
		return betOperations;
	}

	public void setBetOperations(List<StateMachineResult> betOperations) {
		this.betOperations = betOperations;
	}
}
