package gameState;

import java.awt.Graphics2D;
import java.util.ArrayList;

import handlers.KeyInput;

import sprites.*;
import startUp.GamePanel;
import tileMap.Background;
import tileMap.TileMap;

public abstract class LevelState extends GameState
{
	// All levels have a tile map, a background, and the main character.
	protected TileMap tileMap;
	protected Background backgroundImage;
	protected Player mainCharacter;

	protected ArrayList<Enemy> enemyList;

	public void update()
	{
		// update everything
		backgroundImage.update();

		tileMap.setPosition(GamePanel.WIDTH / 2 - mainCharacter.getX(), GamePanel.HEIGHT / 2
				- mainCharacter.getY());
		tileMap.update();

		mainCharacter.update();

		// the arraylist is so that the enemyList array doesn't get smaller mid updating all enemies
		// - that would lead to some enemies not getting updated.
		ArrayList<Integer> removeTheseEnemies = new ArrayList<Integer>();
		for(int i = 0; i < enemyList.size(); i++)
		{
			enemyList.get(i).update();
			if(enemyList.get(i).shouldBeRemoved())
			{
				removeTheseEnemies.add(i);
			}
		}
		while(!removeTheseEnemies.isEmpty())
		{
			enemyList.remove(removeTheseEnemies.remove(0));
		}

		// Every frame, check the keys that have been pressed
		handleInput();
	}

	public void draw(Graphics2D graphics)
	{
		// draw background
		backgroundImage.draw(graphics);

		// Draw tilemap
		tileMap.draw(graphics);
		mainCharacter.draw(graphics);

		for(int eachEnemy = 0; eachEnemy < enemyList.size(); eachEnemy++)
		{
			enemyList.get(eachEnemy).draw(graphics);
		}
	}

	private void handleInput()
	{
		/**
		 * Check all the relevant keys, set the main character's 'do this' to true if button is
		 * held, false if not.
		 */
		if(KeyInput.isPressed(KeyInput.key_LEFT))
			mainCharacter.setLeft(true);
		else
			mainCharacter.setLeft(false);

		if(KeyInput.isPressed(KeyInput.key_RIGHT))
			mainCharacter.setRight(true);
		else
			mainCharacter.setRight(false);

		if(KeyInput.isPressed(KeyInput.key_UP))
			mainCharacter.setUp(true);
		else
			mainCharacter.setUp(false);

		if(KeyInput.isPressed(KeyInput.key_DOWN))
			mainCharacter.setDown(true);
		else
			mainCharacter.setDown(false);

		if(KeyInput.isPressed(KeyInput.key_JUMP))
			mainCharacter.setJump(true);
		else
			mainCharacter.setJump(false);

		if(KeyInput.isPressed(KeyInput.key_SHOOT))
			mainCharacter.setWeaponUse(true);
		else
		{
			mainCharacter.setWeaponUse(false);
			mainCharacter.setChargingWeapon(false);
		}

	}
}