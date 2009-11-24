package dk.bot.bettingengine.betapi;

import dk.bot.marketobserver.model.BetType;


/** Bet placement interface. Implementation must be provided from outside of a betting engine.
 * 
 * @author daniel
 *
 */
public interface BetApi {

	/** Place a bet on a betting exchange.
	 * 
	 * @param marketId
	 * @param selectionId
	 * @param betType
	 * @param price
	 * @param size
	 * @param checkTxLimit
	 *            If true then tx counter is incremented and limit is checked. If limit is reached then exception is
	 *            thrown. If checkTxLimit is false then tx counter is not incremented and tx limit is not checked.
	 * @return
	 */
	public BetPlaceResult placeBet(int marketId, int selectionId, BetType betType, double price, double size,
			boolean checkTxLimit);
	
	/** Place a sp bet on a betting exchange.
	 * 
	 * @param marketId
	 * @param selectionId
	 * @param betType
	 * @param liability
	 *            This is the maximum amount of money you want to risk for a BSP bet. The minimum amount for a back bet
	 *            is £2. The minimum amount for a lay bet is £10.
	 * @param limit
	 *            if not null then SP Limit on Price bet is placed
	 * @return
	 */
	public SPBetPlaceResult placeSPBet(int marketId, int selectionId, BetType betType, double liability,
			Double limit);
	
	public BetCancelResult cancelBet(long betId);
}
