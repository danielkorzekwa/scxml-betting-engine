package dk.bot.bettingengine.regression;

import java.io.Serializable;

/** Represents prediction for a trend of binary odds over time.
 * 
 * @author daniel
 *
 */
public class Prediction implements Serializable{

	/** Regression slope in degrees, if <0 then price is decreasing, if >0 then price growing.
	 *  Example:
	 *  For regression y = f(minutes) and probability of price growing 1% (price is decreasing) every minute then slope = -45%
	 * 
	 *  Double.NaN if cannot be estimated (observations <2)
	 */
	private double slope;
	
	/** Probability of slope error (between 0 and 1). 0 - no error probability. Double.NaN if cannot be estimated*/
	private double slopeErr;
	
	/**Real value for the latest timestamp that the prediction was calculated for. To check the swing between real and predicted values.*/
	private double realValue;
	/**Predicted value for the latest timestamp that the prediction was calculated for. To check the swing between real and predicted values.*/
	private double predictedValue;

	public Prediction() {
	}
	
	public Prediction(double slope, double slopeErr) {
		this.slope=slope;
		this.slopeErr=slopeErr;
	}
	
	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	public double getSlopeErr() {
		return slopeErr;
	}

	public void setSlopeErr(double slopeErr) {
		this.slopeErr = slopeErr;
	}

	public double getPredictedValue() {
		return predictedValue;
	}

	public void setPredictedValue(double predictedValue) {
		this.predictedValue = predictedValue;
	}

	public double getRealValue() {
		return realValue;
	}

	public void setRealValue(double realValue) {
		this.realValue = realValue;
	}
}
