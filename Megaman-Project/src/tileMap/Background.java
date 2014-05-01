package tileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import startUp.GamePanel;

public class Background
{
	private BufferedImage backgroundImage;

	private double xLocation;
	private double yLocation;
	private double moveXBy;
	private double moveYBy;

	private double moveScale; // how fast the background moves.

	public Background(String backgroundName, double moveScale)
	{
		try
		{
			// reading the background image
			backgroundImage = ImageIO.read(getClass().getResourceAsStream(
					"/Backgrounds/" + backgroundName));

			this.moveScale = moveScale;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setPosition(double X, double Y)
	{
		xLocation = (X * moveScale) % GamePanel.WIDTH;
		yLocation = (Y * moveScale) % GamePanel.HEIGHT;
	}

	public void setVector(double X, double Y)
	{
		moveXBy = (X * moveScale) % GamePanel.WIDTH;
		moveYBy = (Y * moveScale) % GamePanel.HEIGHT;
	}

	public void update()
	{
		// The checks are for if the picture has completely scrolled through once.
		if(xLocation > GamePanel.WIDTH || xLocation < -GamePanel.WIDTH)
		{
			xLocation = 0;
		}
		xLocation += moveXBy;
		if(yLocation > GamePanel.HEIGHT || yLocation < -GamePanel.HEIGHT)
		{
			yLocation = 0;
		}
		yLocation += moveYBy;
	}

	public void draw(Graphics2D graphics)
	{
		graphics.drawImage(backgroundImage, (int) xLocation, (int) yLocation, null);

		if(xLocation < 0)
		{
			// creating a copy of the background to the right so it repeats smoothly
			graphics.drawImage(backgroundImage, (int) xLocation + GamePanel.WIDTH, (int) yLocation,
					null);
		}
		else if(xLocation > 0)
		{
			// creating a copy of the background to the left so it repeats smoothly
			graphics.drawImage(backgroundImage, (int) xLocation - GamePanel.WIDTH, (int) yLocation,
					null);
		}
	}
}
