package dk.bot.bettingengine.betapi;



/** Bet cancel result object.
 * 
 * @author daniel
 *
 */
public class BetCancelResult implements BetResult{

	private final long betId;
	
	/** Amount cancelled by cancel operation. O if status!=OK*/
	final private double sizeCancelled;
	
	/**Amount of original bet matched since placement.*/
	final private double sizeMatched;

	final private BetCancelResultEnum status;
	
	public BetCancelResult(long betId, double sizeCancelled,double sizeMatched, BetCancelResultEnum status) {
		this.betId=betId;
		this.sizeCancelled = sizeCancelled;
		this.sizeMatched = sizeMatched;
		this.status = status;
	}

	public double getSizeCancelled() {
		return sizeCancelled;
	}

	public double getSizeMatched() {
		return sizeMatched;
	}

	public BetCancelResultEnum getStatus() {
		return status;
	}

	public long getBetId() {
		return betId;
	}
}
