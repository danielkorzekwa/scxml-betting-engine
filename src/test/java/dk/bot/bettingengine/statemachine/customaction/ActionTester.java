package dk.bot.bettingengine.statemachine.customaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.env.SimpleDispatcher;
import org.apache.commons.scxml.env.SimpleErrorReporter;
import org.apache.commons.scxml.env.jexl.JexlContext;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;
import org.apache.commons.scxml.model.Transition;
import org.jmock.Expectations;
import org.jmock.Mockery;

import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.bettingengine.statemachine.context.MarketRunnerCtxFactoryImpl;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketDetailsRunner;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.MarketRunners;
import dk.bot.marketobserver.model.RunnerPrice;
import dk.bot.marketobserver.tasks.bwin.BetFairBwinRegionEnum;
import dk.bot.marketobserver.tasks.bwin.BwinMarketPrices;
import dk.bot.oddschecker.model.HorseWinMarket;

/**
 * Allows testing custom actions.
 * 
 * @author daniel
 * 
 */
public class ActionTester {

	/**
	 * Executes the action.execute(...) method.
	 * 
	 * @param action
	 * @param ctxParams
	 *            Are set in the action context before executing action.
	 * @throws ModelException
	 */
	public static void executeAction(AbstractAction action, Map<String, Object> ctxParams) throws ModelException {
		SCXML scxml = new SCXML();

		State initial = new State();
		initial.setId("initial");
		Transition transition = new Transition();
		action.setParent(transition);
		transition.addAction(action);
		initial.addTransition(transition);
		scxml.setInitialTarget(initial);

		SCXMLExecutor executor = new SCXMLExecutor(new JexlEvaluator(), new SimpleDispatcher(),
				new SimpleErrorReporter());
		executor.setStateMachine(scxml);
		executor.setSuperStep(false);

		Context context = getDefaultContext();
		addToContext(context, ctxParams);
		executor.setRootContext(context);

		executor.go();
		executor.triggerEvent(new TriggerEvent("event", TriggerEvent.SIGNAL_EVENT, null));

	}

	private static Context getDefaultContext() {
		JexlContext ctx = new JexlContext();

		MarketData market = new MarketData();
		market.setMarketId(1);
		market.setSuspendTime(new Date(0));
		market.setRunners(new ArrayList<MarketDetailsRunner>());
		Market compositeMarket = new Market(market, BetFairBwinRegionEnum.SOCCER_ARGENTINA,
				new BwinMarketPrices(market.getMarketId(),new HashMap<Integer, Double>()), new HorseWinMarket());
			
		MarketRunner marketRunner = new MarketRunner(2,0,0,0,0,0,new ArrayList<RunnerPrice>());

		MarketRunners marketRunners = new MarketRunners(1,new ArrayList<MarketRunner>(),0,new Date(0));
		marketRunners.getMarketRunners().add(marketRunner);
		compositeMarket.setMarketRunners(marketRunners);
		compositeMarket.setMarketMUBets(new ArrayList<MUBet>());
		
		RunnerBet runnerBetLay = new RunnerBet();
		runnerBetLay.setPlacedDate(new Date(0));

		RunnerBet runnerBetBack = new RunnerBet();
		runnerBetBack.setPlacedDate(new Date(0));

		Mockery mockery = new Mockery();
		final BettingEngineDAO bettingEngineDao = mockery.mock(BettingEngineDAO.class); 
		mockery.checking(new Expectations() {
			{
			allowing(bettingEngineDao).findLastBet(with(any(Integer.class)), with(any(String.class)));
			}
		});
		addToContext(ctx, new MarketRunnerCtxFactoryImpl().createContext(1,compositeMarket, marketRunner, new Prediction(Double.NaN,Double.NaN),null, bettingEngineDao,0));

		return ctx;
	}

	private static void addToContext(Context ctx, Map<String, Object> params) {
		for (String paramName : params.keySet()) {
			ctx.getVars().put(paramName, params.get(paramName));
		}
	}

}
