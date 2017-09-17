package TankGame;

import java.awt.Rectangle;

abstract class GameObject {
	
	public abstract int getX();
	public abstract int getY();
	public abstract int getW();
	public abstract int getH();
	public abstract int getXoffset();
	public abstract int getYoffset();
	public abstract String getName();
	
	public boolean isCollision(GameObject obj) {
		if(getBounds().intersects(obj.getBounds())){
			return true;
		}
		return false;
	}
	
	public Rectangle getBounds(){
		return new Rectangle(getX()+getXoffset(), getY()+getYoffset(), getW()-(2*getXoffset()), getH()-(2*getYoffset()));
	}
}
