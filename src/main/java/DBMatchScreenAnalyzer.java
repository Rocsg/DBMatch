import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class DBMatchScreenAnalyzer extends JFrame implements PitchDetectionHandler {
	public final static String slash=File.separator;
	private final MusicMapPanel panel;
	private int count=0;
	private AudioDispatcher dispatcher;
	private Mixer currentMixer;	
	private PitchEstimationAlgorithm algo;	
	private ActionListener algoChangeListener = new ActionListener(){
		public void actionPerformed(final ActionEvent e) {
	}};
	
	public DBMatchScreenAnalyzer(){
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("DB Analyzer");
		panel = new MusicMapPanel();
	}

	
	
	
	public void setAudioFile(String path) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		File audioFile = new File(path);
//		if(dispatcher!= null){dispatcher.stop();}
		float sampleRate = 44100;
		int bufferSize = 1536;
		int overlap = 0;
		
		System.out.println("Started listening file : "+path);

		final AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,false);
//		dispatcher = new AudioDispatcher(audioStream, bufferSize,overlap);
		dispatcher = AudioDispatcherFactory.fromFile(audioFile, bufferSize,25);
		// add a processor, handle percussion event.
		dispatcher.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.YIN, sampleRate, bufferSize, this));		
		
		// run the dispatcher (on a new thread).
		new Thread(dispatcher,"Audio dispatching").start();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4787721035066991486L;

	public static void main(String... strings) throws InterruptedException,InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					//ignore failure to set default look en feel;
				}
				DBMatchScreenAnalyzer frame = new DBMatchScreenAnalyzer();
				String testFile="Flute.WAV";
				File f=new File(System.getProperty("user.dir"),"test"+slash+"Input_samples"+slash+testFile);
				String strPath=f.getAbsolutePath();
				frame.pack();
				frame.setSize(640,480);
				frame.setVisible(true);
				try {
					frame.setAudioFile(strPath);
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void handlePitch(PitchDetectionResult pitchDetectionResult,AudioEvent audioEvent) {
		double timeStamp = audioEvent.getTimeStamp();
		float pitch = pitchDetectionResult.getPitch();
		//panel.setMarker(timeStamp, pitch);		
		System.out.println("Ouais !"+(count++)+" "+timeStamp);
	}

}