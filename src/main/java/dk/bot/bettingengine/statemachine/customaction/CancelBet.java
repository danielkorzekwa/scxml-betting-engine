package dk.bot.bettingengine.statemachine.customaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.scxml.SCXMLExpressionException;

import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.dao.model.RunnerBet;

/**
 * Cancel bet
 * 
 * @author daniel
 * 
 */
public class CancelBet extends AbstractAction {

	private final Log log = LogFactory.getLog(CancelBet.class.getSimpleName());

	
	private String betId;
	private long betIdValue;

	public void setBetId(String betId) {
		this.betId = betId;
	}

	@Override
	public void execute() throws SCXMLExpressionException {
		
			parseParameters();
			
			BettingEngineDAO bettingEngineDao = getBettingEngineDao();
			RunnerBet lastBet = bettingEngineDao.findBet(betIdValue);

			/** do not cancel if bet is totally matched */
			if (lastBet != null && lastBet.getSizeMatched() == lastBet.getSize()) {
				// do nothing
			} else {
				getBetApi().cancelBet(betIdValue);
			}
	}

	private void parseParameters() throws SCXMLExpressionException {
		if (betId != null) {
			betIdValue = ((Number) getExprEval().eval(betId)).longValue();
		} else {
			throw new IllegalArgumentException("The 'betId' parameter is not set.");
		}
	}
}
