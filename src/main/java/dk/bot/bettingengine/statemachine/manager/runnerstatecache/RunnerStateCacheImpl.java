package dk.bot.bettingengine.statemachine.manager.runnerstatecache;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.scxml.env.SimpleErrorHandler;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.CustomAction;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.dao.model.RunnerState;
import dk.bot.bettingengine.statemachine.executor.StateMachineExecutor;
import dk.bot.bettingengine.statemachine.executor.VisitedStatesStat;
import dk.bot.bettingengine.statemachine.manager.RunnerStateMachine;
import dk.bot.bettingengine.statemachine.manager.StateMachineKey;
import dk.bot.bettingengine.statemachine.manager.StateMachineStat;
import dk.bot.marketobserver.util.BotException;

/**
 * Cache for runner state machines. Machines are persisted in database.
 * 
 * @author daniel
 * 
 */
public class RunnerStateCacheImpl implements RunnerStateCache {

	private final Log log = LogFactory.getLog(RunnerStateCacheImpl.class.getSimpleName());

	private Map<StateMachineKey, RunnerStateMachine> machines = new HashMap<StateMachineKey, RunnerStateMachine>();

	private SCXML initialStateScxml = null;
	private String initialStateName;
	/** SCXML objects for machine states. Key - stateName */
	private Map<String, SCXML> scxmlMap = new HashMap<String, SCXML>();

	private final List<CustomAction> customActions;

	private final URL stateMachineUrl;

	private final String stateMachineId;

	private final BettingEngineDAO bettingEngineDao;

	public RunnerStateCacheImpl(String stateMachineId, URL stateMachineUrl, List<CustomAction> customActions,
			BettingEngineDAO bettingEngineDao) {
		this.stateMachineId = stateMachineId;
		this.stateMachineUrl = stateMachineUrl;
		this.customActions = customActions;
		this.bettingEngineDao = bettingEngineDao;

		this.initialStateScxml = parseScxmlUrl(stateMachineUrl, customActions);
		this.initialStateName = initialStateScxml.getInitialstate();
		scxmlMap.put(initialStateName, initialStateScxml);
	}

	public RunnerStateMachine get(StateMachineKey machineKey) {
		RunnerStateMachine machine = machines.get(machineKey);

		if (machine == null) {
			RunnerState state = bettingEngineDao.findRunnerState(stateMachineId, machineKey.getMarketId(), machineKey
					.getSelectionId(), initialStateName);
			try {
				SCXML scxml = scxmlMap.get(state.getStateName());
				if (scxml == null) {
					scxml = createSCXMLConfig(state.getStateName());
					scxmlMap.put(state.getStateName(), scxml);
				}
				machine = new RunnerStateMachine(state.getId(), new StateMachineExecutor(scxml));
			} catch (ModelException e) {
				log.error("Can't create state machine", e);
				throw new RunnerStateCacheException("Can't create state machine", e);
			}
			machines.put(machineKey, machine);
		}
		return machine;
	}

	public void put(StateMachineKey machineKey, RunnerStateMachine machine) {
		machines.put(machineKey, machine);
	}

	public int getMachinesAmount() {
		return machines.size();
	}

	public int removeMachines(Date marketTime) {

		List<StateMachineKey> toRemove = new ArrayList<StateMachineKey>();
		for (StateMachineKey machineKey : machines.keySet()) {
			if (machineKey.getMarketTime().getTime() < marketTime.getTime()) {
				toRemove.add(machineKey);
			}
		}

		for (StateMachineKey stateMachineKey : toRemove) {
			machines.remove(stateMachineKey);
		}
		return toRemove.size();
	}

	public StateMachineStat getVisitedStates() {
		StateMachineStat stateMachineStat = new StateMachineStat();

		/** Set visited states stat. */
		VisitedStatesStat visitedStatesStat = new VisitedStatesStat();
		for (RunnerStateMachine machine : machines.values()) {
			visitedStatesStat.accumulate(machine.getStateMachine().getVisitedStates());
		}
		stateMachineStat.setVisitedStatesStat(visitedStatesStat);

		/** Set current state stat.Key - state name, value - number of state machine instances in a given state. */
		Map<String, Integer> currentStates = new HashMap<String, Integer>();
		for (RunnerStateMachine machine : machines.values()) {
			String currentStateId = machine.getStateMachine().getCurrentStateId();
			Integer numOfStates = currentStates.get(currentStateId);
			if (numOfStates == null) {
				numOfStates = 0;
			}
			numOfStates++;
			currentStates.put(currentStateId, numOfStates);
		}
		stateMachineStat.setCurrentStates(currentStates);
		return stateMachineStat;
	}

	public List<String> getStates() {

		List<String> stateNames = new ArrayList<String>();
		for (Object stateName : this.initialStateScxml.getChildren().keySet()) {
			stateNames.add((String) stateName);
		}
		return stateNames;
	}

	private SCXML createSCXMLConfig(String initialState) throws ModelException {
		SCXML specificScxml = parseScxmlUrl(stateMachineUrl, customActions);
		specificScxml.setInitialstate(initialState);
		SCXMLParser.updateSCXML(specificScxml);
		return specificScxml;
	}

	private SCXML parseScxmlUrl(URL scxmlUrl, List<CustomAction> customActions) {
		SCXML scxml = null;
		try {
			ErrorHandler errHandler = new SimpleErrorHandler();
			scxml = SCXMLParser.parse(scxmlUrl, errHandler, customActions);
			return scxml;
		} catch (IOException ioe) {
			throw new BotException("Error parsing scxml document", ioe);
		} catch (SAXException se) {
			throw new BotException("Error parsing scxml document", se);
		} catch (ModelException me) {
			throw new BotException("Error parsing scxml document", me);
		}
	}

}
