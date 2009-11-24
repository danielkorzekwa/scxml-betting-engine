package dk.bot.bettingengine.statemachine;

import java.net.URL;
import java.util.List;

import dk.bot.marketobserver.model.MarketFilter;

/**Represents state machine information*/
public class StateMachineInfo {

	/**State machine unique id*/
	private final String stateMachineId;
	
	/**Points to the state machine scxml file.*/
	private final URL stateMachineUrl;

	/**Defines list of markets that should be analyzed by state machine.*/
	private final List<MarketFilter> marketFilters;
	
	public StateMachineInfo(String stateMachineId,URL stateMachineUrl, List<MarketFilter> marketFilters) {
		this.marketFilters = marketFilters;
		this.stateMachineId = stateMachineId;
		this.stateMachineUrl = stateMachineUrl;
	}
	
	public List<MarketFilter> getMarketFilters() {
		return marketFilters;
	}



	public String getStateMachineId() {
		return stateMachineId;
	}

	public URL getStateMachineUrl() {
		return stateMachineUrl;
	}
}
