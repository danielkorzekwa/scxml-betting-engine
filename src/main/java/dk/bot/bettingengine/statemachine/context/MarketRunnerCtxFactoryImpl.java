package dk.bot.bettingengine.statemachine.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.joda.time.DateTimeUtils;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.marketobserver.model.BetStatus;
import dk.bot.marketobserver.model.BetType;
import dk.bot.marketobserver.model.MUBets;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.tasks.bwin.BetFairBwinRegionEnum;

/**
 * Creates market runner context
 * 
 * @author daniel
 * 
 */
public class MarketRunnerCtxFactoryImpl implements MarketRunnerCtxFactory {

	/**
	 * Create context which is used from Scxml state machine, e.g. in condition to check current odds
	 * 
	 * @return
	 * @throws BetFairException
	 */
	public Map<String, Object> createContext(int runnerStateId, Market compositeMarket, MarketRunner runner,Prediction prediction,
			BetApi betApi, BettingEngineDAO bettingEngineDao,
			long now) {

		HashMap<String, Object> scCtx = new HashMap<String, Object>();

		/** put object context - used by custom actions */
		scCtx.put(ContextObjectEnum.MARKET.getName(), compositeMarket.getMarketData());
		scCtx.put(ContextObjectEnum.RUNNER.getName(), runner);
		scCtx.put(ContextObjectEnum.BET_API.getName(), betApi);
		scCtx.put(ContextObjectEnum.BETTING_ENGINE_DAO.getName(), bettingEngineDao);

		/** put variable context - used by transitions */
		scCtx.put(ContextVariableEnum.MARKET_ISGREEN.getName(), MarketRunnerCtxHelper.isMarketGreen(compositeMarket
				.getMarketRunners().getMarketRunners(), compositeMarket.getMarketMUBets()));
		scCtx.put(ContextVariableEnum.MARKET_NAME.getName(), compositeMarket.getMarketData()
				.getMarketName());
		scCtx.put(ContextVariableEnum.MARKET_ISBSPMARKET.getName(), compositeMarket.getMarketData()
				.isBsbMarket());
		scCtx.put(ContextVariableEnum.MARKET_ISTURNING_INPLAY.getName(), compositeMarket.getMarketData()
				.isTurningInPlay());
		scCtx
				.put(ContextVariableEnum.MARKET_INPLAYDELAY.getName(), compositeMarket.getMarketRunners()
						.getInPlayDelay());
		scCtx.put(ContextVariableEnum.MARKET_TOTALAMOUNT_MATCHED.getName(), compositeMarket
				.getMarketTotalAmountMatched());
		scCtx.put(ContextVariableEnum.MARKET_DETAILS_SUSPEND_TIME.getName(), compositeMarket.getMarketData().getSuspendTime().getTime());

		if (compositeMarket.getRegion() != null) {
			scCtx.put(ContextVariableEnum.MARKET_EVENTPATH.getName(), compositeMarket.getRegion()
					.getBetFairEvent());
		} else {
			scCtx.put(ContextVariableEnum.MARKET_EVENTPATH.getName(), "unknown");
		}

		scCtx.put(ContextVariableEnum.RUNNER_STATE_ID.getName(), runnerStateId);

		RunnerBet runnerLastBetLay = bettingEngineDao.findLastBet(runnerStateId, BetType.L.value());
		RunnerBet runnerLastBetBack = bettingEngineDao.findLastBet(runnerStateId, BetType.B.value());

		if (runnerLastBetLay != null) {
			scCtx.put(ContextVariableEnum.LASTBET_LAY_BETID.getName(), runnerLastBetLay.getBetId());
			scCtx.put(ContextVariableEnum.LASTBET_LAY_PLACED_DATE.getName(), runnerLastBetLay.getPlacedDate().getTime());
			scCtx.put(ContextVariableEnum.LASTBET_LAY_MATCHED_DATE.getName(), runnerLastBetLay.getMatchedDate().getTime());
			scCtx.put(ContextVariableEnum.LASTBET_LAY_PRICE.getName(), runnerLastBetLay.getPrice());
			scCtx.put(ContextVariableEnum.LASTBET_LAY_SIZE.getName(), runnerLastBetLay.getSize());
			scCtx.put(ContextVariableEnum.LASTBET_LAY_AVG_PRICE_MATCHED.getName(), runnerLastBetLay
					.getAvgPriceMatched());
			scCtx.put(ContextVariableEnum.LASTBET_LAY_SIZE_MATCHED.getName(), runnerLastBetLay.getSizeMatched());
			scCtx.put(ContextVariableEnum.LASTBET_LAY_SIZE_CANCELLED.getName(), runnerLastBetLay.getSizeCancelled());
		} else {
			scCtx.put(ContextVariableEnum.LASTBET_LAY_BETID.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_LAY_PLACED_DATE.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_LAY_MATCHED_DATE.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_LAY_PRICE.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_LAY_SIZE.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_LAY_AVG_PRICE_MATCHED.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_LAY_SIZE_MATCHED.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_LAY_SIZE_CANCELLED.getName(), "no lastBet");

		}

		if (runnerLastBetBack != null) {
			scCtx.put(ContextVariableEnum.LASTBET_BACK_BETID.getName(), runnerLastBetBack.getBetId());
			scCtx.put(ContextVariableEnum.LASTBET_BACK_PLACED_DATE.getName(), runnerLastBetBack.getPlacedDate().getTime());
			scCtx.put(ContextVariableEnum.LASTBET_BACK_MATCHED_DATE.getName(), runnerLastBetBack.getMatchedDate().getTime());
			scCtx.put(ContextVariableEnum.LASTBET_BACK_PRICE.getName(), runnerLastBetBack.getPrice());
			scCtx.put(ContextVariableEnum.LASTBET_BACK_SIZE.getName(), runnerLastBetBack.getSize());
			scCtx.put(ContextVariableEnum.LASTBET_BACK_AVG_PRICE_MATCHED.getName(), runnerLastBetBack
					.getAvgPriceMatched());
			scCtx.put(ContextVariableEnum.LASTBET_BACK_SIZE_MATCHED.getName(), runnerLastBetBack.getSizeMatched());
			scCtx.put(ContextVariableEnum.LASTBET_BACK_SIZE_CANCELLED.getName(), runnerLastBetBack.getSizeCancelled());
		} else {
			scCtx.put(ContextVariableEnum.LASTBET_BACK_BETID.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_BACK_PLACED_DATE.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_BACK_MATCHED_DATE.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_BACK_PRICE.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_BACK_SIZE.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_BACK_AVG_PRICE_MATCHED.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_BACK_SIZE_MATCHED.getName(), "no lastBet");
			scCtx.put(ContextVariableEnum.LASTBET_BACK_SIZE_CANCELLED.getName(), "no lastBet");

		}

		/** Use bets on a market runner only to check if runner is green */
		MUBets marketRunnerMBets = new MUBets(compositeMarket.getMarketMUBets()).getMarketRunnerMUBets(
				compositeMarket.getMarketData().getMarketId(), runner.getSelectionId())
				.getBets(BetStatus.M);
		scCtx.put(ContextVariableEnum.RUNNER_ISGREEN.getName(), MarketRunnerCtxHelper.isRunnerGreen(runner
				.getSelectionId(), marketRunnerMBets.getMuBets()));

		String selectionName = compositeMarket.getMarketData().getSelectionName(runner.getSelectionId());
		if (selectionName == null) {
			selectionName = "n/a";
		}
		scCtx.put(ContextVariableEnum.RUNNER_NAME.getName(), selectionName);

		scCtx.put(ContextVariableEnum.RUNNER_PRICETOBACK.getName(), runner.getPriceToBack());
		scCtx.put(ContextVariableEnum.RUNNER_PRICETOBACK_PROB.getName(), 1d / runner.getPriceToBack());
		scCtx.put(ContextVariableEnum.RUNNER_PRICETOLAY.getName(), runner.getPriceToLay());
		scCtx.put(ContextVariableEnum.RUNNER_PRICETOLAY_PROB.getName(), 1d / runner.getPriceToLay());
		scCtx.put(ContextVariableEnum.RUNNER_TOTALAMOUNT_MATCHED.getName(), runner.getTotalAmountMatched());
		scCtx.put(ContextVariableEnum.RUNNER_LASTPRICE_MATCHED.getName(), runner.getLastPriceMatched());
		scCtx.put(ContextVariableEnum.RUNNER_PRICENEARSP.getName(), runner.getNearSP());
		scCtx.put(ContextVariableEnum.RUNNER_PRICENEARSP_PROB.getName(), 1d / runner.getNearSP());
		scCtx.put(ContextVariableEnum.RUNNER_PRICEFARSP.getName(), runner.getFarSP());
		scCtx.put(ContextVariableEnum.RUNNER_PRICEFARSP_PROB.getName(), 1d / runner.getFarSP());

		double runnerPriceToBackWeighted = compositeMarket.getMarketRunners().getRunnerPriceToBackWeighted(
				runner.getSelectionId());
		scCtx.put(ContextVariableEnum.RUNNER_PRICETOBACK_WEIGHT.getName(), runnerPriceToBackWeighted);
		scCtx.put(ContextVariableEnum.RUNNER_PRICETOBACK_PROB_WEIGHT.getName(), 1d / runnerPriceToBackWeighted);

		/** Set runner price prediction for last 15 minutes. */
		if (prediction != null) {
			scCtx.put(ContextVariableEnum.RUNNER_SLOPE.getName(), prediction.getPredictedValue());
			scCtx.put(ContextVariableEnum.RUNNER_SLOPEERR.getName(), prediction.getSlopeErr());
		} else {
			scCtx.put(ContextVariableEnum.RUNNER_SLOPE.getName(), Double.NaN);
			scCtx.put(ContextVariableEnum.RUNNER_SLOPEERR.getName(), Double.NaN);

		}

		if (compositeMarket.getBwinPrices() != null) {
			Double runnerPrice = compositeMarket.getBwinPrices().getRunnerPrice(runner.getSelectionId());
			if (runnerPrice != null) {
				scCtx.put(ContextVariableEnum.BWIN_BESTPRICETOBACK.getName(), runnerPrice);
			} else {
				scCtx.put(ContextVariableEnum.BWIN_BESTPRICETOBACK.getName(), 1.01d);
			}
		} else {
			scCtx.put(ContextVariableEnum.BWIN_BESTPRICETOBACK.getName(), 1.01d);
		}
		if (compositeMarket.getMasseyPrices() != null
				&& compositeMarket.getMasseyPrices().areAllPricesAvailable()) {
			Double masseyPrice = compositeMarket.getMasseyPrices().getMasseyRunnerPrice(
					runner.getSelectionId());
			if (masseyPrice != null) {
				scCtx.put(ContextVariableEnum.MASSEY_PRICE.getName(), masseyPrice);
			} else {
				scCtx.put(ContextVariableEnum.MASSEY_PRICE.getName(), Double.NaN);
			}
		} else {
			scCtx.put(ContextVariableEnum.MASSEY_PRICE.getName(), Double.NaN);
		}
		if (compositeMarket.getRacingPostPrices() != null) {
			Double racingPostPrice = compositeMarket.getRacingPostPrices().getRacingPostRunnerPrice(
					runner.getSelectionId());
			if (racingPostPrice != null) {
				scCtx.put(ContextVariableEnum.RACINGPOST_PRICE.getName(), racingPostPrice);
			} else {
				scCtx.put(ContextVariableEnum.RACINGPOST_PRICE.getName(), Double.NaN);
			}
		} else {
			scCtx.put(ContextVariableEnum.RACINGPOST_PRICE.getName(), Double.NaN);
		}

		Double oddsCheckerPrice = Double.NaN;
		if (oddsCheckerPrice != null) {
			scCtx.put(ContextVariableEnum.ODDSCHECKER_BESTPRICETOBACK.getName(), oddsCheckerPrice);
		} else {
			scCtx.put(ContextVariableEnum.ODDSCHECKER_BESTPRICETOBACK.getName(), 1000);
		}

		scCtx.put(ContextVariableEnum.NOW.getName(), now);

		return scCtx;
	}

