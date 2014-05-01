package startUp;

// import java.awt.Color;
import gameState.GameStateManager;
import handlers.KeyInput;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener
{
	// dimensions
	public static final int WIDTH = 256; // Megaman game width
	public static final int HEIGHT = 240; // megaman game height
	public static final int SCALE = 2;

	// thread
	private Thread thread;
	private boolean running;
	private int fps = 60; // Frames per second
	private int targetTime = 1000 / fps; // How fast the game will preferably run

	// image
	private BufferedImage image;
	private Graphics2D graphics;

	// game state manager
	private GameStateManager stateManager;

	public GamePanel()
	{
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}

	// This starts the thread.
	public void addNotify()
	{
		super.addNotify();

		if(thread == null)
		{
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	private void init()
	{
		running = true;

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) image.getGraphics();

		stateManager = new GameStateManager();
	}

	public void run()
	{
		init();

		long startTime;
		long elapsedTime;
		long waitTime;

		while(running)
		{
			startTime = System.nanoTime();

			update();
			draw();
			drawToScreen();

			elapsedTime = (System.nanoTime() - startTime) / 1000000; // One million
			waitTime = targetTime - elapsedTime;

			try
			{
				thread.sleep(waitTime); // I don't know what the problem is. Doesn't negatively
										// impact the performance or anything as far as I can tell
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	// SEPERATOR LINE -----------------------------------------------------------------

	private void update()
	{
		stateManager.update();
		KeyInput.update();
	}

	private void draw()
	{
		stateManager.draw(graphics);
	}

	private void drawToScreen()
	{
		Graphics screenDrawing = getGraphics();
		screenDrawing.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		screenDrawing.dispose();
	}

	public void keyTyped(KeyEvent key)
	{} // NOTHING HAPPENS

	public void keyPressed(KeyEvent key)
	{
		KeyInput.keySet(key.getKeyCode(), true); // Set that key being pressed to true
	}

	public void keyReleased(KeyEvent key)
	{
		KeyInput.keySet(key.getKeyCode(), false); // Set that key being pressed to false
	}
}
