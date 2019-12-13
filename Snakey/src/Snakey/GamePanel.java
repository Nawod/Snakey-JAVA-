package Snakey;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 500, HEIGHT = 500;
	
	private Thread thread;
	private boolean running;
	private boolean right = true, left = false, up = false, down = false, space = false;
	
	private BodyPart b;
	private ArrayList<BodyPart> snake;

	private Bite bite;
	private ArrayList<Bite> bites;
	
	private Random r;
	
	private int xCoor = 10, yCoor = 10, size = 5;
	private int ticks =0;
	private int score = 0;
	
	public GamePanel() {
		setFocusable(true);
		
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		addKeyListener(this);
		snake = new ArrayList<BodyPart>();
		bites = new ArrayList<Bite>();
		
		r = new Random();
		
			start();
		
	}
	
	public void start() {
		
		running = true;
		thread = new Thread(this);
		thread.start();
		
	}
	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}
	public void tick() {
		//snake movement
		if(snake.size()==0) {
			b = new BodyPart(xCoor, yCoor,10);
			snake.add(b);
		}
		ticks++;
		
		if(ticks > 250000) {
			if(right) xCoor++;
			if(left) xCoor--;
			if(up) yCoor--;
			if(down) yCoor++;
			
			ticks = 0;
			
			b = new BodyPart(xCoor, yCoor, 10);
			snake.add(b);
			
			if(snake.size() > size) {
				snake.remove(0);
				
			}
		}
		
		//bite placements
		if(bites.size()==0) {
			int xCoor = r.nextInt(49);
			int yCoor = r.nextInt(49);
			
			bite = new Bite(xCoor, yCoor, 10);
			bites.add(bite);
		}
		for(int i =0; i < bites.size();i++) {
			if(xCoor == bites.get(i).getxCoor() && yCoor == bites.get(i).getyCoor()) {
				size++;
				score++;
				bites.remove(i);
				i++;
			}
		}
		

	}
	public void paint(Graphics g) {
		//game panel creating
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for(int i = 0 ; i < WIDTH / 10; i++) {
			g.drawLine(i * 10, 0, i * 10, HEIGHT);
		}
		
		for(int i = 0 ; i < HEIGHT / 10; i++) {
			g.drawLine(0, i * 10 , HEIGHT, i * 10);
		}
		
		for(int i = 0 ; i < snake.size();i++) {
			snake.get(i).draw(g);
		}
		
		for(int i = 0; i < bites.size();i++) {
			bites.get(i).draw(g);
		}
		
		g.setColor(Color.WHITE);
		g.drawString("Score : " + score, 10, 10);
		
		//GameOver
		for(int i = 0;i < snake.size();i++) {
			if(xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
				if(i != snake.size() - 1) {
					g.setColor(Color.WHITE);
					g.drawString("GAMEOVER", 150, 200);
					stop();
				}
			}
		}
		
		if(xCoor < 0 || xCoor > 49 || yCoor < 0 || yCoor > 49) {
			g.setColor(Color.WHITE);
			g.drawString("GAMEOVER", 220, 250);
			stop();
		}
	}

	@Override
	public void run() {
		while (running) {
			tick();
			repaint();
			
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//snake direction
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_SPACE) {
			space = true;
		}
		if(key == KeyEvent.VK_RIGHT && !left) {
			right = true;
			up = false;
			down = false;
		}
		if(key == KeyEvent.VK_LEFT && !right) {
			left = true;
			up = false;
			down = false;
		}
		if(key == KeyEvent.VK_UP && !down) {
			up = true;
			right = false;
			left = false;
		}
		if(key == KeyEvent.VK_DOWN && !up) {
			down = true;
			right = false;
			left = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
