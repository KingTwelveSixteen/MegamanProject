package sprites;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class HUD
{
	private Player mainCharacter;
	private BufferedImage image;
	
	public HUD(Player thePlayer)
	{
		mainCharacter = thePlayer;
		
		loadSprites();
	}
	
	private void loadSprites()
	{
				try
				{
					BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(
							"/SpriteGraphics/HUD.png"));

					// Read all the sprites
						BufferedImage[] aSprite = new BufferedImage[numFrames[eachAnimationType]];

						for(int eachAnimationFrame = 0; eachAnimationFrame < numFrames[eachAnimationType]; eachAnimationFrame++)
						{
							aSprite[eachAnimationFrame] = spriteSheet.getSubimage(eachAnimationFrame
									* width, eachAnimationType * height, width, height);
						}
						spriteGraphics.add(anAnimation);
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
	}
	
	public void draw()
	{
		
	}
}
