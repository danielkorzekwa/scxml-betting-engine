package dk.bot.bettingengine.statemachine;

import java.util.Date;
import java.util.List;

import dk.bot.bettingengine.regression.Prediction;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketRunner;

/**
 * Creates new state machines for new runners, updates state machines in the db,
 * allows to send events to state machines
 * 
 * @author daniel
 * 
 */
public interface StateMachineService {

	/**Fire event on state machines
	 * 
	 * @param completeMarket
	 * @param marketRunner
	 * @param runnersHistory
	 * @param prediction runner price prediction
	 * @return List of bet operations executed by a betting engine as a result of processing market event, e.g. placeBet, cancelBet.
	 */
	public List<StateMachineResult> fireEvent(Market completeMarket, MarketRunner marketRunner,Prediction prediction);
		
	/**Remove state machine with marketTime older than marketTime parameter.
	 * 
	 * @param marketTime
	 * @return amount of removed machines
	 */
	public int removeMachines(Date marketTime);

}
