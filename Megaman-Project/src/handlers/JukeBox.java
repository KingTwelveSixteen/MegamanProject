package handlers;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class JukeBox
{
	boolean notOverYet = true;
	String filename = "Melancholy.wav";

	public JukeBox()
	{
		while(notOverYet)
		{
			try
			{
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(new File(filename)));
				clip.start();
			} catch (Exception exc)
			{
				exc.printStackTrace(System.out);
				notOverYet = false;
			}
		}
	}
}