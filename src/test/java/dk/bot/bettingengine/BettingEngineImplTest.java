package dk.bot.bettingengine;

import java.util.ArrayList;

import org.apache.commons.scxml.model.CustomAction;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.statemachine.StateMachineInfo;
import dk.bot.bettingengine.statemachine.StateMachineServiceConfig;

@RunWith(JMock.class)
public class BettingEngineImplTest {

	Mockery context = new JUnit4Mockery();
	
	private BetApi betApi = context.mock(BetApi.class);
	private BettingEngineDAO bettingEngineDao = context.mock(BettingEngineDAO.class);
	
	@Test
	public void test() throws Exception {
		
		BettingEngineFactoryBean bettingEngineFactoryBean = new BettingEngineFactoryBean(betApi,bettingEngineDao, new StateMachineServiceConfig(new ArrayList<StateMachineInfo>(),new ArrayList<CustomAction>()));
		bettingEngineFactoryBean.getObject();
		
		Thread.sleep(3000);
	}

}
