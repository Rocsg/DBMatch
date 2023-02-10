
public class NoteFactory {
	final float reference_A0=55;//Hertz
	float freqMin=reference_A0;
	int nbHandledOctaves=8;
	float freqMax=(float) (reference_A0*Math.pow(2,nbHandledOctaves));
	float freqClasses[][][]=new float[nbHandledOctaves][12][3];
	final String[]stringKeys= {"A","A#","B","C","C#","D","D#","E","F","F#","G","G#"};

	public int getOctave(int keyCode) {
		return (keyCode%1000)/100;
	}	
	public int getJitter(int keyCode) {
		return keyCode/1000;
	}	
	public int getKey(int keyCode) {
		return keyCode%100;
	}
	public String getStringKey(int keyCode) {
		return stringKeys[keyCode%100];
	}
	public int getKeyCode(double pitch) {
		int keyRet;
		if(pitch>freqMax)return 812;
		if(pitch<freqMin)return 0;
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
		int jit=(int)Math.round( 1000.0*Math.abs( (freqClasses[lastOct][lastKey][1]-pitch)/freqClasses[lastOct][lastKey][1] ) );
		return jit*1000+lastOct*100+lastKey;
	}
	
	private void initFrequenciesClasses(){
	}
		
	
	public NoteFactory() {
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

}
