
public class Note {
	private Key key;
	private double startTime;
	private double duration;
	/**
	 * @param key
	 * @param startTime
	 * @param duration
	 * @param probability
	 */
	public Note(Key key, double startTime, double duration, double probability) {
		super();
		this.key = key;
		this.startTime = startTime;
		this.duration = duration;
		this.probability = probability;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Note [key=" + key + ", startTime=" + startTime + ", duration=" + duration + ", probability="
				+ probability + "]";
	}
	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}
	/**
	 * @return the startTime
	 */
	public double getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(double duration) {
		this.duration = duration;
	}
	/**
	 * @return the probability
	 */
	public double getProbability() {
		return probability;
	}
	/**
	 * @param probability the probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}
	private double probability;
}