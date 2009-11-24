package dk.bot.bettingengine.dao.model;


/**
 * 
 * @author daniel
 *
 */
public class RunnerState {

	private int id;
	
	private String stateMachineId;
	
	private int marketId;
	
	private int selectionId;
	
	private String stateName;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStateMachineId() {
		return stateMachineId;
	}

	public void setStateMachineId(String stateMachineId) {
		this.stateMachineId = stateMachineId;
	}

	public int getMarketId() {
		return marketId;
	}

	public void setMarketId(int marketId) {
		this.marketId = marketId;
	}

	public int getSelectionId() {
		return selectionId;
	}

	public void setSelectionId(int selectionId) {
		this.selectionId = selectionId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}
