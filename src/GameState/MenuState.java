package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

public class MenuState extends GameState{
	
	private Background bg;
	
	private String[] options = {"Start", "Help", "Exit"};
	
	private int currentChoice = 0;
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;
		
		try{
			bg = new Background("/Backgrounds/menubg.gif", 10);
			bg.setVectors(0.3, 0);
			
			titleColor = new Color(0 , 128, 0);
			titleFont = new Font("Century Gothics", Font.PLAIN, 28);
			font = new Font("Century Gothics", Font.PLAIN, 12);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void init(){
		
	}
	
	public void update(){
		bg.update();
	}
	
	public void draw(Graphics2D g){
		
		bg.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Game", 100, 70);
		
		g.setFont(font);
		for(int i = 0; i < options.length; i++){
			if(i == currentChoice){
				g.setColor(Color.YELLOW);
			}
			else{
				g.setColor(Color.BLACK);
			}
			g.drawString(options[i], 145, 140 + 15 * i);
		}
		
	}
	
	private void select(){
		
		if(currentChoice == 0){
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		
		else if(currentChoice == 1){
			//help
		}
		
		else if(currentChoice == 2){
			System.exit(0);
		}
	}
	
	public void keyPressed(int k){
		
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		
		if(k == KeyEvent.VK_DOWN){
			currentChoice++;
			if(currentChoice == options.length){
				currentChoice = 0;
			}
		}
		
		if(k == KeyEvent.VK_UP){
			currentChoice--;
			if(currentChoice == -1){
				currentChoice = options.length - 1;
			}
		}
	}
	
	public void keyReleased(int k){
		
	}
}