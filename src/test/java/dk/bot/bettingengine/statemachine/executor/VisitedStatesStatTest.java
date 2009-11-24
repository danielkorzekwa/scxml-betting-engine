package dk.bot.bettingengine.statemachine.executor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VisitedStatesStatTest {

	@Test
	public void testAccumulate() {
		VisitedStatesStat visitedStatesStat = new VisitedStatesStat();
		visitedStatesStat.put("state1", getStateStat("state1"));
		
		VisitedStatesStat visitedStatesStat2 = new VisitedStatesStat();
		visitedStatesStat2.put("state1", getStateStat("state1"));
		
		assertEquals(1,visitedStatesStat.get("state1").getVisits());
		assertEquals(1,visitedStatesStat.get("state1").getTransitionStats().get(0).getExecutions());
	
		visitedStatesStat.accumulate(visitedStatesStat2);
		
		assertEquals(2,visitedStatesStat.get("state1").getVisits());
		assertEquals(2,visitedStatesStat.get("state1").getTransitionStats().get(0).getExecutions());
	}
	
	/**One stat is empty, second, third and fourth have one state. Test emptyStat.acumulate(notEmptyStats 2,3 and 4)*/
	@Test
	public void testAccumulateToEmpty() {
		VisitedStatesStat emptyStatesStat = new VisitedStatesStat();
		
		VisitedStatesStat visitedStatesStat1 = new VisitedStatesStat();
		visitedStatesStat1.put("state1", getStateStat("state1"));
		VisitedStatesStat visitedStatesStat2 = new VisitedStatesStat();
		visitedStatesStat2.put("state1", getStateStat("state1"));
		VisitedStatesStat visitedStatesStat3 = new VisitedStatesStat();
		visitedStatesStat3.put("state2", getStateStat("state2"));
		
		
		assertEquals(null,emptyStatesStat.get("state1"));
		
		assertEquals(1,visitedStatesStat1.get("state1").getVisits());
		assertEquals(1,visitedStatesStat1.get("state1").getTransitionStats().get(0).getExecutions());
		
		assertEquals(1,visitedStatesStat2.get("state1").getTransitionStats().get(0).getExecutions());
		assertEquals(1,visitedStatesStat2.get("state1").getTransitionStats().get(0).getExecutions());
		
		assertEquals(1,visitedStatesStat3.get("state2").getTransitionStats().get(0).getExecutions());
		assertEquals(1,visitedStatesStat3.get("state2").getTransitionStats().get(0).getExecutions());
		
		
		emptyStatesStat.accumulate(visitedStatesStat1);
		emptyStatesStat.accumulate(visitedStatesStat2);
		emptyStatesStat.accumulate(visitedStatesStat3);
		
		
		assertEquals(2,emptyStatesStat.getStateStats().size());
		assertEquals(2,emptyStatesStat.get("state1").getVisits());
		assertEquals(2,emptyStatesStat.get("state1").getTransitionStats().get(0).getExecutions());
		assertEquals(1,emptyStatesStat.get("state1").getTransitionStats().size());
		
		assertEquals(1,visitedStatesStat1.get("state1").getVisits());
		assertEquals(1,visitedStatesStat1.get("state1").getTransitionStats().get(0).getExecutions());
		assertEquals(1,visitedStatesStat1.get("state1").getTransitionStats().size());
		
		
		assertEquals(1,visitedStatesStat2.get("state1").getTransitionStats().get(0).getExecutions());
		assertEquals(1,visitedStatesStat2.get("state1").getTransitionStats().get(0).getExecutions());
		assertEquals(1,visitedStatesStat2.get("state1").getTransitionStats().size());
		
	}
	
	private StateStat getStateStat(String stateName) {
		List<TransitionStat> transitions = new ArrayList<TransitionStat>();
		TransitionStat transStat = new TransitionStat("target1","condition");
		transStat.addExecution();
		transitions.add(transStat );
		
		StateStat stateStat = new StateStat(stateName,transitions );
		stateStat.addVisit();		
		
		return stateStat;
	}
}
