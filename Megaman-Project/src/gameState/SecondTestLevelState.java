package gameState;

import java.util.ArrayList;

import sprites.enemies.*;
import sprites.Enemy;
import sprites.HUD;
import sprites.Player;
import tileMap.Background;
import tileMap.TileMap;

public class SecondTestLevelState extends LevelState
{
	protected HUD theHud;
	
	public SecondTestLevelState(GameStateManager theGameStateManager)
	{
		this.stateManager = theGameStateManager;
		init();
	}

	// All the important stuff goes in here.
	public void init()
	{
		tileMap = new TileMap(16);
		tileMap.loadTiles("caveAndMetal.gif", 0);
		tileMap.loadMap("megamantestmap3");
		tileMap.setPosition(0, 0);

		backgroundImage = new Background("grassbg1.gif", 0.1);

		mainCharacter = new Player(tileMap);
		mainCharacter.setPosition(50, 50);

		enemyList = new ArrayList<Enemy>();
		Met firstMet = new Met(tileMap, mainCharacter);
		firstMet.setPosition(150, 100); //DO THIS COORRRREEECCCTTT
		enemyList.add(firstMet);
		//theHud = new HUD(mainCharacter);
	}
}