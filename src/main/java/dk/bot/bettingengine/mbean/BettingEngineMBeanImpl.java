package dk.bot.bettingengine.mbean;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import dk.bot.bettingengine.BettingEngine;
import dk.bot.bettingengine.regression.Prediction;
import dk.bot.bettingengine.regression.RegressionCache;
import dk.bot.bettingengine.regression.RegressionCacheInfo;

@ManagedResource(objectName = "dk.flexibet.bettingengine:name=BettingEngine")
public class BettingEngineMBeanImpl implements BettingEngineMBean {

	@Resource
	private BettingEngine bettingEngine;

	@Resource
	private RegressionCache regressionCache;

	@ManagedOperation
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "marketId", description = "") })
	public Map<Integer, Prediction> getRunnerPricePredictions(int marketId) {
		return bettingEngine.getRunnerPricePredictions(marketId);
	}

	@ManagedAttribute
	public RegressionCacheInfo getRegressionCacheInfo() {
		return regressionCache.getCacheInfo();
	}
}
