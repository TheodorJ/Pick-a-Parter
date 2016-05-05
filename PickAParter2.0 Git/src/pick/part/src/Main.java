package pick.part.src;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.*;

import pick.part.FileIO.Save;
import pick.part.axis.Line;

public class Main extends Canvas implements Runnable, KeyListener, ActionListener, MouseListener, MouseMotionListener {
	Toolkit tk = Toolkit.getDefaultToolkit();
    public int WIDTH2 = ((int) tk.getScreenSize().getWidth());
    public int HEIGHT2 = ((int) tk.getScreenSize().getHeight());
    Font font = new Font("Calibri", Font.PLAIN, 15);
    
    public int WIDTH = 1200;
    public int realHeight = 720;
    public int HEIGHT = 700;
    
    public boolean leftHeld = false;
    public boolean rightHeld = false;
    
    public int grade = 0;
    
    public int mode = 0;
    
    public int xSpace;
    public int ySpace;
    
    public long lastUpdate = System.currentTimeMillis();
    
    public ArrayList<Line> lines = new ArrayList<Line>();
    public ArrayList<String> axisOptions = new ArrayList<String>();
    
    double z = 0.3609145;
    
    File file = new File(".");
    File[] files = file.listFiles();
    ArrayList<File> saves = new ArrayList<File>();
    
    public int currentFile = 0;
    
    public ArrayList<Save> usingSaves = new ArrayList<Save>();
    
    public static final String NAME = "Pick-a-Parter 2.0";
    DecimalFormat df = new DecimalFormat("#.##");
    private JFrame jFrame;
  
