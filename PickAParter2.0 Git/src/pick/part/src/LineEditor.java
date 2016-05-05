package pick.part.src;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import pick.part.FileIO.Save;

public class LineEditor extends Canvas implements Runnable, KeyListener, ActionListener, MouseListener, MouseMotionListener {
	Toolkit tk = Toolkit.getDefaultToolkit();
    public int WIDTH2 = ((int) tk.getScreenSize().getWidth());
    public int HEIGHT2 = ((int) tk.getScreenSize().getHeight());
    
    public int WIDTH = 400;
    public int HEIGHT = 400;
    
    public long lastUpdate = System.currentTimeMillis();
    
    public Main main;
    
    public int offset = 0;
    
    public static final String NAME = "Line Editor";
    DecimalFormat df = new DecimalFormat("#.##");
    private JFrame jFrame;
    public static Font font = new Font("Calibri", Font.BOLD, 15);
    

    public LineEditor(Main m) {
    	main = m;
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));

        jFrame = new JFrame(NAME);
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
    }

    public void drawCircle(Graphics g, double x, double y, int r) {
    	g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
    }
    
	public void run() {
		while (true) {
            //System.out.println("Hej");
        	tick();
        	render();
            /*try {
                Thread.sleep(0);
            } catch (Exception e) {
            }*/
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
        g.setFont(font);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, WIDTH, 30);
        g.setColor(Color.black);
        g.drawString("Cancel", 5, 25);
        g.drawString("Save", 205, 25);
        g.drawRect(10, 40, 380, HEIGHT - 50);
        int i;
        for(i = 0; i < main.lines.size(); i++) {
        	g.drawRect(20, 50 + 20*i + offset, 10, 10);
        	g.drawString("Line " + i + ", " + main.axisOptions.get(main.lines.get(i).yIndex) + " vs. " + main.axisOptions.get(main.lines.get(i).xIndex), 40, 60 + 20*i + offset);
        }
        g.drawString("+Add new", 20, 55 + 20*(i) + offset);
        
        g.dispose();
        bs.show();
	}

	private void tick() {
		
	}
	
	/*public static void main(String args[]) {
        new XAxis().start();
    }*/
    
	public void start() {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
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
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
