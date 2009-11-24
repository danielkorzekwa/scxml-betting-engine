package dk.bot.bettingengine.statemachine.context;

/** Context variables accessible from scxml state machine
 * 
 * @author daniel
 *
 */
public enum ContextVariableEnum {
	
	MARKET_ISGREEN("market.isGreen"),
	RUNNER_ISGREEN("runner.isGreen"),
	RUNNER_NAME("runner.name"),
	
	MARKET_TOTALAMOUNT_MATCHED("market.totalMatched"),
	MARKET_NAME("market.name"),
	MARKET_ISBSPMARKET("market.isBspMarket"),
	MARKET_INPLAYDELAY("market.inPlayDelay"),
	MARKET_ISTURNING_INPLAY("market.isTurningInPlay"),
	MARKET_DETAILS_SUSPEND_TIME("marketDetails.marketSuspendTime"),
	MARKET_EVENTPATH("market.eventPath"),
	RUNNER_PRICETOBACK("runner.priceToBack"),
	/** 1 - 100%, 0 - 0% */
	RUNNER_PRICETOBACK_PROB("runner.priceToBackProb"),
	RUNNER_PRICETOLAY("runner.priceToLay"),
	/** 1 - 100%, 0 - 0% */
	RUNNER_PRICETOLAY_PROB("runner.priceToLayProb"),
	RUNNER_PRICENEARSP("runner.priceNearSP"),
	/** 1 - 100%, 0 - 0% */
	RUNNER_PRICENEARSP_PROB("runner.priceNearSPProb"),
	RUNNER_PRICEFARSP("runner.priceFarSP"),
	/** 1 - 100%, 0 - 0% */
	RUNNER_PRICEFARSP_PROB("runner.priceFarSPProb"),
	
	/** value = priceToBack * sum of probs for all runners on market*/
	RUNNER_PRICETOBACK_WEIGHT("runner.priceToBackWeight"),
	/** 1 - 100%, 0 - 0% */
	RUNNER_PRICETOBACK_PROB_WEIGHT("runner.priceToBackProbWeight"),
	
	RUNNER_TOTALAMOUNT_MATCHED("runner.totalMatched"),
	RUNNER_LASTPRICE_MATCHED("runner.lastPriceMatched"),
	/** Price linear regression per minute based on the last 15 minutes.*/
	RUNNER_SLOPE("runner.slope"),
	RUNNER_SLOPEERR("runner.slopeErr"),
	
	BWIN_BESTPRICETOBACK("bwinRunner.priceToBack"),
	MASSEY_PRICE("massey.price"),
	RACINGPOST_PRICE("racingpost.price"),
	ODDSCHECKER_BESTPRICETOBACK("oddschecker.bestPriceToBack"),
	
	/**Database unique id of runner state*/
	RUNNER_STATE_ID("obj.runnerStateId"),
	
	LASTBET_LAY_BETID("lastBetLay.betId"),
	LASTBET_LAY_PRICE("lastBetLay.price"),
	LASTBET_LAY_SIZE("lastBetLay.betSize"),
	LASTBET_LAY_AVG_PRICE_MATCHED("lastBetLay.avgPriceMatched"),
	LASTBET_LAY_SIZE_MATCHED("lastBetLay.sizeMatched"),
	LASTBET_LAY_SIZE_CANCELLED("lastBetLay.sizeCancelled"),
	LASTBET_LAY_PLACED_DATE("lastBetLay.placedDate"),
	LASTBET_LAY_MATCHED_DATE("lastBetLay.matchedDate"),
	
	LASTBET_BACK_BETID("lastBetBack.betId"),
	LASTBET_BACK_PRICE("lastBetBack.price"),
	LASTBET_BACK_SIZE("lastBetBack.betSize"),
	LASTBET_BACK_AVG_PRICE_MATCHED("lastBetBack.avgPriceMatched"),
	LASTBET_BACK_SIZE_MATCHED("lastBetBack.sizeMatched"),
	LASTBET_BACK_SIZE_CANCELLED("lastBetBack.sizeCancelled"),
	LASTBET_BACK_PLACED_DATE("lastBetBack.placedDate"),
	LASTBET_BACK_MATCHED_DATE("lastBetBack.matchedDate"),

	NOW("now");
	
	private final String contextName;

	ContextVariableEnum(String contextName) {
		this.contextName = contextName;	
	}

	public String getName() {
		return contextName;
	}	
}
