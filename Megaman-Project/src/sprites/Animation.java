package sprites;

import java.awt.image.BufferedImage;

public class Animation
{
	private BufferedImage frames[];
	private int currentFrame;

	// delay between one image and the next
	private long startTime;
	private long delay;

	private boolean hasPlayedOnce;

	public Animation()
	{
		hasPlayedOnce = false;
	}

	public void setFrames(BufferedImage images[])
	{
		frames = images;
		if(currentFrame >= frames.length)
		{
			currentFrame = 0;
		}
	}

	/*
	 * setDelay * set method for the delay
	 * 
	 * @param d the delay being set. If this is negative then there is only one sprite and no
	 * animation happens.
	 */
	public void setDelay(long d)
	{
		delay = d;
	}

	public void update()
	{
		if(delay < 0)
		{
			return;
		}

		long elapsed = (System.nanoTime() - startTime) / 1000000; // divided by 1 million

		if(elapsed > delay)
		{
			currentFrame++;
			startTime = System.nanoTime();
		}

		if(currentFrame == frames.length)
		{
			currentFrame = 0;
			hasPlayedOnce = true;
		}
	}

	public BufferedImage getImage()
	{
		return frames[currentFrame];
	}

	public boolean hasPlayedOnce()
	{
		return hasPlayedOnce;
	}
}
