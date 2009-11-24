package dk.bot.bettingengine.regression;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class PriceLinearRegressionTest {

	private List<Observation> observations = new ArrayList<Observation>();
	private long now = System.currentTimeMillis();
	
	
	
	@Test
	public void testPredict45degrees() throws MathException {
		observations.add(new Observation(new Date(now),100d/50d));
		observations.add(new Observation(new Date(now+(1*1000*60)),100d/51d));
		observations.add(new Observation(new Date(now+(2*1000*60)),100d/52d));
		observations.add(new Observation(new Date(now+(3*1000*60)),100d/53d));
		
		Prediction prediction = PriceLinearRegression.predict(observations,TimeUnit.MINUTES);
		assertEquals(-45, prediction.getSlope(),0);
		assertEquals(0, prediction.getSlopeErr(),0);
		assertEquals(1.88, prediction.getRealValue(),0.01);
		assertEquals(1.88, prediction.getPredictedValue(),0.01);
	}
	@Test
	public void testPredictPerSeconds() throws MathException {
		observations.add(new Observation(new Date(now),100d/50d));
		observations.add(new Observation(new Date(now+(1*1000*60)),100d/51d));
		observations.add(new Observation(new Date(now+(2*1000*60)),100d/52d));
		observations.add(new Observation(new Date(now+(3*1000*60)),100d/53d));
		
		Prediction prediction = PriceLinearRegression.predict(observations,TimeUnit.SECONDS);
		assertEquals(-0.9548, prediction.getSlope(),0.001);
		assertEquals(0, prediction.getSlopeErr(),0.001);
		assertEquals(1.88, prediction.getRealValue(),0.01);
		assertEquals(1.88, prediction.getPredictedValue(),0.01);
	}
	
	@Test
	public void testPredictPriceIsGrowing() throws MathException {
		observations.add(new Observation(new Date(now),2d));
		observations.add(new Observation(new Date(now+(1*1000*60)),2.01d));
		observations.add(new Observation(new Date(now+(2*1000*60)),2.02d));
		observations.add(new Observation(new Date(now+(3*1000*60)),2.03d));
		
		Prediction prediction = PriceLinearRegression.predict(observations,TimeUnit.MINUTES);
		assertEquals(13.83677, prediction.getSlope(),0.00001);
		assertEquals(0, prediction.getSlopeErr(),0.00001);
		assertEquals(2.03, prediction.getRealValue(),0);
		assertEquals(2.03, prediction.getPredictedValue(),0.01);
	}
	
	@Test
	public void testPredictPriceIsGrowing2() throws MathException {
		observations.add(new Observation(new Date(now),2.02d));
		observations.add(new Observation(new Date(now+(1*1000*60)),2.01d));
		observations.add(new Observation(new Date(now+(2*1000*60)),2.02d));
		observations.add(new Observation(new Date(now+(3*1000*60)),2.01d));
		observations.add(new Observation(new Date(now+(4*1000*60)),2.02d));
		observations.add(new Observation(new Date(now+(5*1000*60)),2.01d));
		observations.add(new Observation(new Date(now+(6*1000*60)),2.02d));
		observations.add(new Observation(new Date(now+(7*1000*60)),2.01d));
		observations.add(new Observation(new Date(now+(8*1000*60)),2.02d));
		observations.add(new Observation(new Date(now+(9*1000*60)),2.01d));
		observations.add(new Observation(new Date(now+(10*1000*60)),2.03d));
		observations.add(new Observation(new Date(now+(11*1000*60)),2.05d));
		
		Prediction prediction = PriceLinearRegression.predict(observations,TimeUnit.MINUTES);
		assertEquals(2.28, prediction.getSlope(),0.01);
		assertEquals(0.09, prediction.getSlopeErr(),0.01);
		assertEquals(2.05, prediction.getRealValue(),0);
		assertEquals(2.03, prediction.getPredictedValue(),0.01);
	}
	
	@Test
	public void testPredictPriceIsDecreasing() throws MathException {
		observations.add(new Observation(new Date(now),2d));
		observations.add(new Observation(new Date(now+(1*1000*60)),1.99d));
		observations.add(new Observation(new Date(now+(2*1000*60)),1.98d));
		observations.add(new Observation(new Date(now+(3*1000*60)),1.97d));
		
		Prediction prediction = PriceLinearRegression.predict(observations,TimeUnit.MINUTES);
		assertEquals(-14.241, prediction.getSlope(),0.001);
		assertEquals(0, prediction.getSlopeErr(),0.001);
	}
	
	@Test
	public void testPredictSlopeErrorExist() throws MathException {
		observations.add(new Observation(new Date(now),2d));
		observations.add(new Observation(new Date(now+(1*1000*60)),1.99d));
		observations.add(new Observation(new Date(now+(2*1000*60)),2.01d));
		observations.add(new Observation(new Date(now+(3*1000*60)),1.97d));
		
		Prediction prediction = PriceLinearRegression.predict(observations,TimeUnit.MINUTES);
		assertEquals(-10.1165, prediction.getSlope(),0.001);
		assertEquals(0.4663, prediction.getSlopeErr(),0.001);
	}
	
	@Test
	public void testPredict0degreesErrorNaN() throws MathException {
		observations.add(new Observation(new Date(now),100d/50d));
		observations.add(new Observation(new Date(now+(1*1000*60)),100d/50d));
		observations.add(new Observation(new Date(now+(2*1000*60)),100d/50d));
		observations.add(new Observation(new Date(now+(3*1000*60)),100d/50d));
		
		Prediction prediction = PriceLinearRegression.predict(observations,TimeUnit.MINUTES);
		assertEquals(0, prediction.getSlope(),0);
		assertEquals(Double.NaN, prediction.getSlopeErr(),0);
	}
	
	@Test
	public void testPredictSlopeNaN() throws MathException {
		observations.add(new Observation(new Date(now),100d/50d));
				
		Prediction prediction = PriceLinearRegression.predict(observations,TimeUnit.MINUTES);
		assertEquals(Double.NaN, prediction.getSlope(),0);
		assertEquals(Double.NaN, prediction.getSlopeErr(),0);
	}

}
