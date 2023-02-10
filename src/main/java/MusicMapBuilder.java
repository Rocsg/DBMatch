import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.SilenceDetector;
import be.tarsos.dsp.beatroot.BeatRootOnsetEventHandler;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.onsets.PercussionOnsetDetector;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class MusicMapBuilder extends JFrame implements PitchDetectionHandler, OnsetHandler{
	public static String TEST_TITLE="Flute.WAV";
	public final static boolean bigEndian = false;
	public final static boolean signed = true;
	public final static int bits = 16;
	public final static int channels = 1;
	public final static float sampleRate = (float)44100.0;
	public final static AudioFormat format=new AudioFormat(sampleRate, bits, channels, signed,bigEndian);
	private final MusicMapPanel panel;
	private KeyFactory keyFactory;
	int pits=0;
	int percs=0;
	int pitsDown=0;
	double valPrec=0;
	ArrayList<PitchEvent>listPitchs;
	ArrayList<PercussionEvent>listPercus;
	ArrayList<Note>listNotes;
	ArrayList<OnsetEvent>listOnsets;
	float spectrumTime;
	public MusicMapBuilder() {
		this.keyFactory=new KeyFactory();
		this.listPitchs=new ArrayList<PitchEvent>();

		this.listPercus=new ArrayList<PercussionEvent>();
		this.listNotes=new ArrayList<Note>();
		this.listOnsets=new ArrayList<OnsetEvent>();
		this.panel=new MusicMapPanel();
		this.add(panel);
	}
	
	public static void main(String... strings) throws InterruptedException,InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {}

				MusicMapBuilder frame = new MusicMapBuilder();
				String title=TEST_TITLE;
				String testFile=getPath(title);
				
				frame.pack();frame.setSize(1600,1000);frame.setVisible(true);
				MusicMap mum=frame.analyzeAudioFile(testFile,title);
				frame.panel.setMusicMap(mum);
			}
		});
	}
	
			
	public void handleOnset(double time, double salience) {
		if(salience <0 ) {
			System.out.println("Percussion detected : t="+time);
			listPercus.add(new PercussionEvent(2*time));
		}
		else {
			System.out.println("Onset detected : t="+time+", pow="+salience);
			listOnsets.add(new OnsetEvent(time,salience));
		}
	}
	
	
	public void handlePercussion(double timestamp,double pouet) {
		percs++;
	}
	
	public void handlePitch(PitchDetectionResult pitchDetectionResult,AudioEvent audioEvent) {
		double timeStamp = audioEvent.getTimeStamp();
		float pitch = pitchDetectionResult.getPitch();
		float probability = pitchDetectionResult.getProbability();
		Key keyDetected=keyFactory.buildKeyFromMeasuredPitch(pitch);
		listPitchs.add(new PitchEvent(keyDetected,timeStamp,probability));
		
		/* Last part of this function is just for debug, with some information for developers*/
		if(probability>0.93) {
			if((timeStamp-valPrec)>0.10) {
				System.out.println("------Stop note---------");
				System.out.println(" ");
				System.out.println(" ");
				System.out.println("------Nouvelle note-----");
			}
			System.out.println("Key detected : ["+keyDetected+"] at parameters t="+timeStamp+"-"+", f="+pitch+", prob="+probability);
			valPrec=timeStamp ;
		}
	}

	public MusicMap analyzeAudioFile(String path,String title){
		listPitchsEventsAndOnsets(path);
		MusicMap mum=convertEventsToMusicMap(title);
		//mum.computeRythmicParameters();
		//mum.computeMelodicParameters();
		return mum;
	}
	
	
	
	public void listPitchsEventsAndOnsets(String path) {
		File audioFile = new File(path);
		final float sampleRate=44100;//Hertz
		final int bagSize=512;//samples used for FFT
		final float tBag=(float) (bagSize*1.0/sampleRate);		
		int overlap;
		final float spectrumRate=80;//Hertz
		this.spectrumTime=1/spectrumRate;
		if(tBag<spectrumTime)overlap=0;
		else overlap=(int)Math.round(sampleRate*(tBag-spectrumTime/2));
		System.out.println("Tbag="+tBag+"et spectrumTime="+spectrumTime+" et overlap="+overlap);

	
		
		//Compute the spectrogram, according to parameters
		AudioDispatcher dispatcher;
		try {
			//Pitch estimation
			dispatcher = AudioDispatcherFactory.fromFile(audioFile, bagSize,(int)overlap);
			dispatcher.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.YIN, sampleRate, bagSize, this));
			dispatcher.run();
			
			//Onset detector
			dispatcher = AudioDispatcherFactory.fromFile(audioFile, bagSize,overlap);
			ComplexOnsetDetector detector = new ComplexOnsetDetector(bagSize,0.3,0.1);
			detector.setHandler(this);			
			dispatcher.addAudioProcessor(detector);			
			dispatcher.run();
			
			//Percussion detector
			dispatcher = AudioDispatcherFactory.fromFile(audioFile, bagSize,overlap);
			PercussionOnsetDetector perc=new PercussionOnsetDetector (sampleRate,bagSize,this,60,4);
			perc.setHandler(this);
			dispatcher.addAudioProcessor(perc);			
			dispatcher.run();

		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		System.out.println("ons="+listOnsets.size()+" et pitchs="+listPitchs.size()+" et percs="+percs);
	}
	
	public MusicMap convertEventsToMusicMap(String title) {
		final double seuilStopNote=0.10+2*spectrumTime;//The minimum detected silence (in seconds) that make the pitch detector to conclude that there is two separated notes
		//1)Track pitch events 
		//each time a pitchEvent is upon the hysteresis threshold, start tracking,
		//stop tracking when  : 
				//next pitch event have not the same key
				//next pitch event goes under the hysteresis low threshold
				//next pitch event is far from the previous one (1.5 times the time distance between samples)
		boolean isTracking=false;
		Key lastKey=null;
		Key curKey=null;
		PitchEvent lastPit=null;
		PitchEvent curPit=null;
		int startIndex=-1;
		double histeresisStartThreshold=0.90;
		double histeresisStopThreshold=0.7;
		int curInd=0;
		do {			
			curKey=listPitchs.get(curInd).getKey();
			curPit=listPitchs.get(curInd);
			if(isTracking) {//curPit-1 had a key actually tracked in time, until lastStart
				if(  (curKey.isTheSameKey( lastKey ) ) && 
					 (curPit.getProbability() >= histeresisStopThreshold)  && 
					 (curPit.getTimestamp() - lastPit.getTimestamp() < seuilStopNote) ) {//keep tracking
					lastPit=curPit;
					lastKey=curKey;
				}
				else {
					addNoteToList(lastKey,startIndex,curInd-1);
					lastKey=null;
					lastPit=null;
					startIndex=-1;
					isTracking=false;
				}	
			}
			if(!isTracking) {
				if(  curPit.getProbability() >= histeresisStartThreshold) {//start a new track
					lastPit=curPit;
					lastKey=curKey;
					startIndex=curInd;
					isTracking=true;
				}
				else {		/*keep processing the data */        }
			}

		}while(++curInd<listPitchs.size()-1);
		return new MusicMap(listNotes,listPercus,listOnsets,title,spectrumTime);
		
	}
	
	public void addNoteToList(Key k,int firstIndex,int lastIndex){
		double accumulator=0;
		for(int i=firstIndex;i<=lastIndex ; i++)accumulator+=this.listPitchs.get(i).getProbability();
		accumulator/=(lastIndex-firstIndex+1);
		Note not=new Note( this.listPitchs.get(firstIndex).getKey() ,
						   this.listPitchs.get(firstIndex).getTimestamp() -spectrumTime,  
					   	   this.listPitchs.get(lastIndex).getTimestamp()+spectrumTime -  this.listPitchs.get(firstIndex).getTimestamp() ,
					   	   accumulator );
		listNotes.add(not);
	}
	
	public void drawMusicMapPanel(MusicMap mum) {
		
	}
	
	public static String getPath(String title) {
		String slash=File.separator;
		File f=new File(System.getProperty("user.dir"),"test"+slash+"Input_samples"+slash+title);
		return f.getAbsolutePath();
	}
	

}
