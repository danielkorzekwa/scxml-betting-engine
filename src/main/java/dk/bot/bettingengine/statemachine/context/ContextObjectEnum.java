package dk.bot.bettingengine.statemachine.context;

/** Context objects accessible from scxml state machine
 * 
 * @author daniel
 *
 */
public enum ContextObjectEnum {
	
	MARKET("obj.market"),
	RUNNER("obj.runner"),
	BET_API("obj.betApi"),
	BETTING_ENGINE_DAO("obj.bettingEngineDao");

	private final String contextName;

	ContextObjectEnum(String contextName) {
		this.contextName = contextName;	
	}

	public String getName() {
		return contextName;
	}	
}
