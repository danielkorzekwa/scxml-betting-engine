package dk.bot.bettingengine;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.statemachine.StateMachineServiceConfig;

/**Creates a betting engine bean.
 * 
 * @author daniel
 *
 */
public class BettingEngineFactoryBean implements FactoryBean{

	private final BetApi betApi;
	private final StateMachineServiceConfig stateMachineServiceConfig;
	private final BettingEngineDAO bettingEngineDao;

	public BettingEngineFactoryBean(BetApi betApi, BettingEngineDAO bettingEngineDao,
			StateMachineServiceConfig stateMachineServiceConfig) {
				this.betApi = betApi;
				this.bettingEngineDao = bettingEngineDao;
				this.stateMachineServiceConfig = stateMachineServiceConfig;
	}
	
	public Object getObject() throws Exception {
		ClassPathXmlApplicationContext baseContext = new ClassPathXmlApplicationContext(new String[] {});
		baseContext.getBeanFactory().registerSingleton("betApi", betApi);
		baseContext.getBeanFactory().registerSingleton("bettingEngineDao", bettingEngineDao);
		baseContext.getBeanFactory().registerSingleton("stateMachineServiceConfig", stateMachineServiceConfig);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring-bettingengine.xml",
		}, baseContext);
		
		BettingEngine bettingEngine = (BettingEngine)context.getBean("bettingEngine");
		return bettingEngine;
	}

	public Class getObjectType() {
		return BettingEngineImpl.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
