package TankGame;

import java.awt.event.KeyEvent;
import java.util.Observable;

public class GameEvents extends Observable {

	private int type;
	private Object event;
	
	public void setValue(KeyEvent e, int n){
		type = n;
		event = e;
		setChanged();
		notifyObservers(this);
	}
	
	public void collision(GameObject e){
		type = 3;
		event = e;
		setChanged();
		notifyObservers(this);
	}
	
	public void powerup(GameObject e){
		type = 4;
		event = e;
		setChanged();
		notifyObservers(this);
	}
		
	public int getType(){
		return type;
	}
	
	public Object getEvent(){
		return event;
	}
}
