package dk.bot.bettingengine.statemachine.manager;

import java.util.Date;

/** Unique id of state machine
 * 
 * @author daniel
 *
 */
public class StateMachineKey {

	private final int marketId;
	private final int selectionId;
	private final Date marketTime;

	public StateMachineKey(int marketId, int selectionId, Date marketTime) {
		this.marketId = marketId;
		this.selectionId = selectionId;
		this.marketTime = marketTime;
	}

	public int getMarketId() {
		return marketId;
	}

	public int getSelectionId() {
		return selectionId;
	}

	public Date getMarketTime() {
		return marketTime;
	}

	
	@Override
	public boolean equals(Object obj) {
		StateMachineKey key = (StateMachineKey)obj;
		return getKeyAsString().equals(key.getKeyAsString());
	}
	
	@Override
	public int hashCode() {
		return getKeyAsString().hashCode();
	}
	
	public String getKeyAsString() {
		return marketId + ":" + selectionId;
	}
}
