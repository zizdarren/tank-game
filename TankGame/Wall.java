package TankGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Wall extends GameObject{

	BufferedImage iWall;
	int x, y, w, h, Xoffset, Yoffset;
	boolean destructible, destroyed;
	
	public Wall(BufferedImage iwall, int x, int y, boolean des){
		this.iWall = iwall;
		this.x = x;
		this.y = y;
		this.w = iWall.getWidth();
		this.h = iWall.getHeight();
		Xoffset = Yoffset = 0;
		
		this.destructible = des;
		destroyed = false;
	}
	
	public void update(){
		
	}
	
	public void draw(Graphics g){
		if(!destroyed){
			g.drawImage(iWall, x, y, null);
		}
	}
	
	public void setDestroy(boolean des){
		this.destroyed = des;
	}

	@Override
	public int getX() {return x;}

	@Override
	public int getY() {return y;}

	@Override
	public int getW() {return w;}

	@Override
	public int getH() {return h;}

	@Override
	public int getXoffset(){return Xoffset;}
	
	@Override
	public int getYoffset(){return Yoffset;}
	
	@Override
	public String getName(){return "Wall";}

}
