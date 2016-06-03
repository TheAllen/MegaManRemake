package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Player extends MapObject{

	private Animation animation;
	
	//player stuff
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	//FireBalls
	private ArrayList<FireBall> fireBalls;
	private int fireCost;
	private int fireDamage;
	private boolean firing;
	
	//attacking
	private boolean attacking;
	private int attackDamage;
	private int attackRange;
	
	//double jump
	private boolean doubleJump;
	private boolean alreadyDoubleJump;
	private double doubleJumpStart;
	
	//charging
	private boolean charging;
	private int chargeTick;
	
	//gliding
	private boolean gliding;
	
	//animation
	private ArrayList<BufferedImage[]> sprites;
	
	//numFrames
	private final int[] numFrames = {6, 10, 1, 1, 2, 9, 10};
	
	//FrameWidths
	private final int[] frameWidths = {34, 34, 22, 23, 38, 43, 63};
	
	//frameHeight
	private final int[] frameHeights = {36, 36, 46, 41, 31, 36, 55};
	
	//enums
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int CHARGING = 4;
	private static final int FIREBALL = 5;
	private static final int ATTACKING = 6;
	
	public Player(TileMap tileMap){
		
		super(tileMap);
		
		width = 34;
		height = 36;
		cwidth = 30;
		cheight = 30;
		
		moveSpeed = 0.3;
		maxSpeed = 2.6;
		stopSpeed = 0.4;
		jumpStart = -4.8;
		doubleJumpStart = -3.0;
		stopJumpSpeed = 0.3;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		
		facingRight = true;
		
		health = maxHealth = 5;
		fire = maxFire = 25;
		
		//fireBalls
		fireBalls = new ArrayList<FireBall>();
		fireDamage = 5;
		fireCost = 2;
		
		//attacking
		attackRange = 65;
		attackDamage = 8;
		
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/armor4.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			int count = 0;
			for(int i = 0; i < numFrames.length; i++){
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++){
					if(i == 6){
						bi[j] = spritesheet.getSubimage(j * frameWidths[i], count + 10, frameWidths[i], frameHeights[i]);
					}
				    else{
					   bi[j] = spritesheet.getSubimage(j * frameWidths[i], count, frameWidths[i], frameHeights[i]);
				    }
				}
				count += frameHeights[i];
				sprites.add(bi);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);	
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getMaxHealth(){
		return maxHealth;
	}
	
	public int getFire(){
		return fire;
	}
	
	public int getMaxFire(){
		return maxFire;
	}
	
	public void setFiring(){
		firing = true; 
	}
	
	public void setAttacking(){
		attacking = true;
	}
	
	//implement doubleJump 
	public void setJumping(boolean b){
		if(b && falling  && !alreadyDoubleJump){
			doubleJump = true;
		}
		jumping = b;
	}
	
	//charging
	public void setCharging(){
		charging = true;
		chargeTick = 500;
		if(facingRight) dx = -moveSpeed * (3 - chargeTick * 0.07);
		else dx =  moveSpeed * (3 - chargeTick * 0.07);
	}
	
	public void setGliding(boolean b){
		gliding = b;
	}

	public void hit(int damage){
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void checkAttack(ArrayList<Enemy> enemies){
		
		for(int i = 0; i < enemies.size(); i++){
			
			Enemy e = enemies.get(i);
			
			if(attacking){
				if(facingRight){
					if(e.getX() > x && 
					e.getX() < x + attackRange &&
					e.getY() > y - height / 2 && 
					e.getY() < y + height / 2){
					 e.hit(attackDamage);
					}
				}
				else{
					if(e.getX() < x &&
						e.getX() > x - attackRange &&
						e.getY() > y - height / 2 &&
						e.getY() < y + height / 2){
						 e.hit(attackDamage);
					}
				}
			}
			
			for(int j = 0; j < fireBalls.size(); j++){
				if(fireBalls.get(i).intersects(e)){
					e.hit(fireDamage);
					fireBalls.get(i).setHit();
					break;
				}
			}
			
			if(intersects(e)){
				hit(e.getDamage());
			}
		}
	}
	
	
	public void getNextPosition(){
		
		if(left){
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx = -maxSpeed;
			}
			if(charging){
				dx =  moveSpeed * (3 - chargeTick * 0.07);
			}
		}
		
		else if(right){
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
			if(charging){
				dx = -moveSpeed * (3 - chargeTick * 0.07);
			}
		}
		
		else if(dx < 0){
			dx += stopSpeed;
			if(dx > 0){
				dx = 0;
			}
		}
		
		else if(dx > 0){
			dx -= stopSpeed;
			if(dx < 0){
				dx = 0;
			}
		}
		
		if(currentAction == ATTACKING || currentAction == FIREBALL && !(jumping || falling)){
			
			dx = 0;
		}
		
		//charging
		
		
		if(jumping && !falling){
			dy = jumpStart;
			falling = true;
		}
		
		if(doubleJump){
			dy = doubleJumpStart;
			alreadyDoubleJump = true;
			doubleJump = false;
		}
		
		if(!falling){
			alreadyDoubleJump = false;
		}
		
		if(falling){
			
			dy += fallSpeed;
			if(dy > 0){
				jumping = false;
			}
			
			if(dy < 0 && !jumping){
				dy += stopJumpSpeed;
			}
			
			if(dy > maxFallSpeed){
				dy = maxFallSpeed;
			}
			

		

		}
	}
	
	public void update(){
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(currentAction == ATTACKING){
			if(animation.hasPlayedOnce()){
				attacking = false;
			}
		}
		
		if(currentAction == FIREBALL){
			if(animation.hasPlayedOnce()){
				firing = false;
			}
		}
		
		if(currentAction == CHARGING){
			if(animation.hasPlayedOnce()) charging = false;
		}
		
		fire += 1;
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL){
			if(fire > fireCost){
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				//FireBall fb1 = new FireBall(tileMap, facingRight);
				//FireBall fb2 = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y);
				//fb1.setPosition(x + 35, y);
				//fb2.setPosition(x + 50, y);
				fireBalls.add(fb);
				//fireBalls.add(fb1);
				//fireBalls.add(fb2);
			}
		}
		
		for(int i = 0; i < fireBalls.size(); i++){
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()){
				fireBalls.remove(i);
				i--;
			}
		}
		
		if(flinching){
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000){
				flinching = false;
			}
		}
		
		if(attacking){
			if(currentAction != ATTACKING){
				currentAction = ATTACKING;
				animation.setFrames(sprites.get(ATTACKING));
				animation.setDelay(20);
				width = 63;
				height = 55;
				//y = ytemp - 20;
			}
		}
		
		else if(firing){
			if(currentAction != FIREBALL){
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(20);
				width = 43;
				height = 36;
			}
		}
		
		//charging
		else if(charging){
			if(currentAction != CHARGING){
				currentAction = CHARGING;
				animation.setFrames(sprites.get(CHARGING));
				animation.setDelay(100);
				width = 38;
				height = 31;
			}
		}
		
		else if(dy < 0){
			if(currentAction != JUMPING){
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 22;
				height = 46;
			}
		}
		
		else if(dy > 0){
			if(currentAction != FALLING){
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 23;
				height = 41;
			}
		}
		
		else if(left || right){
			if(currentAction != WALKING){
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 34;
				height = 36;
			}
		}
		
		else {
			if(currentAction != IDLE){
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(300);
				width = 34;
				height = 36;
			}
		}
		
		
		animation.update();
		if(currentAction != ATTACKING && currentAction != FIREBALL){
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}
	
	public void draw(Graphics2D g){
		
		setMapPosition();
		
		for(int i = 0; i < fireBalls.size(); i++){
			fireBalls.get(i).draw(g);
		}
		
		if(flinching){
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0){
				return;
			}
		}
		
		if(facingRight){
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
		}
		else{
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
		}
	}
}