	/**
	 * Create context which is used from Scxml state machine, e.g. in condition to check current odds.
	 * 
	 * @return
	 * @throws BetFairException
	 */
	public Map<String, Object> createRandomContext(Random random) {

		HashMap<String, Object> scCtx = new HashMap<String, Object>();

		/** put object context - used by custom actions */
		scCtx.put(ContextObjectEnum.MARKET.getName(), "notSupported");
		scCtx.put(ContextObjectEnum.RUNNER.getName(), "notSupported");
		scCtx.put(ContextObjectEnum.BET_API.getName(), "notSupported");
		scCtx.put(ContextObjectEnum.BETTING_ENGINE_DAO.getName(), "notSupported");

		/** put variable context - used by transitions */
		long now = DateTimeUtils.currentTimeMillis();

		scCtx.put(ContextVariableEnum.MARKET_ISGREEN.getName(), getBoolean(random, 50));
		scCtx.put(ContextVariableEnum.MARKET_TOTALAMOUNT_MATCHED.getName(), random.nextInt(50));
		if (random.nextInt(2) == 0) {
			scCtx.put(ContextVariableEnum.MARKET_NAME.getName(), "To Be Placed");
		} else {
			scCtx.put(ContextVariableEnum.MARKET_NAME.getName(), "Other");

		}
		scCtx.put(ContextVariableEnum.MARKET_INPLAYDELAY.getName(), random.nextInt(2));
		scCtx.put(ContextVariableEnum.MARKET_ISBSPMARKET.getName(), getBoolean(random, 2));
		scCtx.put(ContextVariableEnum.MARKET_ISTURNING_INPLAY.getName(), getBoolean(random, 2));
		scCtx.put(ContextVariableEnum.MARKET_DETAILS_SUSPEND_TIME.getName(), (now - 1000 * 3600 * 24)
				+ (1000 * 60 * random.nextInt(60 * 24 * 2)));
		scCtx.put(ContextVariableEnum.MARKET_EVENTPATH.getName(), BetFairBwinRegionEnum.values()[38 + random
				.nextInt(BetFairBwinRegionEnum.values().length - 38)].getBetFairEvent());

		scCtx.put(ContextVariableEnum.RUNNER_STATE_ID.getName(), 1);

		scCtx.put(ContextVariableEnum.LASTBET_LAY_BETID.getName(), random.nextInt(5));
		scCtx.put(ContextVariableEnum.LASTBET_LAY_PLACED_DATE.getName(), (now - 1000 * 3600 * 24)
				+ (1000 * 60 * random.nextInt(60 * 24 * 1)));
		scCtx.put(ContextVariableEnum.LASTBET_LAY_MATCHED_DATE.getName(), (now - 1000 * 3600 * 24)
				+ (1000 * 60 * random.nextInt(60 * 24 * 1)));
		scCtx.put(ContextVariableEnum.LASTBET_LAY_PRICE.getName(), 1.01f + random.nextInt(2) + random.nextFloat());
		scCtx.put(ContextVariableEnum.LASTBET_LAY_SIZE.getName(), 1.01f + random.nextInt(2) + random.nextFloat());
		scCtx.put(ContextVariableEnum.LASTBET_LAY_AVG_PRICE_MATCHED.getName(), 1.01f + random.nextInt(2)
				+ random.nextFloat());
		scCtx.put(ContextVariableEnum.LASTBET_LAY_SIZE_MATCHED.getName(), random.nextInt(5));
		scCtx.put(ContextVariableEnum.LASTBET_LAY_SIZE_CANCELLED.getName(), random.nextInt(2));

		scCtx.put(ContextVariableEnum.LASTBET_BACK_BETID.getName(), random.nextInt(5));
		scCtx.put(ContextVariableEnum.LASTBET_BACK_PLACED_DATE.getName(), (now - 1000 * 3600 * 24)
				+ (1000 * 60 * random.nextInt(60 * 24 * 1)));
		scCtx.put(ContextVariableEnum.LASTBET_BACK_MATCHED_DATE.getName(), (now - 1000 * 3600 * 24)
				+ (1000 * 60 * random.nextInt(60 * 24 * 1)));
		scCtx.put(ContextVariableEnum.LASTBET_BACK_PRICE.getName(), 1.01f + random.nextInt(2) + random.nextFloat());
		scCtx.put(ContextVariableEnum.LASTBET_BACK_SIZE.getName(), 1.01f + random.nextInt(2) + random.nextFloat());
		scCtx.put(ContextVariableEnum.LASTBET_BACK_AVG_PRICE_MATCHED.getName(), 1.01f + random.nextInt(2)
				+ random.nextFloat());
		scCtx.put(ContextVariableEnum.LASTBET_BACK_SIZE_MATCHED.getName(), random.nextInt(5));
		scCtx.put(ContextVariableEnum.LASTBET_BACK_SIZE_CANCELLED.getName(), random.nextInt(2));

		scCtx.put(ContextVariableEnum.RUNNER_PRICETOBACK.getName(), 1.01f + random.nextInt(2) + random.nextFloat());
		scCtx.put(ContextVariableEnum.RUNNER_PRICETOBACK_PROB.getName(), random.nextFloat());
		scCtx.put(ContextVariableEnum.RUNNER_PRICETOLAY.getName(), 1.01f + random.nextInt(2) + random.nextFloat());
		scCtx.put(ContextVariableEnum.RUNNER_PRICETOLAY_PROB.getName(), random.nextFloat());
		scCtx.put(ContextVariableEnum.RUNNER_TOTALAMOUNT_MATCHED.getName(), random.nextInt(50));
		scCtx.put(ContextVariableEnum.RUNNER_LASTPRICE_MATCHED.getName(), random.nextInt(10));
		scCtx.put(ContextVariableEnum.RUNNER_PRICENEARSP.getName(), 1.01f + random.nextInt(2) + random.nextFloat());
		scCtx.put(ContextVariableEnum.RUNNER_PRICENEARSP_PROB.getName(), random.nextFloat());
		scCtx.put(ContextVariableEnum.RUNNER_PRICEFARSP.getName(), 1.01f + random.nextInt(2) + random.nextFloat());
		scCtx.put(ContextVariableEnum.RUNNER_PRICEFARSP_PROB.getName(), random.nextFloat());

		scCtx.put(ContextVariableEnum.RUNNER_PRICETOBACK_WEIGHT.getName(), 1.01f + random.nextInt(2)
				+ random.nextFloat());
		scCtx.put(ContextVariableEnum.RUNNER_PRICETOBACK_PROB_WEIGHT.getName(), random.nextFloat());

		scCtx.put(ContextVariableEnum.RUNNER_SLOPE.getName(), random.nextInt(100) - 50);
		scCtx.put(ContextVariableEnum.RUNNER_SLOPEERR.getName(), random.nextDouble());

		scCtx.put(ContextVariableEnum.RUNNER_ISGREEN.getName(), getBoolean(random, 50));
		scCtx.put(ContextVariableEnum.RUNNER_NAME.getName(), "Golden star");

		scCtx.put(ContextVariableEnum.BWIN_BESTPRICETOBACK.getName(), 1.01f + random.nextInt(2) + random.nextFloat());
		scCtx.put(ContextVariableEnum.MASSEY_PRICE.getName(), 1.01f + random.nextInt(2) + random.nextFloat());
		scCtx.put(ContextVariableEnum.RACINGPOST_PRICE.getName(), 1.01f + random.nextInt(2) + random.nextFloat());

		scCtx.put(ContextVariableEnum.ODDSCHECKER_BESTPRICETOBACK.getName(), 1.01f + random.nextInt(2)
				+ random.nextFloat());

		scCtx.put(ContextVariableEnum.NOW.getName(), now);

		return scCtx;
	}

	private boolean getBoolean(Random random, int factor) {
		int nextInt = random.nextInt(factor);
		if (nextInt == 0) {
			return true;
		} else {
			return false;
		}
	}

}
