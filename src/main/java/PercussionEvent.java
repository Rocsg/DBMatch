
public class PercussionEvent {
	private double timestamp;
	/**
	 * @return the timestamp
	 */
	public double getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}
	public PercussionEvent(double timestamp) {
		super();
		this.timestamp = timestamp;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PercussionEvent [timestamp=" + timestamp + "]";
	}
	
	
}
