import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class MusicMapPanel extends JPanel {
	private MusicMap mum;
	
	public MusicMapPanel(MusicMap mum) {
		this.mum=mum;
	}

	public MusicMapPanel() {
		this.mum=null;
	}

	public void setMusicMap(MusicMap mum) {
		this.mum=mum;
		repaint();
	}
	
	@Override
	public void paint(final Graphics g) {
		Key keyMax=new Key("A",8,0,256*55);
		int keyCodeMax=keyMax.keyNumber();
		int marginLeft=100;
		int marginUp=100;
		int yBar=10;
		int spaceForPercusAndOnsets=250;
		int X=getWidth()-marginLeft;
		int Y=getHeight()-marginUp-spaceForPercusAndOnsets;
		int Ystarts=getHeight()-(spaceForPercusAndOnsets*3)/4;
		int Yonsets=getHeight()-spaceForPercusAndOnsets/2;
		int Ypercus=getHeight()-spaceForPercusAndOnsets/4;
		double xPerSecond=X*1.0/mum.getLength();
		double yPerOctave=Y/8;
		double yPerKey=yPerOctave/12;
		int nbSecondsDisplayed=(int)Math.ceil(mum.getLength());
		int nbOctavesDisplayed=8;
		final Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		graphics.setBackground(Color.WHITE);
		graphics.clearRect(0, 0, getWidth(), getHeight());

		////////////// Texts ///////////////
		//Title
		if(mum==null)graphics.drawString("No sample imported",X/2,marginUp/3);
		else graphics.drawString("Music map of sample named : "+mum.getTitle(),(X*2)/5,marginUp/3);

		//X axis : time in seconds
		for(int i=0;i<nbSecondsDisplayed ; i++) {
			int offX=marginLeft-6+(int)Math.round(xPerSecond * i);
			graphics.drawString(""+i+"s",offX,(marginUp*9)/10);
		}

		//Y axis : keys, in octaves
		for(int i=0;i<=nbOctavesDisplayed ; i++) {
			int offY=marginUp+(int)Math.round(yPerOctave * i);
			graphics.drawString("A"+(8-i),(marginLeft*2)/3,offY);
		}

		//Start notes, Onset and percussion lines titles
		graphics.setColor(Color.BLUE);
		graphics.drawString("Starts",marginLeft/2,Ystarts);
		graphics.drawLine(marginLeft, Ystarts ,marginLeft+X, Ystarts);
		graphics.setColor(Color.BLUE);
		graphics.drawString("Onsets",marginLeft/2,Yonsets);
		graphics.drawLine(marginLeft, Yonsets ,marginLeft+X, Yonsets);
		graphics.setColor(Color.BLUE);
		graphics.drawString("Percus",marginLeft/2,Ypercus);
		graphics.drawLine(marginLeft, Ypercus ,marginLeft+X, Ypercus);

		
		////////////// Grid for visualization ///////////////
		//Vertical bar, on per second
		graphics.setColor(Color.BLACK);
		for(int i=0;i<nbSecondsDisplayed ; i++) {
			int offX=marginLeft+(int)Math.round(xPerSecond * i);
			graphics.drawLine(offX, marginUp, offX,Ypercus);
		}
		
		//Horizontal bar, on under each "A" key
		graphics.setColor(Color.BLACK);
		for(int i=0;i<=nbOctavesDisplayed ; i++) {
			int offY=marginUp+(int)Math.round(yPerOctave * i);
			graphics.drawLine(marginLeft, offY, marginLeft+X, offY);
		}
		

		////////////// Music map drawing of the analyzed sample : notes, onset events and percussion events ///////////////		
		//Draw notes
		System.out.println("\n\n\n\n\nDebug");
		for(Note note : mum.getNotes()) {
			int x0=(int)Math.round(marginLeft+xPerSecond*note.getStartTime() );
			int xWidth=(int)Math.ceil(xPerSecond*note.getDuration() );
			int y0=(int)Math.round( marginUp + (keyCodeMax-note.getKey().keyNumber()-1) *yPerKey); //increasing key pitch implies decreasing in y
			int transp=(int)(Math.round(255*Math.pow(note.getProbability(),4)));
			graphics.setColor(new Color(0,180,0,transp));
			graphics.fillRoundRect  (x0, y0, xWidth, (int)Math.floor(yPerKey) , 5 , 5);
		}

	
		//Draw starts of notes (TODO : major, minors..)
		graphics.setColor(new Color(0,180,0));
		for(Note note : mum.getNotes()) {
			int x0=(int)Math.round(marginLeft+xPerSecond*note.getStartTime() );
			int xWidth=2;
			int y0=Ystarts-yBar/2;
			int yWidth=yBar;
			int transp=(int)(Math.round(255*Math.pow(note.getProbability(),4)));
			graphics.setColor(new Color(0,180,0,transp));
			graphics.fillRect  (x0, y0, xWidth, yWidth);
		}


		//Draw detected onsets
		graphics.setColor(new Color(180,20,20));
		if(mum.getOnsets()!=null) {
			for(OnsetEvent onset : mum.getOnsets()) {
				System.out.println(onset);
				int x0=(int)Math.round(marginLeft+xPerSecond*onset.getTimestamp() );
				int xWidth=(int)Math.round(2+0.1*Math.pow(onset.getSalience(),0.85));
				int y0=Yonsets-xWidth/2;
				int yWidth=xWidth;
				graphics.fillOval(x0, y0, xWidth, yWidth);
			}
		}

		//Draw detected percussions
		graphics.setColor(new Color(20,20,180));
		if(mum.getPercus()!=null) {
			for(PercussionEvent percu : mum.getPercus()) {
				System.out.println(percu);
				int x0=(int)Math.round(marginLeft+xPerSecond*percu.getTimestamp() );
				int xWidth=5;
				int y0=Ypercus-xWidth/2;
				int yWidth=xWidth;
				graphics.fillOval(x0, y0, xWidth, yWidth);
			}
		}

	
	}
		
		
		
		
	
	
	
	
	
	public MusicMapPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public MusicMapPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public MusicMapPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}


}
