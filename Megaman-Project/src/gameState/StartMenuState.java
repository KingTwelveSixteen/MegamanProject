package gameState;

import handlers.KeyInput;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import tileMap.Background;

public class StartMenuState extends GameState
{
	private Background backgroundImage;

	private int currentChoice = 0;
	private String[] options = {"Start", "Options", "Quit", "Test"};

	private Color titleColor;
	private Font titleFont;

	private Font choiceFont;

	public StartMenuState(GameStateManager theGameStateManager)
	{
		this.stateManager = theGameStateManager;
		init();
	}

	public void init()
	{
		try
		{
			backgroundImage = new Background("grassbg1.gif", 1);
			backgroundImage.setVector(-0.1, 0);

			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);

			choiceFont = new Font("Arial", Font.PLAIN, 12);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void select()
	{
		if(currentChoice == 0)
		{
			// stateManager.setState(GameStateManager.LEVEL_SELECT_STATE);
		}
		else if(currentChoice == 1)
		{

		}
		else if(currentChoice == 2)
		{

		}
		else
		{
			stateManager.setState(GameStateManager.TEST_LEVEL_STATE);
		}
	}

	public void handleInput()
	{
		// Press up to go up a menu item
		if(KeyInput.wasPressed(KeyInput.key_UP))
		{
			if(currentChoice > 0) // No looping from one side to the other
			{
				currentChoice--; // Lower numbers are higher up
			}
		}
		// Press down to go down a menu item
		else if(KeyInput.wasPressed(KeyInput.key_DOWN))
		{
			if(currentChoice < options.length - 1) // No looping from one side to the other
			{
				currentChoice++; // Higher numbers are lower
			}
		}
		// Press enter to select an option
		else if(KeyInput.wasPressed(KeyInput.key_ENTER))
		{
			select();
		}
	}

	public void update()
	{
		backgroundImage.update();
		handleInput();
	}

	public void draw(Graphics2D graphics)
	{
		backgroundImage.draw(graphics);

		graphics.setColor(titleColor);
		graphics.setFont(titleFont);
		graphics.drawString("Megaman", 80, 70);

		graphics.setFont(choiceFont);
		for(int i = 0; i < options.length; i++)
		{
			if(i == currentChoice)
			{
				graphics.setColor(Color.GRAY);
			}
			else
			{
				graphics.setColor(Color.WHITE);
			}
			graphics.drawString(options[i], 145, 140 + (i * 15));
		}
	}
}
