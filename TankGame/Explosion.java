package TankGame;

import java.awt.Graphics;
import java.awt.Image;

public class Explosion extends GameObject{

	Image explode[];
	int x, y;
	int index, max;
	long oldtime, newtime;
	boolean finish;
	
	public Explosion(Image image[], int x, int y, int type){
		this.x = x;
		this.y = y;
		index = 0;
		if(type == 1){
			max = 6;
			explode = image;
		}
		if(type == 2){
			max = 7;
			explode = image;
		}
		oldtime = System.currentTimeMillis();
		finish = false;
	}
	
	public void draw(Graphics g){
		newtime = System.currentTimeMillis();
		if(newtime - oldtime <=100){
			if(index <max){
				g.drawImage(explode[index], x, y, null);
			}
		}else{
			g.drawImage(explode[index], x, y, null);
			index++;
			oldtime = newtime;
			newtime = System.currentTimeMillis();
		}
		if(index == max){
			finish = true;
		}
	}
	
	public boolean done(){return finish;}
	
	@Override
	public int getX() {return x;}

	@Override
	public int getY() {return y;}

	@Override
	public int getW() {return 0;}

	@Override
	public int getH() {return 0;}
	
	@Override
	public int getXoffset() {return 0;}

	@Override
	public int getYoffset() {return 0;}

	@Override
	public String getName() {
		return "Explosion";
	}
	
}
