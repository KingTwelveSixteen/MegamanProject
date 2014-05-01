package handlers;

import java.awt.event.KeyEvent;

/**
 * this class contains a boolean array of current and previous key states for the keys that are used
 * for this game. a key k is down when keyState[k] is true.
 */
public class KeyInput extends SpecialIndexToWordsThing
{
	// This exists for efficiency's sake. Put it to the actual number of keys please. If you are to
	// lazy to count them (or to intelligent) just take the highest numbered keyID, add one, and put
	// it here. Because that is what this one is. Always.
	public static final int NUM_KEYS = 10;
	
	public static boolean keysPressedState[] = new boolean[NUM_KEYS];
	public static boolean keysAlreadyPressedState[] = new boolean[NUM_KEYS];

	public static void keySet(int i, boolean isKeyPressed)
	{
		// the 'int i' is the 'code' for a specific key, as shown above (UP, LEFT, DOWN, ect.)

		// THIS IS ALSO CALLED FOR SETTING KEYS TO 'NOT PRESSED'

		if(i == KeyEvent.VK_UP)
			keysPressedState[key_UP] = isKeyPressed;
		else if(i == KeyEvent.VK_LEFT)
			keysPressedState[key_LEFT] = isKeyPressed;
		else if(i == KeyEvent.VK_DOWN)
			keysPressedState[key_DOWN] = isKeyPressed;
		else if(i == KeyEvent.VK_RIGHT)
			keysPressedState[key_RIGHT] = isKeyPressed;
		else if(i == KeyEvent.VK_Z)
			keysPressedState[key_JUMP] = isKeyPressed;
		else if(i == KeyEvent.VK_X)
			keysPressedState[key_SHOOT] = isKeyPressed;
		else if(i == KeyEvent.VK_C)
			keysPressedState[key_CHANGEWEAPONR] = isKeyPressed; // Probably change weapon
		else if(i == KeyEvent.VK_SHIFT)
			keysPressedState[key_CHANGEWEAPONL] = isKeyPressed; // Probably change weapon
		else if(i == KeyEvent.VK_ENTER)
			keysPressedState[key_ENTER] = isKeyPressed;
		else if(i == KeyEvent.VK_ESCAPE)
			keysPressedState[key_ESCAPE] = isKeyPressed;
		else
		{
			System.out.println("The key you tried to press does not exist.");
		}
	}

	public static void update()
	{
		for(int i = 0; i < NUM_KEYS; i++)
		{
			keysAlreadyPressedState[i] = keysPressedState[i];
		}
	}

	/**
	 * Be careful about all the remaining methods. Don't call the wrong one.
	 * 
	 * @wasPressed - works ONCE per button press, then returns false.
	 * @isHeld - works ONLY AFTER BEING UPDATED ONCE.
	 * @isPressed - returns true until the player lets go of it (and keySet has been called to set
	 *            the key to false).
	 * @anyKeyPress - works REPEATEDLY for any VALID key-press.
	 */

	public static boolean wasPressed(int i)
	{
		// The key has been pressed, and update has not been called yet
		return (keysPressedState[i] && !keysAlreadyPressedState[i]);
	}

	public static boolean isHeld(int i)
	{
		// The key is being pressed, and has been through a call to update()
		return (keysPressedState[i] && keysAlreadyPressedState[i]);
	}

	public static boolean isPressed(int i)
	{
		// The key is being pressed
		return (keysPressedState[i]);
	}

	public static boolean anyKeyPress()
	{
		for(int i = 0; i < NUM_KEYS; i++)
		{
			// End the method if ANY key is found to have been pressed. Because otherwise you are
			// just wasting time/processing speed.
			if(keysPressedState[i])
				return true;
		}
		return false;
	}
}
