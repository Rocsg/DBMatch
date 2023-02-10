import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
 
import com.google.common.primitives.Bytes;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class DBTest {
	public final static boolean bigEndian = false;
	public final static boolean signed = true;
	public final static int bits = 16;
	public final static int channels = 1;
	public final static float sampleRate = (float)44100.0;
	public final static AudioFormat format=new AudioFormat(sampleRate, bits, channels, signed,bigEndian);
	public final static float log2=(float)Math.log(2.0);
	public final static String slash=File.separator;

	
	public static void main(String[] args) {
		System.out.println("C 'est parti !");
		//sinusoidTest();
		musicMapTest();
	}
	

	public static void musicMapTest() {
		String testFile="Flute.WAV";
		File f=new File(System.getProperty("user.dir"),"test"+slash+"Input_samples"+slash+testFile);
		String strPath=f.getAbsolutePath();
		MusicMapBuilder mmbuild=new MusicMapBuilder();
		//mmbuild.analyzeAudioFile(strPath);
//		mmbuild.testSpectrum("BABY_DUC.WAV");
//	MusicMap musicMap=MusicMapBuilder.testSpectrum();
	}
	
	
	
	public static void SinusoidTest() {
		float[]buffer=createBasicSample();
		byte[] byteBuffer=convertSampleToReadableFormat(buffer);
		writeSampleToFile(byteBuffer,"test1.wav");
		readSample(byteBuffer);
	}
	
	public static float[] createBasicSample(){
		double seconds = 10.0;
		double f0 = 800.0;
		double amplitude0 = 0.8;
		double twoPiF0 = 2*Math.PI*f0;
		double f1 = 8*f0;
		double amplitude1 = 0.2;
		double twoPiF1 = 2*Math.PI*f1;
		float[] buffer = new float[(int) (seconds*sampleRate)];
	
		for (int sample = 0; sample < buffer.length; sample++) {
			double time = sample / sampleRate;
			double f0Component = amplitude0*Math.sin(twoPiF0*time);
			double f1Component = amplitude1*Math.sin(twoPiF1*time);
			buffer[sample] = (float) (f0Component + f1Component);
		}
		return buffer;
	}
	
	public static byte[] convertSampleToReadableFormat(float[]buffer){
		byte[] byteBuffer = new byte[buffer.length*2];
		int bufferIndex = 0;
		for (int i = 0; i < byteBuffer.length; i++) {
			final int x = (int) (buffer[bufferIndex++]*32767.0);
			byteBuffer[i] = (byte) x;i++;
			byteBuffer[i] = (byte) (x >>> 8);
		}
		return byteBuffer;
	}
	
	public static void writeSampleToFile(byte[] byteBuffer,String title) {
		File out = new File(System.getProperty("user.dir"),"test"+slash+"Computed_samples"+slash+title);		
		ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer);
		AudioInputStream audioInputStream;
		audioInputStream = new AudioInputStream(bais, format,byteBuffer.length);
		try {
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, out);
			audioInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readSample(byte[] byteBuffer) {	
		SourceDataLine line;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format);line.start();
			line.write(byteBuffer, 0, byteBuffer.length);line.close();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
	}
	
	
}
