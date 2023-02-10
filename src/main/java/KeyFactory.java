
public class KeyFactory {
	private final float reference_A0=55;//Hertz
	private final float freqMin=reference_A0;
	private final int nbHandledOctaves=8;
	private float freqMax=(float) (reference_A0*Math.pow(2,nbHandledOctaves));
	private float freqClasses[][][]=new float[nbHandledOctaves][12][3];
	private final String[]stringKeys= {"A","A#","B","C","C#","D","D#","E","F","F#","G","G#"};
	
	public KeyFactory() {
		double modifier1,modifier2,modifier3,modifier4;
		modifier3=Math.pow(2,1/24.0);
		modifier4=Math.pow(2,-1/24.0);
		for(int oct=0;oct<8;oct++){
			modifier1=freqMin*Math.pow(2,oct);
			for(int key=0;key<12;key++){
				modifier2=Math.pow(2,key/12.0);
				freqClasses[oct][key][0]=(float) (modifier1*modifier2*modifier4);
				freqClasses[oct][key][1]=(float) (modifier1*modifier2);
				freqClasses[oct][key][2]=(float) (modifier1*modifier2*modifier3);
			}
		}
	}

	public Key buildKeyFromMeasuredPitch(double pitch) {
		if(pitch>freqMax)return new Key("A",8,256*55,100);
		if(pitch<freqMin)return new Key("A",0,55,100);
		int lastOct=0;
		int lastKey=0;
		for(int oct=0;oct<8;oct++){
			for(int key=0;key<12;key++){
				if(pitch>freqClasses[oct][key][0]){
					lastOct=oct;
					lastKey=key;
				}
			}
		}
		int jit=(int)Math.round( - 1000.0*( (freqClasses[lastOct][lastKey][1]-pitch)/freqClasses[lastOct][lastKey][1] ) );
		return (new Key(stringKeys[lastKey],lastOct,pitch, jit));	
	}

	public static void main (String[]args) {
		KeyFactory factory=new KeyFactory();
		Key la2=factory.buildKeyFromMeasuredPitch(440);
		System.out.println("La2="+la2);
		Key la2foireux=factory.buildKeyFromMeasuredPitch(444);
		System.out.println("La2foireux="+la2foireux);
		Key la3foireux=factory.buildKeyFromMeasuredPitch(878);
		System.out.println("La3foireux="+la3foireux);
		Key quelqueChose=factory.buildKeyFromMeasuredPitch(906);
		System.out.println("quelqueChose="+quelqueChose);
	}
}