package sprites;

import tileMap.TileMap;

abstract public class Projectile extends MapObject
{
	protected int damage;
	protected int projectileType;
	protected boolean hasHitSomething = false;
	protected boolean goesThroughWalls;

	public Projectile(TileMap currentTileMap, boolean goingRight)
	{
		super(currentTileMap);
	}

	protected void getNextPosition()
	{
		x += xMovement;
		y += yMovement;
	}

	public void hitSomething()
	{
		if(hasHitSomething) // already been called, stop messing around!
			return; // RETURN STATEMENT MID-METHOD WHOOP WHOOP WHOOP!

		hasHitSomething = true;
		xMovement = 0;
	}

	public boolean shouldBeRemoved()
	{
		if(this.onScreen()) // if on screen
		{
			return hasHitSomething; // it should be removed if it's hit something
		}
		return true; // if not on screen, it should be removed
	}

	// OVERRIIIIIDE!
	public void update()
	{
		// Every frame do the following
		getNextPosition();

		if(goesThroughWalls || hasHitSomething)
		{}
		else
		{
			checkTilemapCollisions();
			if(againstFloorOrCeiling || againstWall) // one of these is true if it hit something
			{
				hasHitSomething = true;
			}
		}

		setPosition(tempX, tempY);

		animateSprite();
	}

	protected abstract void animateSprite();
	protected abstract void loadSprites();
}
