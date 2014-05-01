package sprites;

import tileMap.TileMap;

public abstract class Item extends MapObject
{
	protected int itemType;

	public abstract void pickUp();

	public Item(TileMap inThisMap)
	{
		super(inThisMap);

		falling = true; // To make sure if it starts in the air it will immediately fall and won't
						// just float forever.
	}

	public void getNextPosition()
	{
		if(falling)
		{
			yMovement += gravity;
		}
	}
}
