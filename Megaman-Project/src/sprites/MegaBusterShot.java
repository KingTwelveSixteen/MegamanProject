package sprites;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tileMap.TileMap;

/**
 * Otherwise known as a 'pellet' or 'lemon' (if you're weird).
 */
public class MegaBusterShot extends Projectile
{
	public MegaBusterShot(TileMap currentTileMap, boolean goingRight)
	{
		super(currentTileMap, goingRight);
		
		width = 8;
		height = 6;
		hitboxWidth = 8;
		hitboxHeight = 6;
		findHalfHitboxValues();
		
		moveSpeed = 1.5;
		if(goingRight)
		{
			xMovement = moveSpeed; // going right
		}
		else
		{
			xMovement = -moveSpeed; // going left
		}
		yMovement = 0;
		
		projectileType = w_MEGABUSTER;
		damage = 2;
		
		goesThroughWalls = false;
		
		loadSprites();
	}

	protected void loadSprites()
	{
		spriteGraphics = new ArrayList<BufferedImage[]>();

		// only one animation makes this super-simple.
		try
		{
			BufferedImage fullSpriteList = ImageIO.read(getClass().getResourceAsStream(
					"/SpriteGraphics/" + "pelletSprite.png"));

			currentAnimation = new BufferedImage[1];

			currentAnimation[0] = fullSpriteList.getSubimage(1, 1, width, height);

			spriteGraphics.add(currentAnimation);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void animateSprite()
	{
		// only one animation makes this super-simple.
		animation.setFrames(currentAnimation);
		animation.setDelay(-1);
		
		animation.update();
	}
}
