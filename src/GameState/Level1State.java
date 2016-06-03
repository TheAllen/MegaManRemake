package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Player;
import Entity.Enemies.Goomba;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState{

	private TileMap tileMap;
	private Background bg;
	private Player player;
	
	private ArrayList<Enemy> enemies;
	
	public Level1State(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}
	
	public void init(){
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.07);
		
		bg = new Background("/Backgrounds/forestbg.jpg", 0.1);
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
		enemies = new ArrayList<Enemy>();
		
		Goomba g;
		
		g = new Goomba(tileMap);
		g.setPosition(200, 100);
		enemies.add(g);
	}
	
	public void update(){
		
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		player.checkAttack(enemies);
		
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).update();
			if(enemies.get(i).isDead()){
				enemies.remove(i);
				i--;
			}
		}
		
	}
	
	public void draw(Graphics2D g){
		
		bg.draw(g);
		tileMap.draw(g);
		player.draw(g);
		
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).draw(g);
		}
	}
	
	public void keyPressed(int k){
		
		if(k == KeyEvent.VK_A) player.setLeft(true);
		if(k == KeyEvent.VK_D) player.setRight(true);
		//if(k == KeyEvent.VK_W) player.setUp(true); 
		if(k == KeyEvent.VK_S) player.setDown(true);
		
		if(k == KeyEvent.VK_W) player.setJumping(true);
		//change to flying
		if(k == KeyEvent.VK_K) player.setCharging();
		if(k == KeyEvent.VK_J) player.setAttacking();
		if(k == KeyEvent.VK_L) player.setFiring();
		
	}
	
	public void keyReleased(int k){
		
		if(k == KeyEvent.VK_A) player.setLeft(false);
		if(k == KeyEvent.VK_D) player.setRight(false);
		if(k == KeyEvent.VK_W) player.setUp(false); 
		if(k == KeyEvent.VK_S) player.setDown(false);
		
		if(k == KeyEvent.VK_SPACE) player.setJumping(false);
		
		if(k == KeyEvent.VK_K) player.setGliding(false);
	}
}
