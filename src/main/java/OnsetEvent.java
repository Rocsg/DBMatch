
public class OnsetEvent {
	double timestamp;
	double salience;
	public OnsetEvent(double timestamp,double salience) {
		this.timestamp=timestamp;
		this.salience=salience;
	}

	/**
	 * @return the time
	 */
	public double getTimestamp() {
		return timestamp;
	}

	/**
	 * @param time the time to set
	 */
	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the salience
	 */
	public double getSalience() {
		return salience;
	}

	/**
	 * @param salience the salience to set
	 */
	public void setSalience(double salience) {
		this.salience = salience;
	}

	public String toString() {
		return ("OnsetEvent : t="+timestamp+" , pow="+salience);
	}
}
