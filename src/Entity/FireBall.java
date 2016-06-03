package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class FireBall extends MapObject{
	
	private boolean hit;
	private boolean remove;
	
	private BufferedImage[] sprites;
	private BufferedImage[] hitsprites;

	public FireBall(TileMap tileMap, boolean right){
		super(tileMap);
		
		//set the fireBalls to the right direction
		facingRight = right;
		
		width = 30;
		height = 30;
		
		cwidth = 15;
		cheight = 15;
		
		moveSpeed = 4.8;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/fb.gif"));
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
			hitsprites = new BufferedImage[3];
			for(int i = 0;  i < hitsprites.length; i++){
				hitsprites[i] = spritesheet.getSubimage(i * width, height, width, height);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setHit(){
		if(hit) return;
		hit = true;
		animation.setFrames(hitsprites);
		animation.setDelay(70);
		dx = 0;
	}
	
	public boolean shouldRemove(){
		return remove;
	}
	
	public void update(){
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx ==0 && !hit){
			setHit();
		}
		animation.update();
		if(hit && animation.hasPlayedOnce()){
			remove = true;
		}
	}
	
	public void draw(Graphics2D g){
		
		setMapPosition();
		
		if(facingRight){
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
		}
		else{
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
		}
	}
}
