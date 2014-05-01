package sprites;

import java.awt.Graphics2D;
import java.util.ArrayList;

import tileMap.TileMap;

public abstract class Character extends MapObject
{

	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected boolean flinching;
	protected int flinchTimer;

	// weapon stuff
	protected ArrayList<Projectile> projectilesFired;

	public Character(TileMap inThisMap)
	{
		super(inThisMap);

		projectilesFired = new ArrayList<Projectile>();
	}

	public void hit(int projectileDamage)
	{
		if(dead || flinching) // If already dead or currently flinching
			return; // Do nothing

		health -= projectileDamage;
		if(health <= 0)
		{
			dead = true;
		}
		flinching = true;
		flinchTimer = 30;
	}

	public int getHealth()
	{
		return health;
	}

	public int getMaxHealth()
	{
		return maxHealth;
	}

	// Same as normal update method, but also update and animate the projectiles
	public void update()
	{
		// do everything from MapObject's update
		super.update();

		// also update the projectiles
		updateProjectiles();
	}

	public void updateProjectiles()
	{
		// the arraylist is so that the projectilesFired array doesn't get smaller mid updating all
		// projectiles - that would lead to the last few projectiles not getting updated.
		ArrayList<Integer> removeTheseProjectiles = new ArrayList<Integer>();
		for(int i = 0; i < projectilesFired.size(); i++)
		{
			projectilesFired.get(i).update();
			if(projectilesFired.get(i).shouldBeRemoved())
			{
				removeTheseProjectiles.add(i);
			}
		}
		while(!removeTheseProjectiles.isEmpty())
		{
			projectilesFired.remove(removeTheseProjectiles.remove(0));
		}
	}

	// Same as normal draw method, but also draw the projectiles
	public void draw(Graphics2D graphics)
	{
		// do everything from MapObject's draw
		super.draw(graphics);

		// also draw all the projectiles
		drawProjectiles(graphics);
	}

	public void drawProjectiles(Graphics2D graphics)
	{
		for(int i = 0; i < projectilesFired.size(); i++)
		{
			projectilesFired.get(i).draw(graphics);
		}
	}

	// try
		// {
		// BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(
		// "/SpriteGraphics/normalMegaMan.png"));
		//
		// // Read all the sprites, one animation at a time
		// for(int eachAnimationType = 0; eachAnimationType < 9; eachAnimationType++)
		// {
		// BufferedImage[] anAnimation = new BufferedImage[numFrames[eachAnimationType]];
		//
		// for(int eachAnimationFrame = 0; eachAnimationFrame < numFrames[eachAnimationType];
		// eachAnimationFrame++)
		// {
		// anAnimation[eachAnimationFrame] = spriteSheet.getSubimage(eachAnimationFrame
		// * width, eachAnimationType * height, width, height);
		// }
		// spriteGraphics.add(anAnimation);
		// }
		// } catch (Exception e)
		// {
		// e.printStackTrace();
		// }

}
