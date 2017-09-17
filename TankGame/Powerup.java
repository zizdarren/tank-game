package TankGame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Powerup extends GameObject{

	Image pu;
	int x, y, w, h, Xoffset, Yoffset, spd;
	boolean touchable;
	
	public Powerup(Image pu, int x, int y, int spd){
		this.pu = pu;
		this.x = x;
		this.y = y;
		this.w = pu.getWidth(null);
		this.h = pu.getHeight(null);
		this.spd = spd;
		
		Xoffset = 0;
		Yoffset = 0;
	}
	
	public void update(){
		y += spd;
	}
	
	public void draw(Graphics g){
		g.drawImage(pu, x, y, null);
	}
	
	@Override
	public int getX(){return x;}

	@Override
	public int getY(){return y;}
	
	@Override
	public int getW(){return w;}
	
	@Override
	public int getH(){return h;}
	
	@Override
	public int getXoffset(){return Xoffset;}
	
	@Override
	public int getYoffset(){return Yoffset;}

	@Override
	public String getName() {
		return "Powerup";
	}
}
