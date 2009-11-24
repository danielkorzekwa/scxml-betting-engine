package dk.bot.bettingengine.regression;

import java.util.Date;

/**
 * Represents one observation used to predict trend of binary odds over time.
 * 
 * @author daniel
 * 
 */
public class Observation {

	/** The time at which the observation was made. */
	private final Date timestamp;

	/** Binary odds value. */
	private final double price;

	public Observation(Date timestamp, double price) {
		this.timestamp = timestamp;
		this.price = price;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public double getPrice() {
		return price;
	}

}
