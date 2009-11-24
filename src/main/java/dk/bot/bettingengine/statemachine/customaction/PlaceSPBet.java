package dk.bot.bettingengine.statemachine.customaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.util.MathUtils;
import org.apache.commons.scxml.SCXMLExpressionException;

import dk.bot.marketobserver.model.BetType;

/**
 * Place a sp bet on a BetFair exchange.
 * 
 * @author daniel
 * 
 */
public class PlaceSPBet extends AbstractAction {

	private final Log log = LogFactory.getLog(PlaceSPBet.class.getSimpleName());

	/**If not set then SP MoC, otherwise SP LoC*/
	private String price;
	private Double priceValue;

	private String liability;
	private double liabilityValue;

	private String betType;
	private BetType betTypeValue;

	public void setPrice(String price) {
		this.price = price;
	}

	public void setLiability(String liability) {
		this.liability = liability;
	}

	public void setBetType(String betType) {
		this.betType = betType;
	}

	@Override
	public void execute() throws SCXMLExpressionException {
		parseParameters();
		getBetApi().placeSPBet(getMarket().getMarketId(), getMarketRunner().getSelectionId(), betTypeValue, liabilityValue, priceValue);
	}

	private void parseParameters() throws SCXMLExpressionException {

		if (price != null) {
			priceValue = ((Number) getExprEval().eval(price)).doubleValue();
		} 

		if (liability != null) {
			try {
				liabilityValue = ((Number) getExprEval().eval(liability)).doubleValue();
				if(liabilityValue<2) {
					liabilityValue=2;
				}
				liabilityValue=MathUtils.round(liabilityValue, 2);
			} catch (Exception e) {
				liabilityValue = 2d;
				log.warn("Cannot parse sp bet liability: " + liability + ". Using default sp bet liability: 2");
			}
		} else {
			throw new IllegalArgumentException("The 'liability' parameter is not set.");
		}

		if (betType != null) {
			betTypeValue = BetType.valueOf((String) getExprEval().eval(betType));
		} else {
			throw new IllegalArgumentException("The 'betType' parameter is not set.");
		}

	}

}
