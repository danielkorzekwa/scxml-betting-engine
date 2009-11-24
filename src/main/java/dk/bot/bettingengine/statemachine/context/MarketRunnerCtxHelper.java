package dk.bot.bettingengine.statemachine.context;

import java.util.List;

import dk.bot.bettingengine.utils.BetOutcome;
import dk.bot.marketobserver.model.BetStatus;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.MarketRunner;

/**
 * Some helper methods to calculate ctx variables
 * 
 * @author daniel
 * 
 */
public class MarketRunnerCtxHelper {

	public static double getMarketProfit(List<MUBet> muBets, int winnerSelectionId) {

		double profit = 0;

		for (MUBet bet : muBets) {
			if (bet.getBetStatus().equals(BetStatus.M)) {
				double betProfit = BetOutcome.calculateProfit(bet.getBetType(), bet.getSize(), bet.getPrice(), bet.getSelectionId()==winnerSelectionId);
				profit = profit + betProfit;
			}
		}
		
		return profit;
	}

	/**
	 * Market profit is>=0 for all market winners and at least for one winner
	 * profit is > 0
	 * 
	 */
	public static boolean isMarketGreen(List<MarketRunner> runners,List<MUBet> muBets)  {
		boolean isProfit=false;
		for(MarketRunner runner: runners) {
			double profit = getMarketProfit(muBets, runner.getSelectionId());
			if(profit<0) {
				return false;
			}
			else if(profit>0){
				isProfit=true;
			}
		}
		
		return isProfit;
	}
	
	/**
	 * Runner profit is>=0 when runner win and lose and at least for one scenario
	 * profit is > 0
	 * 
	 */
	public static boolean isRunnerGreen(int selectionId,List<MUBet> muBets)  {
		boolean isProfit=false;
		
		/**Runner win*/
		double profit = getMarketProfit(muBets, selectionId);
		if(profit<0) {
			return false;
		}
		else if(profit>0){
			isProfit=true;
		}
		
		/**Runner lose*/
		profit = getMarketProfit(muBets, -1);
		if(profit<0) {
			return false;
		}
		else if(profit>0){
			isProfit=true;
		}
		
		return isProfit;
	}
	
	
		
}
