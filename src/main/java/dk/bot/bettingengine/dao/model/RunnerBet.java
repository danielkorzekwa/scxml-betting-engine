package dk.bot.bettingengine.dao.model;

import java.util.Date;

/**
 * Data model for runner bet
 * 
 * @author daniel
 * 
 */
public class RunnerBet {

	private int runnerStateId;
	private long betId;
	private Date placedDate;
	private String betType;

	private double price;
	private double size;

	private double sizeMatched;
	private double avgPriceMatched;
	private Date matchedDate;

	private double sizeCancelled;

	public int getRunnerStateId() {
		return runnerStateId;
	}

	public void setRunnerStateId(int runnerStateId) {
		this.runnerStateId = runnerStateId;
	}

	public long getBetId() {
		return betId;
	}

	public void setBetId(long betId) {
		this.betId = betId;
	}

	public Date getPlacedDate() {
		return placedDate;
	}

	public void setPlacedDate(Date placedDate) {
		this.placedDate = placedDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getSizeMatched() {
		return sizeMatched;
	}

	public void setSizeMatched(double sizeMatched) {
		this.sizeMatched = sizeMatched;
	}

	public double getAvgPriceMatched() {
		return avgPriceMatched;
	}

	public void setAvgPriceMatched(double avgPriceMatched) {
		this.avgPriceMatched = avgPriceMatched;
	}

	public double getSizeCancelled() {
		return sizeCancelled;
	}

	public void setSizeCancelled(double sizeCancelled) {
		this.sizeCancelled = sizeCancelled;
	}

	public String getBetType() {
		return betType;
	}

	public void setBetType(String betType) {
		this.betType = betType;
	}

	public Date getMatchedDate() {
		return matchedDate;
	}

	public void setMatchedDate(Date matchedDate) {
		this.matchedDate = matchedDate;
	}
	
}
