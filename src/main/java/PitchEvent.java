
public class PitchEvent {
	private Key key;
	private double timestamp;
	private double probability;
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public double getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}

	public PitchEvent(Key key, double timestamp, double probability) {
		super();
		this.key = key;
		this.timestamp = timestamp;
		this.probability = probability;
	}

	public String toString() {
		return "PitchEvent [key=" + key + ", timestamp=" + timestamp + ", probability=" + probability + "]";
	}//The probability that the frequency was well estimated
}