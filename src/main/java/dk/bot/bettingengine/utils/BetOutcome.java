package dk.bot.bettingengine.utils;

import dk.bot.marketobserver.model.BetType;

/**
 * Calculate profit for bet depending on it's outcome (winner/loser).
 * 
 * @author daniel
 * 
 */
public class BetOutcome {

	/**
	 * What is the bet profit for bet (no commission is calculated)
	 * 
	 * @param betType
	 * @param price
	 * @param size
	 * @param winner
	 *            true for winner, false for loser
	 * @return
	 */
	public static double calculateProfit(BetType betType, double size, double price, boolean winner) {
		if (betType == BetType.B && winner) {
			return (size * price) - size;
		} else if (betType == BetType.B && !winner) {
			return -size;
		} else if (betType == BetType.L && winner) {
			return -((size * price) - size);
		} else if (betType == BetType.L && !winner) {
			return size;
		} else {
			throw new IllegalStateException("BetType + winner combination not found: " + betType + ":" + winner);
		}
	}
}
