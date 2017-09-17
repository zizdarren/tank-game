package TankGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public class Tank extends Vehicle implements Observer{
	
	int  w, h, Xoffset, Yoffset, angle, TAngle, index, maxhp, hp, life, powerup;
	double x, y, Xspd, Yspd, centerX, centerY, startX, startY;
	boolean isForward, isBackward, leftRotate, rightRotate;
	boolean TopCol, BotCol, LCol, RCol, shooting, invinsible, alive;
	long timer;
	BufferedImage tank[];
	Source S;
	int ID;
	
	public Tank(Source S, int x, int y, int id){
		this.ID = id;	
		this.S = S;
		if(ID == 1){this.tank = S.BlueTank;}
		else if(ID == 2){this.tank = S.RedTank;}
		this.x = startX = x;
		this.y = startY = y;
		this.w = this.h = 64;
		Xoffset = Yoffset = 7;   ////offset is actually 7
		timer = System.currentTimeMillis();
		
		invinsible = false;
		life = 3;
		maxhp = 30;
		
		reset();
	}
	
	public void reset(){
		this.x = startX;
		this.y = startY;
		centerX = x+(w/2);
		centerY = y+(h/2);
		hp = maxhp;
		alive = true;
		shooting = false;
		isForward = false;
		isBackward = false;
		angle = TAngle = 0;
		index = 0;
		powerup = 0;
	}

	@Override
	public void shoot(){
		if(alive){
			long newtimer = System.currentTimeMillis();
			if(newtimer - timer >=200){
				if(powerup>0){
					TankGame.tankBullet.add(new Bullet(S, centerX, centerY, TAngle, 2, ID));
					powerup--;
				}else{
					TankGame.tankBullet.add(new Bullet(S, centerX, centerY, TAngle, 1, ID));
				}
			}
			timer = newtimer;
		}
	}


	@Override
	public void detonate(int x) {
		S.expSmall.play();
		if(x == 1){
			hp--;
		}
		else if(x ==2){
			hp -= 3;
		}
		if(hp <= 0){
			life--;
			alive = false;
			TankGame.explosion.add(new Explosion(S.largeExp, (int)this.x, (int)this.y, 2));
			S.expLarge.play();
			if(ID == 2){
				TankGame.score1++;
			}else if(ID ==1){
				TankGame.score2++;
			}
		}
	}

	@Override
	public Boolean isAlive() {
		return alive;
	}
	
	public void update(){
		movement();
		x += 2*Xspd;
		y += 2*Yspd;
		centerX = x+(w/2);
		centerY = y+(h/2);
		TopCol = false;
		BotCol = false;
		LCol = false;
		RCol = false;
		if(angle%6 ==0){TAngle = angle;}
		if(!alive && life>=0){alive = true; reset();}
	}
	
	@Override
	public void update(Observable obs, Object arg) {
		GameEvents ge = (GameEvents) arg;
		if(ge.getType() == 1 || ge.getType() == 2){
			KeyEvent e = (KeyEvent)ge.getEvent();
			int c = e.getKeyCode();
			
			if(alive){
			if(ID == 1){
				if(c == KeyEvent.VK_W){
					if(ge.getType() == 1)
						isForward = true;
					else if(ge.getType() == 2)
						isForward = false;
				}
				if(c == KeyEvent.VK_S){
					if(ge.getType() == 1)
						isBackward = true;
					else if(ge.getType() == 2)
						isBackward = false;
				}
				if(c == KeyEvent.VK_A){
					if(ge.getType() == 1)
						leftRotate = true;
					else if(ge.getType() == 2)
						leftRotate = false;
				}
				if(c == KeyEvent.VK_D){
					if(ge.getType() == 1)
						rightRotate = true;
					else if(ge.getType() == 2)
						rightRotate = false;
				}
				if(c == KeyEvent.VK_T){
					if(ge.getType() == 1 && !shooting){
						shooting = true;
						shoot();
					}else if(ge.getType() == 2 && shooting){
						shooting = false;
					}	
				}
			}
			
			if(ID ==2){
				if(c == KeyEvent.VK_UP){
					if(ge.getType() == 1)
						isForward = true;
					else if(ge.getType() == 2)
						isForward = false;
				}
				if(c == KeyEvent.VK_DOWN){
					if(ge.getType() == 1)
						isBackward = true;
					else if(ge.getType() == 2)
						isBackward = false;
				}
				if(c == KeyEvent.VK_LEFT){
					if(ge.getType() == 1)
						leftRotate = true;
					else if(ge.getType() == 2)
						leftRotate = false;
				}
				if(c == KeyEvent.VK_RIGHT){
					if(ge.getType() == 1)
						rightRotate = true;
					else if(ge.getType() == 2)
						rightRotate = false;
				}
				if(c == KeyEvent.VK_L){
					if(ge.getType() == 1 && !shooting){
						shooting = true;
						shoot();
					}else if(ge.getType() == 2 && shooting){
						shooting = false;
					}	
				}
			}	
			}
		}
		
		if(ge.getType() == 3){
			GameObject obj = (GameObject)ge.getEvent();
			if(this.isCollision(obj)){
				if(obj.getName().equals("Wall")){
					Rectangle overlap = this.getBounds().intersection(obj.getBounds());
					if(centerX>overlap.getCenterX()){
						if(centerY>overlap.getCenterY()){
							if(overlap.getWidth()<overlap.getHeight()){
								LCol = true;
							}else if(overlap.getWidth()>overlap.getHeight()){
								TopCol = true;
							}else{
								LCol = false;
								TopCol = false;
							}
						}else if(centerY<overlap.getCenterY()){
							if(overlap.getWidth()<overlap.getHeight()){
								LCol = true;
							}else if(overlap.getWidth()>overlap.getHeight()){
								BotCol = true;
							}else{
								LCol = false;
								BotCol = false;
							}
						}
					
					}else if(centerX<overlap.getCenterX()){
						if(centerY>overlap.getCenterY()){
							if(overlap.getWidth()<overlap.getHeight()){
								RCol = true;
							}else if(overlap.getWidth()>overlap.getHeight()){
								TopCol = true;
							}else{
								RCol = false;
								TopCol = false;
							}
						}else if(centerY<overlap.getCenterY()){
							if(overlap.getWidth()<overlap.getHeight()){
								RCol = true;
							}else if(overlap.getWidth()>overlap.getHeight()){
								BotCol = true;
							}else{
								RCol = false;
								TopCol = false;
							}
						}
					}	
				}else if(obj.getName().equals("Bullet")){
					Bullet b = (Bullet) obj;
					if(this.isCollision(b)){
						TankGame.explosion.add(new Explosion(S.smallExp, b.getX(), b.getY(), 1));
						detonate(b.getType());
					}
				}
			}
		}
	}
	
	public void draw(Graphics g){
		if(alive){
			g.drawImage(tank[index], (int)x, (int)y, null);
			g.setColor(Color.GREEN);
			g.drawRect((int)x, (int)y+h, maxhp*2, 6);
			g.fillRect((int)x, (int)y+h, hp*2, 6);
			if(powerup > 0){
				g.drawImage(S.Weapon[0], (int)x+ maxhp*2 +3, (int)y+h-5, null);
				g.setColor(Color.WHITE);
				g.drawString(powerup+"", (int)x+ maxhp*2 +S.Weapon[0].getWidth()+4, (int)y+h+8);
			}
		}
	}
	
	public void movement(){
		if(leftRotate){leftRotate();}
		if(rightRotate){rightRotate();}
		
		if(isForward){
			forward();
		}else if(isBackward){
			backward();
		}else{
			stopped();
		}
		
	}
	
	public void forward(){
		Xspd = Math.cos(Math.toRadians(TAngle));
		Yspd = -Math.sin(Math.toRadians(TAngle));
		if((LCol && Xspd<0)||(RCol && Xspd>0)){
			Xspd = 0;
		}
		if((TopCol && Yspd<0)||(BotCol && Yspd>0)){
			Yspd = 0;
		}
	}
	
	public void backward(){
		Xspd = -Math.cos(Math.toRadians(TAngle));
		Yspd = Math.sin(Math.toRadians(TAngle));
		if((LCol && Xspd<0)||(RCol && Xspd>0)){
			Xspd = 0;
		}
		if((TopCol && Yspd<0)||(BotCol && Yspd>0)){
			Yspd = 0;
		}
	}
	
	public void stopped(){
		Xspd = 0;
		Yspd = 0;
	}
	
	public void leftRotate(){
		if(angle>=360){ angle = 0;}
		angle += 2;
		if(angle == 360){angle = 0;}
		if(angle%6==0 && angle !=TAngle){
			TAngle = angle;
			if(index >= tank.length-1){index = -1;}
			index = angle/6;
		}
	}
	
	public void rightRotate(){
		if(angle<=0){ angle = 360;}
		angle -= 2;
		if(angle == 360){angle = 0;}
		if(angle%6==0 && angle != TAngle){
			TAngle = angle;
			if(index <= 0){index = 60;}
			index = angle/6;
		}
	}

	public void PowerUp(){
		powerup = 10;
	}
	
	public int getLife(){
		return life;
	}
	
	@Override
	public int getX() {return (int)x;}

	@Override
	public int getY() {return (int)y;}

	@Override
	public int getW() {return w;}

	@Override
	public int getH() {return h;}

	@Override
	public int getXoffset() {return Xoffset;}

	@Override
	public int getYoffset() {return Yoffset;}

	@Override
	public String getName(){return "Tank";}
	
	public double getCenterX(){return centerX;}
	
	public double getCenterY(){return centerY;}

}
