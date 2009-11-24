package dk.bot.bettingengine.statemachine;

import java.util.List;

import org.apache.commons.scxml.model.CustomAction;


/**Configuration bean for StateMachineService
 * 
 * @author daniel
 *
 */
public class StateMachineServiceConfig {

	/**List of state machines used for market runners monitoring.*/
	private final List<StateMachineInfo> stateMachines;
	
	/**List of available scxml custom actions.*/
	private final List<CustomAction> customActions;
	
	public StateMachineServiceConfig(List<StateMachineInfo> stateMachines,List<CustomAction> customActions) {
		this.stateMachines = stateMachines;
		this.customActions = customActions;
	}

	public List<StateMachineInfo> getStateMachines() {
		return stateMachines;
	}

	public List<CustomAction> getCustomActions() {
		return customActions;
	}
}
