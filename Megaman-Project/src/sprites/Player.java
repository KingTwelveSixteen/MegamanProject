package sprites;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tileMap.TileMap;
import handlers.SpecialIndexToWordsThing;

public class Player extends Character
{
	// cheats. ONLY ALTER THESE HERE.
	private boolean infiniteJump = false;

	// weapon stuff
	private int[] ammunition;
	private int[] maxAmmunition = {1, 28, 28, 28, 28};
	private int[] ammunitionCost = {0};
	private String currentWeaponSpriteLocation = "normal";
	private int currentWeapon;
	private boolean usingWeapon;
	private boolean chargingWeapon;

	// sliding
	private boolean sliding;
	private int slideDelay;
	private double slideSpeed;
	// sliding changes the hitbox
	private int baseHitboxWidth = 12;
	private int baseHitboxHeight = 24;
	private int slidingHitboxWidth = 22;
	private int slidingHitboxHeight = 18;

	// Megaman's first step is a single step, walking comes after
	private boolean takenOneStep = false;
	private int firstStepTimer;

	// animation
	//private ArrayList<BufferedImage[]> spriteGraphics;
	//private BufferedImage[] currentAnimation;

	// private final int[] numFrames = {6, 4, 1, 1, 4, 1, 1, 1, 1, 1, 1, 2};

	public Player(TileMap inThisMap)
	{
		super(inThisMap);

		width = 50;
		height = 50;

		hitboxWidth = baseHitboxWidth;
		hitboxHeight = baseHitboxHeight;

		moveSpeed = 1.5;
		slideSpeed = 3;
		maxSpeed = 1.5;
		maxFallSpeed = 12;
		jumpStartPower = -4.875;
		stopJumpSpeed = 0.3;

		facingRight = true;
		sliding = false;

		maxHealth = 28;
		health = maxHealth;
		
		ammunition = maxAmmunition;

		currentWeaponSpriteLocation = "normal";
		currentWeapon = w_MEGABUSTER;

		findHalfHitboxValues();

		loadSprites();

		animation.setFrames(spriteGraphics.get(SpecialIndexToWordsThing.p_IDLE));
	}

	public void useCurrentWeapon()
	{
		if(chargingWeapon)
		{}
		else
		{
			if(ammunition[currentWeapon] < ammunitionCost[currentWeapon])
			{
				// You are out of ammo for that weapon!
			}
			else
			{
				ammunition[currentWeapon] -= ammunitionCost[currentWeapon];
				chargingWeapon = true;

				createProjectile(currentWeapon);

				//MegaBusterShot aShot = new MegaBusterShot(tileMap, facingRight);

			}
		}
	}

	private void createProjectile(int projectileType)
	{
		MegaBusterShot aShot = new MegaBusterShot(tileMap, facingRight);

		projectilesFired.add(aShot);

		if(facingRight)
		{
			aShot.setPosition(x + 16, y);
		}
		else
		{
			aShot.setPosition(x - 16, y);
		}
	}

	public void beginSliding()
	{
		sliding = true;
		slideDelay = 20;
		hitboxWidth = slidingHitboxWidth;
		hitboxHeight = slidingHitboxHeight;
		findHalfHitboxValues();
		y += 3; // stupid 'not on ground' glitch
	}

	public void stopSliding()
	{
		sliding = false;
		hitboxWidth = baseHitboxWidth;
		hitboxHeight = baseHitboxHeight;
		findHalfHitboxValues();
		if(againstWall)
		{
			if(facingRight)
			{
				x = (columnRightOfObject * tileSize) + halfHitboxWidth;
			}
			else
			// column or row * tilesize gives the TOP and LEFT point of the tile. So add one to
			// column before multiplying to get one pixel past the right edge. Thats what the
			// negative one is there to counteract.
			{
				x = -1 + ((columnLeftOfObject + 1) * tileSize) - halfHitboxWidth;
			}
		}
		y -= 3; // stupid stuck in ground glitch
	}

	public void setWeaponUse(boolean toThis)
	{
		usingWeapon = toThis;
	}

	public void setChargingWeapon(boolean toThis)
	{
		chargingWeapon = toThis;
	}

	protected void getNextPosition()
	{
		if(jumping && !sliding && (!falling || infiniteJump))
		{
			if(down)
			{
				beginSliding(); // Down + Jump = Sliding!
			}
			else
			{
				yMovement = jumpStartPower; // Jumping!
			}
		}

		if(falling)
		{
			yMovement += gravity; // Falling!
			if(sliding)
			{
				stopSliding(); // You can't slide in mid-air silly
			}
		}

		// Sliding FORCES sliding. Nothing else allowed.
		if(sliding)
		{
			// Hitting a wall stops a slide
			if(againstWall)
			{
				stopSliding();
			}
			else
			{
				slideDelay--;
				if(facingRight)
				{
					xMovement = slideSpeed; // right
				}
				else
				{
					xMovement = -slideSpeed; // left
				}
				if(slideDelay <= 0)
				{
					stopSliding();
				}
			}
			return; // NOTHING ELSE ALLOWED!!! >:W
		}

		// Max speed actually doesn't matter for megaman. He just goes a single speed when walking
		if(left)
		{
			xMovement = -moveSpeed;
			facingRight = false;
		}
		else if(right)
		{
			xMovement = moveSpeed;
			facingRight = true;
		}
		else
		// Who cares about friction? We're MEGAMAN!!!!
		{
			xMovement = 0;
		}
	}

