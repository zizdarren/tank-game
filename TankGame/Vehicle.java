package TankGame;

abstract class Vehicle extends GameObject{

	public abstract void shoot();
	
	public abstract void detonate(int x);
	
	public abstract Boolean isAlive();
}
