package dk.bot.bettingengine.statemachine.customaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.util.MathUtils;
import org.apache.commons.scxml.SCXMLExpressionException;

import dk.bot.marketobserver.model.BetType;

/**
 * Place a bet
 * 
 * @author daniel
 * 
 */
public class PlaceBet extends AbstractAction {

	private final Log log = LogFactory.getLog(PlaceBet.class.getSimpleName());

	private String price;
	private double priceValue;

	private String size;
	private double sizeValue;

	private String minPrice;
	private Double minPriceValue;

	private String maxPrice;
	private Double maxPriceValue;

	private String betType;
	private BetType betTypeValue;

	private String checkTxCounter;
	private boolean checkTxCounterValue;

	public void setCheckTxCounter(String checkTxCounter) {
		this.checkTxCounter = checkTxCounter;

	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public void setBetType(String betType) {
		this.betType = betType;
	}

	@Override
	public void execute() throws SCXMLExpressionException {

		parseParameters();

		if (minPriceValue != null && priceValue < minPriceValue) {
			priceValue = minPriceValue;
		}
		if (maxPriceValue != null && priceValue > maxPriceValue) {
			priceValue = maxPriceValue;
		}

		getBetApi().placeBet(getMarket().getMarketId(), getMarketRunner().getSelectionId(), betTypeValue, priceValue,
				sizeValue, checkTxCounterValue);

	}

	private void parseParameters() throws SCXMLExpressionException {

		if (price != null) {
			priceValue = ((Number) getExprEval().eval(price)).doubleValue();
		} else {
			throw new IllegalArgumentException("The 'price' parameter is not set.");
		}

		if (minPrice != null) {
			minPriceValue = ((Number) getExprEval().eval(minPrice)).doubleValue();
		} else {
			minPriceValue = null;
		}
		if (maxPrice != null) {
			maxPriceValue = ((Number) getExprEval().eval(maxPrice)).doubleValue();
		} else {
			maxPriceValue = null;
		}
		if (minPriceValue != null && maxPriceValue != null && minPriceValue > maxPriceValue) {
			throw new IllegalArgumentException("The minPriceValue is bigger than maxPriceValue: " + minPriceValue + ">"
					+ maxPriceValue);
		}

		if (size != null) {
			try {
				sizeValue = ((Number) getExprEval().eval(size)).doubleValue();
				if (sizeValue < 2) {
					sizeValue = 2;
				}
				sizeValue = MathUtils.round(sizeValue, 2);
			} catch (Exception e) {
				sizeValue = 2d;
				log.warn("Cannot parse bet size: " + size + ". Using default bet size: 2");
			}
		} else {
			throw new IllegalArgumentException("The 'size' parameter is not set.");
		}

		if (betType != null) {
			betTypeValue = BetType.valueOf((String) getExprEval().eval(betType));
		} else {
			throw new IllegalArgumentException("The 'betType' parameter is not set.");
		}

		if (checkTxCounter != null) {
			checkTxCounterValue = Boolean.parseBoolean(checkTxCounter);
		} else {
			checkTxCounterValue = true;
		}
	}

}