    public Main() {
    	setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));

        jFrame = new JFrame(NAME);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());
        jFrame.add(this, BorderLayout.CENTER);
        jFrame.setResizable(true);
        jFrame.setLocation(WIDTH2/2 - WIDTH/2,HEIGHT2/2 - HEIGHT/2);
        //jFrame.setUndecorated(true);
        jFrame.setVisible(true);
        jFrame.addKeyListener(this);
        addKeyListener(this);
        jFrame.addMouseListener(this);
        addMouseListener(this);
        jFrame.addMouseMotionListener(this);
        addMouseMotionListener(this);
        jFrame.pack();
        
        axisOptions.add("Time");
        axisOptions.add("Water on Map");
        lines.add(new Line(0, 1));
        
        jFrame.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                WIDTH = jFrame.getWidth();
                HEIGHT = jFrame.getHeight();
                xSpace = WIDTH - 120;
                ySpace = HEIGHT - 43;
            }
            public void componentHidden(ComponentEvent arg0) {}
            public void componentMoved(ComponentEvent arg0) {}
            public void componentShown(ComponentEvent arg0) {}
        });
        xSpace = WIDTH - 120;
        ySpace = HEIGHT - 43;
        for(int i = 0; i < files.length; i++) {
        	if(files[i].getName().contains(".psave")) {
        		saves.add(files[i]);
        	}
        }
    }

    public void drawCircle(Graphics g, double x, double y, int r) {
    	g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
    }
    
	public void run() {
		while (true) {
            //System.out.println("Hej");
        	tick();
        	try {
        		render();
        	} catch(Exception e) {}
            try {
                Thread.sleep(60);
            } catch (Exception e) {}
		}
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        
        for(int o = 0; o < usingSaves.size(); o++) {
        	int drawWid = WIDTH - 120;
        	int drawHei = HEIGHT - 42;
        	//drawHei = drawHei/30;
        	//drawWid = drawWid/3;
        	/*int widt = (WIDTH - 100) / usingSaves.get(o).wEntries.length;
        	for(int i = 1; i < usingSaves.get(o).timeLine/widt; i+=2) {
        		g.setColor(new Color(225, 225, 225));
        		g.fillRect(10 + widt * i, 10, widt, HEIGHT - 20);
        	}*/
        	g.setColor(new Color(0f, 0f, 1f, 0.1f));
        	for(int i = 1; i < usingSaves.get(o).aEntries.length; i++) {
        		System.out.println(((String)usingSaves.get(o).aEntries[i]));
        		String[] s1 = ((String)usingSaves.get(o).aEntries[i]).split(":");
        		String[] s2 = usingSaves.get(o).aEntries[i-1].split(":");
        		long time1 = Integer.parseInt(s1[1]);
        		long time2 = Integer.parseInt(s2[1]);
        		int mag1 = Integer.parseInt(s1[0]);
        		int mag2 = Integer.parseInt(s2[0]);
        		g.drawLine(10 + drawWid - (int) ((time1 * drawWid)/usingSaves.get(o).timeLine), 10 + drawHei - (int) ((mag1*drawHei)/usingSaves.get(o).maxa), 10 + drawWid - (int) ((drawWid*time2)/usingSaves.get(o).timeLine), 10 + drawHei - (int) ((drawHei*mag2)/usingSaves.get(o).maxa));
        		g.drawLine(11 + drawWid - (int) ((time1 * drawWid)/usingSaves.get(o).timeLine), 10 + drawHei - (int) ((mag1*drawHei)/usingSaves.get(o).maxa), 11 + drawWid - (int) ((drawWid*time2)/usingSaves.get(o).timeLine), 10 + drawHei - (int) ((drawHei*mag2)/usingSaves.get(o).maxa));
        		g.drawLine(10 + drawWid - (int) ((time1 * drawWid)/usingSaves.get(o).timeLine), 11 + drawHei - (int) ((mag1*drawHei)/usingSaves.get(o).maxa), 10 + drawWid - (int) ((drawWid*time2)/usingSaves.get(o).timeLine), 11 + drawHei - (int) ((drawHei*mag2)/usingSaves.get(o).maxa));
        		g.drawLine(9 + drawWid - (int) ((time1 * drawWid)/usingSaves.get(o).timeLine), 10 + drawHei - (int) ((mag1*drawHei)/usingSaves.get(o).maxa), 9 + drawWid - (int) ((drawWid*time2)/usingSaves.get(o).timeLine), 10 + drawHei - (int) ((drawHei*mag2)/usingSaves.get(o).maxa));
        		g.drawLine(10 + drawWid - (int) ((time1 * drawWid)/usingSaves.get(o).timeLine), 9 + drawHei - (int) ((mag1*drawHei)/usingSaves.get(o).maxa), 10 + drawWid - (int) ((drawWid*time2)/usingSaves.get(o).timeLine), 9 + drawHei - (int) ((drawHei*mag2)/usingSaves.get(o).maxa));
        		if(i == usingSaves.get(o).aEntries.length - 1) {
        			g.setColor(Color.black);
        			if(20 + drawHei - (int) ((mag1*drawHei)/usingSaves.get(o).maxa) > HEIGHT - 22) {
        				g.drawString(usingSaves.get(o).playerName + ", Grade" + usingSaves.get(o).grade, 10 + drawWid - (int) ((time1 * drawWid)/usingSaves.get(o).timeLine), -1*20 + drawHei - (int) ((mag1*drawHei)/usingSaves.get(o).maxa));
        			}
        			else {
        				g.drawString(usingSaves.get(o).playerName + ", Grade" + usingSaves.get(o).grade, 10 + drawWid - (int) ((time1 * drawWid)/usingSaves.get(o).timeLine), 20 + drawHei - (int) ((mag1*drawHei)/usingSaves.get(o).maxa));
        			}
        			g.setColor(Color.blue);
        		}
        	}
        	g.setColor(Color.black);
        	for(int i = 1; i < usingSaves.get(o).cEntries.length; i++) {
        		String[] s1 = ((String)usingSaves.get(o).cEntries[i]).split(":");
        		String[] s2 = usingSaves.get(o).cEntries[i-1].split(":");
        		long time1 = Integer.parseInt(s1[1]);
        		long time2 = Integer.parseInt(s2[1]);
        		int mag1 = Integer.parseInt(s1[0]);
        		int mag2 = Integer.parseInt(s2[0]);
        		g.drawLine(10 + drawWid - (int) (time1 * drawWid/usingSaves.get(o).timeLine), 10 + (int) (mag1*drawHei/usingSaves.get(o).maxc),10 + drawWid - (int) (drawWid*time2/usingSaves.get(o).timeLine), 10 + (int) (drawHei*mag2/usingSaves.get(o).maxc));
        		g.drawLine(11 + drawWid - (int) (time1 * drawWid/usingSaves.get(o).timeLine), 11 + (int) (mag1*drawHei/usingSaves.get(o).maxc), 11 + drawWid - (int) (drawWid*time2/usingSaves.get(o).timeLine), 10 + (int) (drawHei*mag2/usingSaves.get(o).maxc));
        		g.drawLine(10 + drawWid - (int) (time1 * drawWid/usingSaves.get(o).timeLine), 10 + (int) (mag1*drawHei/usingSaves.get(o).maxc), 10 + drawWid - (int) (drawWid*time2/usingSaves.get(o).timeLine), 11 + (int) (drawHei*mag2/usingSaves.get(o).maxc));
        		g.drawLine(9 + drawWid - (int) (time1 * drawWid/usingSaves.get(o).timeLine), 10 + (int) (mag1*drawHei/usingSaves.get(o).maxc), 9 + drawWid - (int) (drawWid*time2/usingSaves.get(o).timeLine), 10 + (int) (drawHei*mag2/usingSaves.get(o).maxc));
        		g.drawLine(10 + drawWid - (int) (time1 * drawWid/usingSaves.get(o).timeLine), 10 + (int) (mag1*drawHei/usingSaves.get(o).maxc), 10 +drawWid - (int) (drawWid*time2/usingSaves.get(o).timeLine), 9 + (int) (drawHei*mag2/usingSaves.get(o).maxc));
        	}
        }
        
        g.setColor(Color.BLACK);
        g.drawLine(10, 10, 10, HEIGHT - 32);
        g.drawLine(10, HEIGHT - 32, WIDTH - 110, HEIGHT - 32);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(WIDTH - 100, 0, 100, HEIGHT);
        g.setColor(Color.GRAY);
        g.fillRect(WIDTH - 90, 10, 80, 20);
        g.fillRect(WIDTH - 90, 40, 80, 20);
        g.fillRect(WIDTH - 90, 70, 80, 20);
        g.fillRect(WIDTH - 90, 100, 80, 20);
        g.setColor(Color.black);
        g.drawString("Files", WIDTH - 65, 25);
        g.drawString("Line Editor", WIDTH - 84, 55);
        g.drawString("Y-Axis", WIDTH - 70, 85);
        g.drawString("Export", WIDTH - 70, 115);
        g.dispose();
        bs.show();
	}

	private void tick() {
		//long dif = System.currentTimeMillis() - lastUpdate;
		
		//System.out.println(1000/dif);
		
		//lastUpdate = System.currentTimeMillis();
	}
	
	public static void main(String args[]) {
        new Main().start();
    }
    
	private void start() {
		new Thread(this).start();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		
		/*currentFile = e.getY()/20;
		
		//System.out.println(saves.get(currentFile).getName());
		load();
		aEntries = aLog.split("x");
		bEntries = bLog.split("x");
		cEntries = cLog.split("x");
		rEntries = rLog.split("x");
		wEntries = wLog.split("x");
		hEntries = hLog.split("x");
		for(int i = 0; i < aEntries.length; i++) {
			String[] poop = aEntries[i].split(":");
			//System.out.println(poop[0]);
			if(Integer.parseInt(poop[0]) > maxa) {
				maxa = Integer.parseInt(poop[0]);
			}
		}
		for(int i = 0; i < cEntries.length; i++) {
			String[] poop = cEntries[i].split(":");
			if(Integer.parseInt(poop[0]) > maxc) {
				maxc = Integer.parseInt(poop[0]);
			}
		}
		for(int i = 0; i < hEntries.length; i++) {
			String[] poop = hEntries[i].split(":");
			if(Integer.parseInt(poop[0]) > maxh) {
				maxh = Integer.parseInt(poop[0]);
			}
		}
		System.out.println(maxa);
		mode = 1;*/
		
		if(mouseX > WIDTH - 90 && mouseX < WIDTH - 10) {
			if (mouseY > 10 && mouseY < 30) {
				(new FileViewer(this)).start();
			}
		}
		
		if(mouseX > WIDTH - 90 && mouseX < WIDTH - 10) {
			if (mouseY > 40 && mouseY < 60) {
				(new LineEditor(this)).start();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			
		}
		
		if(e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
			leftHeld = true;
		}
		if(e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
			rightHeld = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
			leftHeld = false;
		}
		if(e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
			rightHeld = false;
		}
	}
}