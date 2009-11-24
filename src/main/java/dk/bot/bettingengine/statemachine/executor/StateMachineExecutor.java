package dk.bot.bettingengine.statemachine.executor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.SCXMLListener;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.env.SimpleDispatcher;
import org.apache.commons.scxml.env.SimpleErrorHandler;
import org.apache.commons.scxml.env.SimpleErrorReporter;
import org.apache.commons.scxml.env.jexl.JexlContext;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.CustomAction;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;
import org.apache.commons.scxml.model.Transition;
import org.apache.commons.scxml.model.TransitionTarget;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import dk.bot.marketobserver.util.BotException;

/**
 * ScXml based state machine.
 * 
 * Defines bot's intelligence, what and when has to be done, e.g. when to place
 * or cancel bet. Read about ScXml standard for more details.
 * 
 * @author daniel
 * 
 */
public class StateMachineExecutor {

	private final Log log = LogFactory.getLog(StateMachineExecutor.class.getSimpleName());
	
	private SCXML scxml = null;
	private SCXMLExecutor engine = null;

	private String currentStateId = null;

	/**
	 * How many times all states have been visited and how many times
	 * transitions have been executed
	 * 
	 * key - stateName
	 */
	private VisitedStatesStat statesStats;

	/**
	 * 
	 * @param url
	 * @param customActions
	 *            e.g. placeBet, cancelBet
	 */
	public StateMachineExecutor(URL url, java.util.List<CustomAction> customActions) {
		this.scxml = parseScXmlConfig(url, customActions);
		initScXmlExecutor();
	}

	public StateMachineExecutor(SCXML scxml) {
		this.scxml = scxml;
		initScXmlExecutor();
	}

	private SCXML parseScXmlConfig(URL url, java.util.List<CustomAction> customActions) {
		SCXML scxmlConfig = null;

		try {
			ErrorHandler errHandler = new SimpleErrorHandler();
			scxmlConfig = SCXMLParser.parse(url, errHandler, customActions);
		} catch (IOException ioe) {
			throw new BotException("Error parsing scxml document", ioe);
		} catch (SAXException se) {
			throw new BotException("Error parsing scxml document", se);
		} catch (ModelException me) {
			throw new BotException("Error parsing scxml document", me);
		}

		if (scxmlConfig == null) {
			throw new BotException("Error parsing scxml document");
		}

		return scxmlConfig;
	}

	private void initScXmlExecutor() {
		engine = new SCXMLExecutor(new JexlEvaluator(), new SimpleDispatcher(), new SimpleErrorReporter());
		engine.setStateMachine(scxml);
		engine.setSuperStep(false);
		engine.setRootContext(new JexlContext());
		engine.addListener(scxml, new EntryListener());

		/** init state statistics */
		statesStats = new VisitedStatesStat();
		for (Object stateName : scxml.getChildren().keySet()) {
			State state = (State) scxml.getChildren().get(stateName);

			List<TransitionStat> transitionStats = new ArrayList<TransitionStat>();

			for (Object object : state.getTransitionsList()) {
				Transition transition = (Transition) object;
				String target = state.getId();
				if (transition.getTargets().size() > 0) {
					target = ((TransitionTarget) transition.getTargets().get(0)).getId();
				}
				TransitionStat transitionStat = new TransitionStat(target, transition.getCond());
				transitionStats.add(transitionStat);
			}

			statesStats.put(state.getId(), new StateStat(state.getId(), transitionStats));
		}

		try {
			engine.go();
		} catch (ModelException me) {
			throw new BotException("Error starting state machine", me);
		}
	}

	public String getCurrentStateId() {
		return currentStateId;
	}

	public boolean isFinalState() {
		return engine.getCurrentStatus().isFinal();
	}

	public boolean isInitial() {
		if (currentStateId.equals(scxml.getInitialstate())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Fire an event on the SCXML engine.
	 * 
	 * @param event
	 *            The event name.
	 * @param context
	 *            put into the engine root context, key - name of context, value
	 *            context's object
	 * @return Whether the state machine has reached a &quot;final&quot;
	 *         configuration.
	 */
	public boolean fireEvent(final String event, Map<String, Object> context) {

		/** add plugin context */
		if (context != null) {
			for (String contextName : context.keySet()) {
				engine.getRootContext().getVars().put(contextName, context.get(contextName));
			}
		}

		TriggerEvent evt = new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT, null);
		try {
			engine.triggerEvent(evt);
		} catch (ModelException me) {
			throw new BotException("Event triggering error", me);
		}

		return engine.getCurrentStatus().isFinal();
	}

	/**
	 * A SCXMLListener that is only concerned about &quot;onentry&quot;
	 * notifications.
	 */
	protected class EntryListener implements SCXMLListener {

		/**
		 * {@inheritDoc}
		 */
		public void onEntry(final TransitionTarget entered) {
			currentStateId = entered.getId();
			StateStat stateStat = statesStats.get(entered.getId());
			if (stateStat != null) {
				stateStat.addVisit();
			} else {
				log.error("State stat not found");
			}

		}

		/**
		 * No-op.
		 * 
		 * @param from
		 *            The &quot;source&quot; transition target.
		 * @param to
		 *            The &quot;destination&quot; transition target.
		 * @param transition
		 *            The transition being followed.
		 */
		public void onTransition(final TransitionTarget from, final TransitionTarget to, final Transition transition) {

			StateStat stateStat = statesStats.get(from.getId());
			if (stateStat != null) {
				TransitionStat transitionStat = stateStat.getTransitionStat(to.getId(), transition.getCond());
				if (transitionStat != null) {
					transitionStat.addExecution();
				} else {
					log.error("Transition not found");
				}
			} else {
				log.error("State stat not found");
			}
		}

		/**
		 * No-op.
		 * 
		 * @param exited
		 *            The transition target being exited.
		 */
		public void onExit(final TransitionTarget exited) {
			// nothing to do
		}

	}
	
	public VisitedStatesStat getVisitedStates() {
		return statesStats;
	}

	public void reset() throws ModelException {
		engine.reset();
	}

}
