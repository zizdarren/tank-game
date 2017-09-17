package TankGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {

	Source S;
	BufferedImage bullet[];
	int w, h, Xoffset, Yoffset;
	int angle, type, id, index;
	double x, y, centerX, centerY, Xspd, Yspd;
	
	
	public Bullet(Source S, double cx, double cy, int angle, int type, int id){
		this.S = S;
		this.centerX = cx;
		this.centerY = cy;
		this.w = this.h = 24;
		this.type = type;
		if(type == 1){
			this.bullet = S.ShellBullet;
		}else if(type == 2){
			this.bullet = S.Rocket;
		}
		this.id = id;
		this.x = centerX-(w/2);
		this.y = centerY-(h/2);
		this.angle = angle;
		
		Xoffset = 3;
		Yoffset = 3;
		
		index = this.angle/6 ;
		
		Xspd = Math.cos(Math.toRadians(this.angle));
		Yspd = -Math.sin(Math.toRadians(this.angle));
	}
	
	public void update(){
		index = angle/6;
		y += 4*Yspd;
		x += 4*Xspd;
	}
	
	public void draw(Graphics g){
		g.drawImage(bullet[index], (int)x, (int)y, null);
	}
	
	@Override
	public int getX() {return (int)x;}

	@Override
	public int getY() {return (int)y;}

	@Override
	public int getW() {return w;}

	@Override
	public int getH() {return h;}
	
	public int getID() {return id;}
	
	@Override
	public int getXoffset(){return Xoffset;}
	
	@Override
	public int getYoffset(){return Yoffset;}
	
	public int getType(){return type;}
	
	@Override
	public String getName(){return "Bullet";}
}
