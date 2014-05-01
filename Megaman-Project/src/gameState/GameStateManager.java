package gameState;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameStateManager
{
	private ArrayList<GameState> gameStates;
	private int currentState;

	// All the IDs of the states. Get the state at the ID from the array to get the correct state.
	public static final int START_GAME_STATE = 0;
	public static final int TEST_LEVEL_STATE = 1;
	public static final int LEVEL_MENU_STATE = 2;

	public GameStateManager()
	{
		gameStates = new ArrayList<GameState>();

		currentState = START_GAME_STATE;
		gameStates.add(new StartMenuState(this));
		
		// Initiate ALL THE GAMESTATES. Literally all of them.
		gameStates.add(new TestLevelState(this));
	}

	public void setState(int newState)
	{
		currentState = newState;
		gameStates.get(currentState).init();
	}

	public void update()
	{
		gameStates.get(currentState).update();
	}

	public void draw(Graphics2D graphics)
	{
		gameStates.get(currentState).draw(graphics);
	}
}
