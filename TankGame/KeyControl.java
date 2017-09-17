package TankGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyControl extends KeyAdapter {

	GameEvents GE;
	
	public KeyControl(GameEvents GE){
		this.GE = GE;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		GE.setValue(e, 1);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		GE.setValue(e, 2);
	}

	
	
}
