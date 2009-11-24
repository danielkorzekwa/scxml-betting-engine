package dk.bot.bettingengine.statemachine.customaction;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.Evaluator;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.statemachine.context.ContextObjectEnum;
import dk.bot.bettingengine.statemachine.context.ContextVariableEnum;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketRunner;

/**
 * Wrapper for CustomAction, add a simple way to evaluate expressions and hide
 * scxml details
 * 
 * @author daniel
 * 
 */
public abstract class AbstractAction extends Action {

	private final Log log = LogFactory.getLog(AbstractAction.class.getSimpleName());

	/** see ContextEnum */
	private Context context;

	/** evaluates scxml expressions */
	private ExprEval eval;

	public AbstractAction() {
		super();
	}

	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep, SCInstance scInstance, Log appLog,
			Collection derivedEvents) throws ModelException, SCXMLExpressionException {

		context = scInstance.getContext(getParentState());

		eval = new ExprEval(context, scInstance.getEvaluator());

		execute();
	}

	/**
	 * 
	 * @param eval
	 * 
	 */
	protected abstract void execute() throws SCXMLExpressionException;

	protected Context getContext() {
		return context;
	}

	protected ExprEval getExprEval() {
		return eval;
	}

	protected MarketData getMarket() {
		return (MarketData) context.get(ContextObjectEnum.MARKET.getName());
	}
	
	protected MarketRunner getMarketRunner() {
		return (MarketRunner) context.get(ContextObjectEnum.RUNNER.getName());
	}

	protected BetApi getBetApi() {
		return (BetApi) context.get(ContextObjectEnum.BET_API.getName());
	}

	protected BettingEngineDAO getBettingEngineDao() {
		return (BettingEngineDAO) context.get(ContextObjectEnum.BETTING_ENGINE_DAO.getName());
	}

	protected int getRunnerStateId() {
		return (Integer)context.get(ContextVariableEnum.RUNNER_STATE_ID.getName());
	}
	
	/**
	 * Evaluate expressions, e.g. Jexml expressions
	 * 
	 * @author daniel
	 * 
	 */
	public class ExprEval {

		private final Context ctx;
		private final Evaluator eval;

		public ExprEval(Context ctx, Evaluator eval) {
			this.ctx = ctx;
			this.eval = eval;
		}

		public Object eval(String expression) throws SCXMLExpressionException {
			return eval.eval(ctx, expression);
		}
	}

}
