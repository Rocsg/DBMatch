
public class Key {
	private int key;
	private int octave;
	private double jitter;//The divergence in %Hz between the official key and the measured data
	private double frequency;
	
	final static int INT_ERROR_KEY=12;
	final static String STRING_ERROR_KEY="H";
	final static String[]stringKeys= {"A","A#","B","C","C#","D","D#","E","F","F#","G","G#"};
	public static int keyInt(String key) {
		for(int i=0;i<12;i++)if(stringKeys[i].equals(key))return i;
		return INT_ERROR_KEY;
	}
	
	public static String keyString(int i) {
		if(i<0 || i>11)return STRING_ERROR_KEY;
		return Key.stringKeys[i];
	}

	
	public Key(String key,int octave,double frequency,double jitter) {
		this.key=keyInt(key);
		this.octave=octave;
		this.jitter=jitter;
		this.frequency=frequency;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Key [key=" + stringKeys[key] + ", octave=" + octave + ", frequency=" + frequency + ", jitter=" + (jitter/10.0) + "%]";
	}

	public Key(int key,int octave,double frequency, double jitter) {
		this.key=key;
		this.octave=octave;
		this.jitter=jitter;
		this.frequency=frequency;
	}

	
	public int keyNumber() {
		return octave*12+key;
	}
	
	public boolean isTheSameKey(Key k1) {
		return (this.octave==k1.octave && this.key==k1.key);
	}
	
	public static boolean areTheSameKeys(Key k1,Key k2) {
		return (k1.key==k2.key && k1.octave==k2.octave);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */


}
