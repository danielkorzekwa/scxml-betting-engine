package dk.bot.bettingengine.statemachine.manager.runnerstatecache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.scxml.model.CustomAction;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.dao.model.RunnerState;
import dk.bot.bettingengine.statemachine.executor.StateMachineExecutor;
import dk.bot.bettingengine.statemachine.manager.RunnerStateMachine;
import dk.bot.bettingengine.statemachine.manager.StateMachineKey;

@RunWith(JMock.class)
public class RunnerStateCacheImplTest {

	private RunnerStateCache runnerStateCache;
	
	private Mockery mockery = new JUnit4Mockery();
	private BettingEngineDAO bettingEngineDao = mockery.mock(BettingEngineDAO.class);
	
	private URL stateMachineUrl;
	
	
	@Before
	public void setUp() throws MalformedURLException {
		stateMachineUrl = new File("src/test/resources/conf/statemachine/testmachine.scxml").toURI().toURL();
		runnerStateCache = new RunnerStateCacheImpl("default",stateMachineUrl, new ArrayList<CustomAction>(),bettingEngineDao);
	}
	@Test
	public void testGetNotExistInMemNotExistInDb() {
		final Sequence sequence = mockery.sequence("sequence");

		final RunnerState runnerState = new RunnerState();
		runnerState.setStateName("layPlaced");
		runnerState.setId(100);

		mockery.checking(new Expectations() {
			{
				exactly(1).of(bettingEngineDao).findRunnerState("default", 10, 1,"layPlaced");
				will(returnValue(runnerState));
			}
		});

		StateMachineKey machineKey = new StateMachineKey(10,1,new Date(100));
		RunnerStateMachine runnerStateMachine = runnerStateCache.get(machineKey);
		
		assertNotNull(runnerStateMachine);
		assertEquals(100, runnerStateMachine.getRunnerStateId());
		assertEquals("layPlaced", runnerStateMachine.getStateMachine().getCurrentStateId());
	}

	@Test
	public void testGetNotExistInMemExistInDb() {
		
		final RunnerState runnerState = new RunnerState();
		runnerState.setStateName("noBets");
		runnerState.setId(100);

		mockery.checking(new Expectations() {
			{
				exactly(1).of(bettingEngineDao).findRunnerState("default", 10, 1,"layPlaced");
				will(returnValue(runnerState));
			}
		});

		StateMachineKey machineKey = new StateMachineKey(10,1,new Date(100));
		RunnerStateMachine runnerStateMachine = runnerStateCache.get(machineKey);
		
		assertNotNull(runnerStateMachine);
		assertEquals(100, runnerStateMachine.getRunnerStateId());
		assertEquals("noBets", runnerStateMachine.getStateMachine().getCurrentStateId());	
	}
	
	@Test
	public void testGetExistInMem() {
		
		RunnerStateMachine runnerStateMachine = new RunnerStateMachine(100,new StateMachineExecutor(stateMachineUrl, new ArrayList<CustomAction>()));
		
		StateMachineKey machineKey = new StateMachineKey(10,1,new Date(100));
		
		/**put machine to the cache*/
		runnerStateCache.put(machineKey, runnerStateMachine);
		
		/**Get from cache and assert*/
		RunnerStateMachine fromCache = runnerStateCache.get(machineKey);
		
		assertNotNull(fromCache);
		assertEquals(100, runnerStateMachine.getRunnerStateId());
	}
	
	@Test
	public void testRemoveMachines() throws MalformedURLException {

		/**put machines to the cache*/
		RunnerStateMachine runnerStateMachine = new RunnerStateMachine(100,new StateMachineExecutor(stateMachineUrl, new ArrayList<CustomAction>()));
		
		StateMachineKey machineKey = new StateMachineKey(10,1,new Date(1000 * 3600 * 4));
		runnerStateCache.put(machineKey, runnerStateMachine);
	
		machineKey = new StateMachineKey(10,2,new Date(1000 * 3600 * 23));
		runnerStateCache.put(machineKey, runnerStateMachine);
	
		machineKey = new StateMachineKey(11,1,new Date(1000 * 3600 * 25));
		runnerStateCache.put(machineKey, runnerStateMachine);
	
		machineKey = new StateMachineKey(11,2,new Date(1000 * 3600 * 26));
		runnerStateCache.put(machineKey, runnerStateMachine);
	
		assertEquals(4, runnerStateCache.getMachinesAmount());

		/** Remove machines from cache and check machines amount.*/
		assertEquals(2, runnerStateCache.removeMachines(new Date(1000 * 3600 * 24)));
		assertEquals(2, runnerStateCache.getMachinesAmount());
	}
	
	@Test
	public void testGetStates() {
		List<String> states = runnerStateCache.getStates();
		
		assertEquals(4,states.size());
		assertEquals("noBets",states.get(0));
		assertEquals("layPlaced",states.get(1));
		assertEquals("layMatched",states.get(2));
		assertEquals("backPlaced",states.get(3));
		
	}
}
