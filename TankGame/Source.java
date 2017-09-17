package TankGame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Source {

	public BufferedImage Background;
	public BufferedImage blueWall;
	public BufferedImage redWall;
	public BufferedImage BlueTankStrip;
	public BufferedImage RedTankStrip;
	public BufferedImage ShellBasicStrip;
	public BufferedImage smallExpStrip;
	public BufferedImage largeExpStrip;
	public BufferedImage PowerUpStrip;
	public BufferedImage RocketStrip;
	public BufferedImage WeaponStrip;
	
	public BufferedImage BlueTank [] = new BufferedImage[60];
	public BufferedImage RedTank [] = new BufferedImage[60];
	public BufferedImage ShellBullet [] = new BufferedImage[60];
	public BufferedImage smallExp [] = new BufferedImage[6];
	public BufferedImage largeExp [] = new BufferedImage[7];
	public BufferedImage PowerUp [] = new BufferedImage[4];
	public BufferedImage Rocket [] = new BufferedImage[60];
	public BufferedImage Weapon [] = new BufferedImage[3];
	
	public AudioClip BGM;
	public AudioClip expSmall;
	public AudioClip expLarge;
	
	public Source(){
		try {
			Background = ImageIO.read(getClass().getResource("Res/Background.png"));
			BlueTankStrip = ImageIO.read(getClass().getResource("Res/Tank_blue_basic_strip60.png"));
			RedTankStrip = ImageIO.read(getClass().getResource("Res/Tank_red_basic_strip60.png"));
			blueWall = ImageIO.read(getClass().getResource("Res/Blue_wall1.png"));
			redWall = ImageIO.read(getClass().getResource("Res/Wall1.png"));
			ShellBasicStrip = ImageIO.read(getClass().getResource("Res/Shell_basic_strip60.png"));
			smallExpStrip = ImageIO.read(getClass().getResource("Res/Explosion_small_strip6.png"));
			largeExpStrip = ImageIO.read(getClass().getResource("Res/Explosion_large_strip7.png"));
			PowerUpStrip = ImageIO.read(getClass().getResource("Res/Pickup_strip4.png"));
			RocketStrip = ImageIO.read(getClass().getResource("Res/Rocket_strip60.png"));
			WeaponStrip = ImageIO.read(getClass().getResource("Res/Weapon_strip3.png"));
			
			BGM = Applet.newAudioClip(getClass().getResource("Res/Music.mid"));
			expSmall = Applet.newAudioClip(getClass().getResource("Res/Explosion_small.wav"));
			expLarge = Applet.newAudioClip(getClass().getResource("Res/Explosion_large.wav"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i=0; i<60; i++){
			BlueTank[i] = BlueTankStrip.getSubimage(i*64, 0, 64, 64);
			RedTank[i] = RedTankStrip.getSubimage(i*64, 0, 64, 64);
			ShellBullet[i] = ShellBasicStrip.getSubimage(i*24, 0, 24, 24);
			Rocket[i] = RocketStrip.getSubimage(i*24, 0, 24, 24);
		}
		for(int i=0; i<6; i++){
			smallExp[i] = smallExpStrip.getSubimage(i*32, 0, 32, 32);
		}
		for(int i=0; i<7; i++){
			largeExp[i] = largeExpStrip.getSubimage(i*64, 0, 64, 64);
		}
		for(int i=0; i<4; i++){
			PowerUp[i] = PowerUpStrip.getSubimage(i*32, 0, 32, 32);
		}
		for(int i=0; i<3; i++){
			Weapon[i] = WeaponStrip.getSubimage(i*16, 0, 16, 16);
		}
		
	}
	
}
