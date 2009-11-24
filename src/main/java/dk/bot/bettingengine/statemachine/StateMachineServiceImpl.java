package dk.bot.bettingengine.statemachine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.bettingengine.statemachine.context.MarketRunnerCtxFactory;
import dk.bot.bettingengine.statemachine.context.MarketRunnerCtxFactoryImpl;
import dk.bot.bettingengine.statemachine.executor.VisitedStatesStat;
import dk.bot.bettingengine.statemachine.manager.StateMachineManager;
import dk.bot.bettingengine.statemachine.manager.StateMachineManagerImpl;
import dk.bot.bettingengine.statemachine.manager.StateMachineStat;
import dk.bot.bettingengine.statemachine.manager.runnerstatecache.RunnerStateCache;
import dk.bot.bettingengine.statemachine.manager.runnerstatecache.RunnerStateCacheImpl;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketRunner;

@ManagedResource(objectName = "dk.flexibet.bettingengine:name=StateMachineService")
public class StateMachineServiceImpl implements StateMachineService, StateMachineServiceMBean {

	private final Log log = LogFactory.getLog(StateMachineServiceImpl.class.getSimpleName());

	/** Key - stateMachineId, Value - manager for state machine */
	private Map<String, StateMachineManagerImpl> stateMachineManagersMap = new LinkedHashMap<String, StateMachineManagerImpl>();

	private MarketRunnerCtxFactory marketRunnerCtxFactory = new MarketRunnerCtxFactoryImpl();

	public StateMachineServiceImpl(StateMachineServiceConfig config, BetApi betApi,BettingEngineDAO bettingEngineDao) {

		for (StateMachineInfo stateMachineInfo : config.getStateMachines()) {
			RunnerStateCache runnerStateCache = new RunnerStateCacheImpl(stateMachineInfo.getStateMachineId(),
					stateMachineInfo.getStateMachineUrl(), config.getCustomActions(), bettingEngineDao);
			StateMachineManagerImpl stateMachineManager = new StateMachineManagerImpl(stateMachineInfo.getStateMachineId(),stateMachineInfo
					.getMarketFilters(), runnerStateCache, betApi,bettingEngineDao,
					marketRunnerCtxFactory);

			stateMachineManagersMap.put(stateMachineInfo.getStateMachineId(), stateMachineManager);
		}
	}

	public Set<String> getStateMachineIds() {
		return stateMachineManagersMap.keySet();
	}

	public List<StateMachineResult> fireEvent(Market completeMarket, MarketRunner marketRunner,
			Prediction prediction) {

		List<StateMachineResult> allResults = new ArrayList<StateMachineResult>();

		for (String stateMachineId : stateMachineManagersMap.keySet()) {

			try {
				StateMachineManagerImpl stateMachineManager = stateMachineManagersMap.get(stateMachineId);
				StateMachineResult stateMachineResult = stateMachineManager.fireEvent(completeMarket,
						marketRunner, prediction);
				if (stateMachineResult != null) {
					allResults.add(stateMachineResult);
				}
			} catch (Exception e) {
				log.error("Fire event error", e);
			}
		}

		return allResults;

	}

	public int removeMachines(Date marketTime) {
		int total = 0;
		for (StateMachineManager manager : stateMachineManagersMap.values()) {
			total = total + manager.removeMachines(marketTime);
		}
		return total;
	}

	/**
	 * JMX operations
	 */
	@ManagedAttribute
	public int getMachinesAmount() {
		int total = 0;
		for (StateMachineManager manager : stateMachineManagersMap.values()) {
			total = total + manager.getMachinesAmount();
		}
		return total;
	}

	@ManagedAttribute
	public Map<String,StateMachineStat> getStateMachineStat() {
		/** Key - stateMachineName, Value - statistics for state machine */
		Map<String, StateMachineStat> stat = new HashMap<String, StateMachineStat>();
		for (String stateMachineId : stateMachineManagersMap.keySet()) {
			StateMachineManager stateMachineManager = stateMachineManagersMap.get(stateMachineId);
			stat.put(stateMachineId, stateMachineManager.getVisitedStates());
		}
		return stat;
	}

	@ManagedAttribute
	public List<String> getStateMachines() {
		List<String> stateMachineIdList = new ArrayList<String>();
		for (String stateMachineId : stateMachineManagersMap.keySet()) {
			stateMachineIdList.add(stateMachineId);
		}
		return stateMachineIdList;
	}

	@ManagedOperation
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "stateMachineId", description = "") })
	public List<String> getStates(String stateMachineId) {
		StateMachineManagerImpl stateMachineManager = stateMachineManagersMap.get(stateMachineId);
		if (stateMachineManager != null) {
			return stateMachineManager.getStates();
		} else {
			throw new IllegalArgumentException("State machine not found: " + stateMachineId);
		}
	}

}