	/**
	 * NEW RULE: ALWAYS ORGANIZE EVERY SPRITE-SHEET BY ONE ANIMATION PER ROW I DON'T WANT TO HAVE TO
	 * DO THIS AGAIN IT IS HORRIBLE.
	 */
	public void loadSprites()
	{
		try
		{
			// Player's Sprites!

			// initialize this
			spriteGraphics = new ArrayList<BufferedImage[]>();

			// This one is the full sprite list page.
			BufferedImage playerSprites = ImageIO.read(getClass().getResourceAsStream(
					"/SpriteGraphics/" + currentWeaponSpriteLocation + "MegaMan.png"));

			// Used for easy locating specific sprites.
			// Starts at top-left and moves right and down.
			// ZERO COUNTS. First row and column are at 0.
			int numColumnsRight = 0;
			int numRowsDown = 0;

			// IDLE
			currentAnimation = new BufferedImage[6];
			numColumnsRight = 3;
			numRowsDown = 5;

			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			currentAnimation[1] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			currentAnimation[2] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			currentAnimation[3] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			currentAnimation[4] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight++;
			currentAnimation[5] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);

			spriteGraphics.add(currentAnimation);

			// WALKING
			currentAnimation = new BufferedImage[4];
			numColumnsRight = 1;
			numRowsDown = 0;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight++;
			currentAnimation[1] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight++;
			currentAnimation[2] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight--;
			currentAnimation[3] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

			// JUMPING
			currentAnimation = new BufferedImage[1];
			numColumnsRight = 4;
			numRowsDown = 0;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

			// SHOOTING
			currentAnimation = new BufferedImage[1];
			numColumnsRight = 0;
			numRowsDown = 1;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

			// WALKINGSHOOTING
			currentAnimation = new BufferedImage[4];
			numColumnsRight = 1;
			numRowsDown = 1;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight++;
			currentAnimation[1] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight++;
			currentAnimation[2] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight--;
			currentAnimation[3] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

			// JUMPINGSHOOTING
			currentAnimation = new BufferedImage[1];
			numColumnsRight = 4;
			numRowsDown = 1;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

			// SLIDING
			currentAnimation = new BufferedImage[1];
			numColumnsRight = 0;
			numRowsDown = 3;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

			// FLINCH
			currentAnimation = new BufferedImage[1];
			numColumnsRight = 1;
			numRowsDown = 2;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

			// ONESTEPFORWARD
			currentAnimation = new BufferedImage[1];
			numColumnsRight = 0;
			numRowsDown = 0;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

			// JUMPINGFLINCH
			currentAnimation = new BufferedImage[1];
			numColumnsRight = 0;
			numRowsDown = 2;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

			// TELEPORTING
			currentAnimation = new BufferedImage[1];
			numColumnsRight = 0;
			numRowsDown = 5;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

			// TELEPORTLANDING
			currentAnimation = new BufferedImage[2];
			numColumnsRight = 1;
			numRowsDown = 5;
			currentAnimation[0] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			numColumnsRight++;
			currentAnimation[1] = playerSprites.getSubimage((numColumnsRight * 50),
					(numRowsDown * 50), 50, 50);
			spriteGraphics.add(currentAnimation);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * the code for takenOneStep goes here, since it is an entirely graphical change.
	 */
	protected void animateSprite()
	{
		/**
		 * Cascade effect is important here. Pay attention to the order and the else/if.
		 */

		if(!left && !right) // if not pressing left or right
		{
			takenOneStep = false; // Megaman has not started moving left or right
		}

		if(flinching)
		{
			if(falling)
			{
				animation.setFrames(spriteGraphics.get(p_JUMPINGFLINCH));
				animation.setDelay(-1);
			}
			else
			{
				animation.setFrames(spriteGraphics.get(p_FLINCH));
				animation.setDelay(-1);
			}
		}
		else if(sliding)
		{
			animation.setFrames(spriteGraphics.get(p_SLIDING));
			animation.setDelay(-1);

			// You stop holding out your weapon if you slide
			usingWeapon = false;
		}
		else if(falling)
		{
			if(usingWeapon)
			{
				animation.setFrames(spriteGraphics.get(p_JUMPINGSHOOTING));
				animation.setDelay(-1);

				// Create the projectile or whatever
				useCurrentWeapon();
			}
			else
			{
				animation.setFrames(spriteGraphics.get(p_JUMPING));
				animation.setDelay(-1);
			}
		}
		else if(left || right)
		{
			if(usingWeapon)
			{
				animation.setFrames(spriteGraphics.get(p_WALKINGSHOOTING));
				animation.setDelay(200);

				// Create the projectile or whatever
				useCurrentWeapon();
			}
			else if(takenOneStep && firstStepTimer <= 0)
			{
				animation.setFrames(spriteGraphics.get(p_WALKING));
				animation.setDelay(200);
			}
			else
			{
				animation.setFrames(spriteGraphics.get(p_TAKEONESTEP));
				animation.setDelay(-1);

				// First step/Taken one step code here
				if(takenOneStep)
				{
					firstStepTimer--;
				}
				else
				{
					takenOneStep = true;
					firstStepTimer = 4;
				}
			}
		}
		else if(usingWeapon)
		{
			animation.setFrames(spriteGraphics.get(p_SHOOTING));
			animation.setDelay(-1);

			// Create the projectile or whatever
			useCurrentWeapon();
		}
		else
		{
			if(firstStepTimer >= 0) // First step lingers if you stand still
			{
				firstStepTimer--;
				animation.setFrames(spriteGraphics.get(p_TAKEONESTEP));
				animation.setDelay(-1);
			}
			else
			{
				animation.setFrames(spriteGraphics.get(p_IDLE));
				animation.setDelay(350);
			}
		}

		// update animation with the (possibly) new graphics
		animation.update();

		if(xMovement > 0)
		{
			facingRight = true;
		}
		if(xMovement < 0)
		{
			facingRight = false;
		}
	}
}