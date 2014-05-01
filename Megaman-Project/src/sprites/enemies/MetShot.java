package sprites.enemies;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import sprites.Projectile;
import tileMap.TileMap;

public class MetShot extends Projectile
{

	public MetShot(TileMap currentTileMap, boolean goingRight)
	{
		super(currentTileMap, goingRight);
		goesThroughWalls = false;
		damage = 2;

		width = 8;
		height = 8;
		hitboxWidth = 8;
		hitboxHeight = 8;
		findHalfHitboxValues();

		moveSpeed = 1.5;
		if(goingRight)
		{
			xMovement = moveSpeed; // right
		}
		else
		{
			xMovement = -moveSpeed; // left
		}
		yMovement = 0;

		loadSprites();
	}

	protected void animateSprite()
	{
		animation.setFrames(currentAnimation);
		animation.setDelay(-1);
	}

	protected void loadSprites()
	{
		spriteGraphics = new ArrayList<BufferedImage[]>();

		// only one animation makes this super-simple.
		try
		{
			BufferedImage fullSpriteList = ImageIO.read(getClass().getResourceAsStream(
					"/SpriteGraphics/" + "MetShot.png"));

			currentAnimation = new BufferedImage[1];

			currentAnimation[0] = fullSpriteList.getSubimage(0, 0, width, height);

			spriteGraphics.add(currentAnimation);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
