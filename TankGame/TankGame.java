package TankGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class TankGame extends JApplet implements Runnable{

	final static int WindowRatio = 5;
	final static int WW = WindowRatio*200;
	final static int WL = WW/2;
	int totalb = 0;
	
	final static int mapW = 1024;
	final static int mapL = 1024;
	
	private boolean running;
	private Thread gameThread;
	private static JFrame frame;
	
	static int [][] mapping;
	
	public static ArrayList<Wall> wallList = new ArrayList<Wall>();
	public static ArrayList<Bullet>bulletLine = new ArrayList<Bullet>();
	public static ArrayList<Bullet>bulletLine2 = new ArrayList<Bullet>();
	public static ArrayList<Bullet>tankBullet = new ArrayList<Bullet>();
	public static ArrayList<Explosion>explosion = new ArrayList<Explosion>();
	public static ArrayList<Explosion>explosion2 = new ArrayList<Explosion>();
	public static ArrayList<Powerup>PU = new ArrayList<Powerup>();
	public Iterator<Wall> wallIter;
	public Iterator<Bullet> bulletIter;
	boolean removeWall = false;
	boolean removeBullet = false;
	
	private BufferedImage bimg;
	private BufferedImage tankSI;
	
	private Image miniMap;
	
	private long newtimer;
	private long oldtimer;
	private long startime;
	private Random R = new Random();
	
	public Source S = new Source();
	KeyControl KC;
	GameEvents GE;

	
	Tank tank1; 
	Tank tank2;

	public static int score1 = 0;
	public static int score2 = 0;
	
	///////////init
	@Override
	public void init(){
		Mapping();
		
		tank1 = new Tank(S, 3*32, 3*32, 1);
		tank2 = new Tank(S, 28*32, 28*32, 2);
		
		GE = new GameEvents();
		KC = new KeyControl(GE);
		addKeyListener(KC);
		GE.addObserver(tank1);
		GE.addObserver(tank2);
		
		S.BGM.loop();
		
		setSize(WW, WL);
		setFocusable(true);
	}
	
	
	
	@Override
	public synchronized void start(){
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
		System.out.println("Game Start!");
	}
	
	@Override
	public synchronized void stop(){
		if(!running){return;}
		running = false;
	}
	
	///////update here
	public void update(){
		tank1.update();
		tank2.update();
		
		for(int i=0; i<tankBullet.size();i++){
			bulletLine.add(tankBullet.get(i));
		}
		tankBullet.clear();
		
		for(int i=0; i<bulletLine.size(); i++){
			bulletLine.get(i).update();
		}
		
		newtimer = System.currentTimeMillis();
		if(newtimer - oldtimer >= 10000){
			PU.clear();
			PU.add(new Powerup(S.PowerUp[0], R.nextInt((int)getSize().getWidth()), R.nextInt((int) getSize().getHeight()),0));
			PU.add(new Powerup(S.PowerUp[0], R.nextInt((int)getSize().getWidth()), R.nextInt((int) getSize().getHeight()),0));
			PU.add(new Powerup(S.PowerUp[0], R.nextInt((int)getSize().getWidth()), R.nextInt((int) getSize().getHeight()),0));
			PU.add(new Powerup(S.PowerUp[0], R.nextInt((int)getSize().getWidth()), R.nextInt((int) getSize().getHeight()),0));
			oldtimer = newtimer;
		}
		
		for(int i=0; i<PU.size(); i++){
			if(tank1.isCollision(PU.get(i))){
				tank1.PowerUp();
				PU.remove(i);
				i--;
			}else if(tank1.isCollision(PU.get(i))){
				tank2.PowerUp();
				PU.remove(i);
				i--;
			}
		}
		
		/////////collision
		for(int i=0; i<wallList.size(); i++){
			if(tank1.isCollision(wallList.get(i))){
				GE.collision(wallList.get(i));
			}
			if(tank2.isCollision(wallList.get(i))){
				GE.collision(wallList.get(i));
			}
				
			bulletIter = bulletLine.iterator();
			while(bulletIter.hasNext()){
				Bullet b = bulletIter.next();
				if(b.isCollision(wallList.get(i))){
					explosion.add(new Explosion(S.smallExp,b.getX(), b.getY(), 1));
					S.expSmall.play();
					if(wallList.get(i).destructible){
						wallList.remove(i);
						removeWall = true;
					}
				}else if(b.isCollision(tank1) && b.getID() == 2){
					if(tank1.isAlive()){
						GE.collision(b);
					}else{
						bulletLine2.add(b);
					}
				}else if(b.isCollision(tank2) && b.getID() == 1 ){
					if(tank2.isAlive()){
						GE.collision(b);
					}else{
						bulletLine2.add(b);
					}
				}
				else{
					bulletLine2.add(b);
				}
			}		
			
			bulletLine.clear();
			ArrayList<Bullet> temp = bulletLine2;
			bulletLine2 = bulletLine;
			bulletLine = temp;
			
			if(removeWall){
						i--;
			removeWall = false;
			}
		}	
	}
	
	///////draw here
	public void draw(Graphics g){
		drawBG(mapW, mapL, g);
		
		for(int i=0; i<wallList.size(); i++){
			wallList.get(i).draw(g);
		}
		
		for(int i=0; i<bulletLine.size(); i++){
			bulletLine.get(i).draw(g);
		}
		
		for(int i=0; i<PU.size(); i++){
			PU.get(i).draw(g);
		}
		
		for(int i=0; i<explosion.size(); i++){
			explosion.get(i).draw(g);
			if(!explosion.get(i).done()){
				explosion2.add(explosion.get(i));
			}
		}
		explosion.clear();
		ArrayList<Explosion> temp = explosion2;
		explosion2 = explosion;
		explosion = temp;
		
		tank1.draw(g);
		tank2.draw(g);
	}
	
	@Override
	public void paint(Graphics g){
		Graphics2D g2 = createGraphics2D(mapW, mapL);
		Graphics2D gsub = createGraphics2DSI((int)getSize().getWidth(), (int)getSize().getHeight());
		
		draw(g2);
		
		BufferedImage tanksi1 = tankView(tank1);
		gsub.drawImage(tanksi1, 0, 0, null);
		
		BufferedImage tanksi2 = tankView(tank2);
		gsub.drawImage(tanksi2, (int)getSize().getWidth()/2, 0, null);
		
		gsub.setFont(new Font("Default", Font.PLAIN,20));
		gsub.setColor(Color.WHITE);
		gsub.drawString("Life: "+ tank1.getLife()+"     Score: "+score1, (int) (getSize().getWidth()/4) - 80, 20);
		gsub.drawString("Life: "+ tank2.getLife()+"     Score: "+score2+"", (int) (getSize().getWidth()*3/4) -80, 20);
		
		gsub.setColor(Color.BLACK);
		gsub.fillRect((int)getSize().getWidth()/2-1, 0, 2, (int)getSize().getHeight());
		gsub.fillRect(((int)getSize().getWidth()/2)-(mapW/16)-2, (int)getSize().getHeight()-(mapL/8)-2, mapW/8+4 , mapL/8+2);
		
		gsub.setFont(new Font("Default", Font.PLAIN,100));
		gsub.setColor(Color.WHITE);
		if(tank1.getLife()<0){
			gsub.drawString("Player 2 Win!", (int) (getSize().getWidth()/2)-300, (int) (getSize().getHeight()/2));
		}else if(tank2.getLife()<0){
			gsub.drawString("Player 1 Win!", (int) (getSize().getWidth()/2)-300, (int) (getSize().getHeight()/2));
		}
		
		if(System.currentTimeMillis() - startime <2000){
			gsub.setFont(new Font("Default", Font.PLAIN,50));
			gsub.drawString("control: WASD-T   Arrow-L", (int) (getSize().getWidth()/2)-400, (int) (getSize().getHeight()/2));
		}
		
		miniMap = bimg.getScaledInstance(mapW/8, mapL/8, Image.SCALE_DEFAULT);
		gsub.drawImage(miniMap, ((int)getSize().getWidth()/2)-(mapW/16), (int)getSize().getHeight()-(mapL/8), null);
		
		g2.dispose();
		gsub.dispose();
		g.drawImage(tankSI, 0, 0, this);
	}
	
	@Override
	public void run(){
		long lastTime = System.nanoTime();
		double nsPerUpdate = 1000000000D / 60D;
		
		int frames = 0;
		int updates= 0;
		
		double delta = 0;
		long lastTimer = System.currentTimeMillis();
		
		oldtimer = System.currentTimeMillis();
		startime = oldtimer + 3000;
		
		while(running){
			long now = System.nanoTime();
			delta += (now-lastTime)/nsPerUpdate;
			lastTime = now;
			boolean shouldDraw = false; 
			
			while(delta >= 1){
				updates++;
				update();
				delta--;
				shouldDraw = true;
			}
			
			if(shouldDraw){
				frames++;
				repaint();
			}
			
			if(System.currentTimeMillis() - lastTimer > 1000){
				lastTimer += 1000;
				System.out.println(updates + " updated, " + frames + " frames");
				updates = 0;
				frames = 0;
			}
			try{
				Thread.sleep(1);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public Graphics2D createGraphics2D(int w, int h){
		Graphics2D g2 = null;
		if(bimg == null || bimg.getWidth() != w || bimg.getHeight() != h){
			bimg = (BufferedImage) createImage(w, h);
		}
		g2 = bimg.createGraphics();
		g2.setBackground(getBackground());
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
		g2.clearRect(0, 0, w, h);
		return g2;
	}
	
	public Graphics2D createGraphics2DSI(int w, int h){
		Graphics2D g2 = null;
		if(tankSI == null || tankSI.getWidth() != w || tankSI.getHeight() != h){
			tankSI = (BufferedImage) createImage(w, h);
		}
		g2 = tankSI.createGraphics();
		g2.setBackground(getBackground());
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
		g2.clearRect(0, 0, w, h);
		return g2;
	}
	
	public void drawBG(int w, int h, Graphics g){
		int tileWidth = S.Background.getWidth(this);
		int tileHeight = S.Background.getHeight(this);
		
		int NumX = ( w / tileWidth );
		int NumY = ( h / tileHeight );
		
		for (int i = -1; i <= NumY; i++) {
            for (int j = 0; j <= NumX; j++) {
                g.drawImage(S.Background, j * tileWidth, i * tileHeight, tileWidth, tileHeight, this);
            }
        }
		
	}
	
	public BufferedImage tankView(Tank tank){
		BufferedImage tanksi;
		double subX = tank.getCenterX()-(getSize().getWidth()/4);
		double subY = tank.getCenterY()-(getSize().getHeight()/2);
		double subW = getSize().getWidth()/2;
		double subH = getSize().getHeight();
		double endW = bimg.getWidth() - tank.getCenterX();
		double endH = bimg.getHeight() - tank.getCenterY();
		double subX2 = bimg.getWidth()-subW;
		double subY2 = bimg.getHeight()-subH;
		
		if(tank.getCenterX() < subW/2){
			if(tank.getCenterY()< subH/2){
				tanksi = bimg.getSubimage(0, 0, (int)subW, (int)subH); //top left condition
			}else if(endH < subH/2){
				tanksi = bimg.getSubimage(0, (int)subY2, (int)subW, (int)subH); //bot
			}else{
				tanksi = bimg.getSubimage(0, (int)subY, (int)subW, (int)subH); //left
			}
		}else if(tank.getCenterY()< subH/2){
			if(endW < subW/2){
				tanksi = bimg.getSubimage((int)subX2, 0, (int)subW, (int)subH); //top right
			}else{
				tanksi = bimg.getSubimage((int)subX, 0, (int)subW, (int)subH); //top
			}
		}else if(endW < subW/2){
			if(endH < subH/2){
				tanksi = bimg.getSubimage((int)subX2, (int)subY2, (int)subW, (int)subH); //bot right
			}else{
				tanksi = bimg.getSubimage((int)subX2, (int)subY, (int)subW, (int)subH); //right
			}
		}else if(endH < subH/2){
			tanksi = bimg.getSubimage((int)subX, (int)subY2, (int)subW, (int)subH); //bot
		}else{
			tanksi = bimg.getSubimage((int)subX, (int)subY, (int)subW, (int)subH); //mid
		}
		
		return tanksi;
	}

	public static void main(String [] args){
		TankGame TG = new TankGame();
		TG.init();
		
		frame = new JFrame("Tank Game");
		frame.getContentPane().add("Center", TG);
		frame.pack();
		frame.setSize(WW, WL);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		TG.start();
	}
	
	
	public void Mapping(){
		mapping = new int[][]
				//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
				{{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}, //0
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,2}, //1
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,2}, //2
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,2}, //3
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,2}, //4
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,1,1,0,0,0,0,2}, //5
				 {2,1,1,1,1,2,2,2,2,2,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,1,1,0,0,0,0,2}, //6
				 {2,1,1,1,1,2,2,2,2,2,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,1,1,0,0,0,0,2}, //7
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,1,1,0,0,0,0,2}, //8
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //9
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //0
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //1
				 {2,0,0,0,0,2,2,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,2,2,0,0,0,0,2}, //2
				 {2,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,2}, //3
				 {2,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,2}, //4
				 {2,0,0,1,1,1,1,1,1,2,2,2,2,2,1,1,1,1,2,2,2,2,2,1,1,1,1,1,1,0,0,2}, //5
				 {2,0,0,1,1,1,1,1,1,2,2,2,2,2,1,1,1,1,2,2,2,2,2,1,1,1,1,1,1,0,0,2}, //6
				 {2,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,2}, //7
				 {2,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,2}, //8
				 {2,0,0,0,0,2,2,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,2,2,0,0,0,0,2}, //9
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //0
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //1
				 {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //2
				 {2,0,0,0,0,1,1,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //3
				 {2,0,0,0,0,1,1,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,2,2,2,2,2,1,1,1,1,2}, //4
				 {2,0,0,0,0,1,1,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,2,2,2,2,2,1,1,1,1,2}, //5
				 {2,0,0,0,0,1,1,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //6
				 {2,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //7
				 {2,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //8
				 {2,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //9
				 {2,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}, //0
				 {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}, //1
				};
		
		for(int i=0; i<mapping.length; i++){
			for(int j=0; j<mapping[i].length; j++){
				if(mapping[i][j]==2){
					wallList.add(new Wall(S.blueWall, i*32, j*32, false));
				}else if(mapping[i][j]==1){
					wallList.add(new Wall(S.redWall, i*32, j*32, true));
				}
			}
		}
	}
}
