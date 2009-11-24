package dk.bot.bettingengine.statemachine;

import java.util.List;

import dk.bot.bettingengine.betapi.BetResult;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.marketobserver.model.MarketRunner;

/**
 * Represents a bet operation executed by a state machine, e.g. place/cancel bet.
 * 
 * @author daniel
 * 
 */
public class StateMachineResult {

	/** Unique id of state machine for market runner (composite key of a stateMachineId, marketId, selectionId) */
	private final int runnerStateId;

	private final String stateMachineId;

	/** Place or cancel bet */
	private final List<BetResult> betResults;

	private final int marketId;

	private final MarketRunner marketRunner;

	/**market runner price prediction*/
	private final Prediction prediction;

	/**What is the machine state before firing event.*/
	private final String inputStateName;
	
	/**What is the machine state after firing event.*/
	private final String outputStateName;
	
	public StateMachineResult(int runnerStateId,String stateMachineId, int marketId, MarketRunner marketRunner, Prediction prediction,List<BetResult> betResults,String inputStateName,String outputStateName) {
		this.runnerStateId = runnerStateId;
		this.stateMachineId = stateMachineId;
		this.marketId = marketId;
		this.marketRunner = marketRunner;
		this.prediction = prediction;
		this.betResults = betResults;
		this.inputStateName=inputStateName;
		this.outputStateName=outputStateName;
	}

	public String getStateMachineId() {
		return stateMachineId;
	}

	
	public List<BetResult> getBetResults() {
		return betResults;
	}

	public int getMarketId() {
		return marketId;
	}

	public MarketRunner getMarketRunner() {
		return marketRunner;
	}

	public int getRunnerStateId() {
		return runnerStateId;
	}

	public Prediction getPrediction() {
		return prediction;
	}

	public String getInputStateName() {
		return inputStateName;
	}

	public String getOutputStateName() {
		return outputStateName;
	}		
}
