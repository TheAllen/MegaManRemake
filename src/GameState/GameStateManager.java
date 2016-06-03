package GameState;

import java.awt.Graphics2D;

public class GameStateManager{
	
	private GameState[] gameState;
	
	private int currentState;
	
	public static final int NUMSTATES = 2;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	
	public GameStateManager(){
		
		gameState = new GameState[NUMSTATES];
		currentState = MENUSTATE;
		
		loadState(currentState);
	}
	
	public void loadState(int state){
		
		if(state == MENUSTATE){
			gameState[state] = new MenuState(this);
		}
		
		if(currentState == LEVEL1STATE){
			gameState[state] = new Level1State(this);
		}
	}
	
	public void unloadState(int state){
		
		gameState[state] = null;
	}
	
	public void setState(int state){
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public void update(){
	
		try{
			gameState[currentState].update();
		}catch(Exception e){
			
		}
	}
	
	public void draw(Graphics2D g){
		try{
			gameState[currentState].draw(g);
		}catch(Exception e){
			
		}
	}
	
	public void keyPressed(int k){
		gameState[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k){
		gameState[currentState].keyReleased(k);
	}
}