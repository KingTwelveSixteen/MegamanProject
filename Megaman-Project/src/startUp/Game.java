package startUp;

import javax.swing.JFrame;

public class Game
{
	public static void main(String[] args)
	{
		JFrame window = new JFrame("MegaMan");

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new GamePanel());
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null); // Not really sure if this one is needed
		window.setVisible(true);
	}
}
