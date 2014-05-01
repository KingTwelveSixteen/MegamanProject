package sprites;

import tileMap.TileMap;

public abstract class Enemy extends Character
{
	protected Player theMainCharacter;
	protected int touchDamage;

	public Enemy(TileMap inThisMap, Player mainCharacter)
	{
		super(inThisMap);
		theMainCharacter = mainCharacter;
	}

	public boolean shouldBeRemoved()
	{
		return dead;
	}

	public int getTouchDamage()
	{
		return touchDamage;
	}

	public boolean playerOnTheLeft()
	{
		if(theMainCharacter.getX() < x)
			return true;
		else
			return false;
	}

	public double howCloseToPlayerX()
	{
		return Math.abs(theMainCharacter.getX() - x);
	}

	public double howCloseToPlayerY()
	{
		return Math.abs(theMainCharacter.getY() - y);
	}
}
