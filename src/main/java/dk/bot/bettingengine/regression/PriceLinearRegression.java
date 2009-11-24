package dk.bot.bettingengine.regression;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.apache.commons.math.util.MathUtils;

/**
 * Binary odds regression based on ordinary least squares regression model with one independent
 * variable.
 * 
 * @author daniel
 * 
 */
public class PriceLinearRegression {

	/**
	 * Predict trend based on observations
	 * 
	 * @param observations
	 * @param unit
	 *            Prediction per time unit: y = f(timeUnit), e.g. per second or
	 *            minute.
	 * @return
	 * @throws MathException
	 */
	public static Prediction predict(List<Observation> observations, TimeUnit unit) {

		SimpleRegression regression = new SimpleRegression();

		long x=0;
		double y=Double.NaN;
		for (Observation observation : observations) {
			x = unit.convert(observation.getTimestamp().getTime(), TimeUnit.MILLISECONDS);
			y = 100d/observation
			.getPrice();
			/** Conver binary odds to probability.*/
			regression.addData(x,y);
		}

		Prediction prediction = new Prediction();
		/** Convert slope to degree.*/
		double slope = Math.toDegrees(Math.atan(regression.getSlope()));
		/**Negation - convert slope for probability to slope for binary odds.*/ 
		prediction.setSlope(-slope);
		
		try {
			prediction.setSlopeErr(regression.getSignificance());
		} catch (MathException e) {
			prediction.setSlopeErr(Double.NaN);
		}
		
		prediction.setPredictedValue(100/regression.predict(x));
		prediction.setRealValue(100/y);
		
		return prediction;
	}

}
