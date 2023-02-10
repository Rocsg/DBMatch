import java.util.ArrayList;

public class MusicMap {	

	
	public MusicMap(ArrayList<Note> listNotes,ArrayList<PercussionEvent>listPercus,
		   ArrayList<OnsetEvent> listOnsets,String title,double spectrumTime) {
		this.nbBeatsPerMesure = nbBeatsPerMesure;
		this.nbMeasures = nbMeasures;
		this.globalPitch = globalPitch;
		this.listOnsets=listOnsets;
		this.listNotes=listNotes;
		this.tempo = tempo;
		this.listPercus=listPercus;
		this.repitchable=true;
		this.culturalCategory=culturalCategory;
		this.instrumentsCategory=instrumentsCategory;
		this.isFromDictionary=isFromDictionary;
		this.title=title;
		this.length=0;
		if(listNotes.size()>0)this.length = Math.max(this.length, listNotes.get(listNotes.size()-1).getStartTime() + listNotes.get(listNotes.size()-1).getDuration() );
		if(listPercus.size()>0)this.length=Math.max(this.length ,  listPercus.get(listPercus.size()-1).getTimestamp() );
		if(listOnsets.size()>0)this.length=Math.max(this.length ,  listOnsets.get(listOnsets.size()-1).getTimestamp() );
		this.spectrumTime=spectrumTime;
	}

	
	
	/**
	 * @param nbBeatsPerMesure Number of beats per mesure, according to the "tempo" parameter (i.e. distance betweend two beats = 60seconds/tempo)
	 * @param nbMeasures Number of measure in the model
	 * @param globalPitch
	 * @param listNotes : the list of successive Notes encountered in the sample
	 * @param tempo Number of beats in a minute
	 * @param listPercus : list of PercussionEvent, giving their power, onset time, and duration 
	 */
	public MusicMap(int nbBeatsPerMesure, int nbMeasures, float globalPitch,ArrayList<Note> listNotes,  int tempo,ArrayList<PercussionEvent>listPercus,
				    boolean isRepitchable,int culturalCategory,int instrumentsCategory,boolean isFromDictionary,String title,double length,double spectrumTime) {
		this.nbBeatsPerMesure = UNDEFINED_VALUE;
		this.nbMeasures =UNDEFINED_VALUE;
		this.globalPitch = UNDEFINED_VALUE;
		this.listNotes=listNotes;
		this.tempo = UNDEFINED_VALUE;
		this.listPercus=listPercus;
		this.repitchable=true;
		this.culturalCategory=UNDEFINED_VALUE;
		this.instrumentsCategory=UNDEFINED_VALUE;
		this.isFromDictionary=false;
		this.title=title;
		this.length=length;
		this.spectrumTime=spectrumTime;
	}
	
	
	
	
	public void computeRythmicParameters(){
		setTempo(60);
		setNbMeasures(1);
		setNbBeatsPerMesure(4);
	}

	public void computeMelodicParameters(){
		setGlobalPitch(440);
	}
	
	public double comparisonScore(){
		return 0;		
	}
	
	

	public MusicMap() {
		this.nbBeatsPerMesure = DEFAULT_VALUE;
		this.nbMeasures = DEFAULT_VALUE;
		this.globalPitch = DEFAULT_VALUE;
		this.listNotes = new ArrayList<Note>();
		this.tempo = DEFAULT_VALUE;
		this.listPercus = new ArrayList<PercussionEvent>();
		this.repitchable=DEFAULT_BOOLEAN;
		this.culturalCategory=DEFAULT_VALUE;
		this.instrumentsCategory=DEFAULT_VALUE;
		this.isFromDictionary=DEFAULT_BOOLEAN;
	}
	public MusicMap(String pathToDictionary,int index) {
		//open file
		//look at index
		//get the parameters out
		//give the parameters to the current object
		//it's over
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	
	
	
	
	
	
	
	/** 
	 * Static values and param declarations
	 */
	public static final int ERROR_VALUE=-1;
	public static final int DEFAULT_VALUE=0;
	public static final int UNDEFINED_VALUE=-2;
	public static final boolean DEFAULT_BOOLEAN=false;
	
	public static final int INST_CAT_PERCU=1;
	public static final int INST_CAT_VOCAL=2;
	public static final int INST_CAT_WIND=3;
	public static final int INST_CAT_STRINGS=4;
	public static final int INST_CAT_WTF=5;

	public static final int CULT_CAT_REGGAE=1;
	public static final int CULT_CAT_JAZZ=2;
	public static final int CULT_CAT_WORLD=3;
	public static final int CULT_CAT_ROCK=4;
	public static final int CULT_CAT_TECHNO=5;
	public static final int CULT_CAT_CLASSIC=6;

	public double spectrumTime;
	
	
	/** 
	 * Object parameters, minor getters and setters
	 */
	public double length;
	public double getLength() {
		return this.length;
	}
	public void setLength(double length) {
		this.length=length;
	}
	
	public int nbBeatsPerMesure;
	public int getNbBeatsPerMesure() {
		return nbBeatsPerMesure;
	}
	public void setNbBeatsPerMesure(int nbBeatsPerMesure) {
		this.nbBeatsPerMesure = nbBeatsPerMesure;
	}

	public String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title=title;
	}
	
	public int nbMeasures;
	public int getNbMeasures() {
		return nbMeasures;
	}
	public void setNbMeasures(int nMeasures) {
		this.nbMeasures = nMeasures;
	}

	public float globalPitch;
	public float getGlobalPitch() {
		return globalPitch;
	}
	public void setGlobalPitch(float globalPitch) {
		this.globalPitch = globalPitch;
	}

	public ArrayList<Note> listNotes;
	public ArrayList<Note> getNotes() {
		return listNotes;
	}
	public void setNotes(ArrayList<Note> list) {
		this.listNotes=list;
	}
	
	public int tempo;
	public int getTempo() {
		return tempo;
	}
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
	public ArrayList<OnsetEvent> listOnsets;
	public ArrayList<OnsetEvent> getOnsets() {
		return listOnsets;
	}
	public void setOnsets(ArrayList<OnsetEvent> list) {
		this.listOnsets = list;
	}

	public ArrayList<PercussionEvent> listPercus;
	public ArrayList<PercussionEvent> getPercus() {
		return listPercus;
	}
	public void setPercus(ArrayList<PercussionEvent> list) {
		this.listPercus = list;
	}

	public boolean repitchable;
	public boolean isRepitchable() {
		return repitchable;
	}
	public void setRepitchable(boolean repitchable) {
		this.repitchable = repitchable;
	}
	
	public int rythmicCategory;
	public int getRythmicCategory() {
		return rythmicCategory;
	}
	public void setRythmicCategory(int rythmicCategory) {
		this.rythmicCategory = rythmicCategory;
	}

	public int culturalCategory;
	public int getCulturalCategory() {
		return culturalCategory;
	}
	public void setCulturalCategory(int culturalCategory) {
		this.culturalCategory = culturalCategory;
	}

	public int instrumentsCategory;	
	public int getInstrumentsCategory() {
		return instrumentsCategory;
	}
	public void setInstrumentsCategory(int instrumentsCategory) {
		this.instrumentsCategory = instrumentsCategory;
	}

	public boolean isFromDictionary;	
	public boolean isFromDictionary() {
		return isFromDictionary;
	}
	public void setFromDictionary(boolean isFromDictionary) {
		this.isFromDictionary = isFromDictionary;
	}

	
	
}
