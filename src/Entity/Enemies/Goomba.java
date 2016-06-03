package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import Entity.Animation;
import TileMap.TileMap;
import Entity.*;

public class Goomba extends Enemy{

	private BufferedImage[] sprites;
	
	public Goomba(TileMap tileMap){
		
		super(tileMap);
		
		width = 30;
		height = 30;
		cwidth = 30;
		cheight = 25;
		
		health = maxHealth = 10;
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/slugger.gif"));
			
			sprites = new BufferedImage[3];
			
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(200);
		
		right = true;
		facingRight = true;
		
	}
	
	public void getNextPosition(){
		
		if(left){
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx = -maxSpeed;
			}
		}
		
		else if(right){
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}
		
		if(falling){
			dy += fallSpeed;
			if(dy > maxFallSpeed){
				dy = maxFallSpeed;
			}
		}
	}
	
	public void update(){
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check flinching
		if(flinching){
			long elapsed  = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400){
				flinching = false;
			}
		}
		
		//check if hitting walls 
		if(right && dx == 0){
			right = false;
			left = true;
			facingRight = true;
		}
		
		if(left && dx == 0){
			right = true;
			left = false;
			facingRight = false;
		}
		
		animation.update();
	}
	
	public void draw(Graphics2D g){
		
		setMapPosition();
		
		super.draw(g);
	}
}
