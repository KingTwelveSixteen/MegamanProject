package gameState;

import java.awt.Graphics2D;

import handlers.SpecialIndexToWordsThing;

public abstract class GameState extends SpecialIndexToWordsThing
{
	protected GameStateManager stateManager;
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D graphics);
}
