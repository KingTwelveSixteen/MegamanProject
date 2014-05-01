package sprites.enemies;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import sprites.Enemy;
import sprites.Player;
import tileMap.TileMap;

public class Met extends Enemy
{
	private boolean crouched = true;
	private int crouchedHitboxHeight = 11;
	private int standingHitboxHeight = 18;
	private int standingTimer = 0;
	private int standingDelayTimer = 0;

	private final int[] numFrames = {1, 3, 1};

	public Met(TileMap inThisMap, Player mainCharacter)
	{
		super(inThisMap, mainCharacter);

		moveSpeed = 0;
		maxSpeed = 0;

		width = 18;
		height = 22;
		hitboxWidth = 15;
		hitboxHeight = crouchedHitboxHeight;

		health = 1;
		maxHealth = 1;

		touchDamage = 2;

		loadSprites();

		animation.setFrames(spriteGraphics.get(0));
	}

	public void standUp()
	{
		hitboxHeight = standingHitboxHeight;
		crouched = false;
		standingTimer = 120;
	}

	public void duckDown()
	{
		hitboxHeight = crouchedHitboxHeight;
		crouched = true;
		standingDelayTimer = 120;
	}

	public void loadSprites()
	{
		spriteGraphics = new ArrayList<BufferedImage[]>();
		try
		{
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(
					"/SpriteGraphics/MetSheet.png"));

			// Read all the sprites, one animation at a time
			for(int eachAnimationType = 0; eachAnimationType < 3; eachAnimationType++)
			{
				BufferedImage[] anAnimation = new BufferedImage[numFrames[eachAnimationType]];

				for(int eachAnimationFrame = 0; eachAnimationFrame < numFrames[eachAnimationType]; eachAnimationFrame++)
				{
					anAnimation[eachAnimationFrame] = spriteSheet.getSubimage(eachAnimationFrame
							* width, eachAnimationType * height, width, height);
				}
				spriteGraphics.add(anAnimation);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void getNextPosition()
	{
		if(playerOnTheLeft())
		{
			facingRight = false;
		}
		else
		{
			facingRight = true;
		}

		if(crouched)
		{
			standingDelayTimer--;
			if(this.howCloseToPlayerX() < 75 && standingDelayTimer <= 0)
			{
				standUp();
				shoot();
			}
		}
		else
		{
			standingTimer--;
			if(standingTimer == 0)
			{
				duckDown();
			}
		}
	}

	public void shoot()
	{
		MetShot bullet = new MetShot(tileMap, facingRight);
		projectilesFired.add(bullet);
	}

	public void animateSprite()
	{
		if(crouched)
		{
			animation.setFrames(spriteGraphics.get(0));
			animation.setDelay(-1);
		}
		else
		{
			animation.setFrames(spriteGraphics.get(2));
			animation.setDelay(-1);
		}
	}
}
