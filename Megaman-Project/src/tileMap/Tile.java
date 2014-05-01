package tileMap;

import java.awt.image.*;

/**
 * Class: Tile
 * 
 * @author James Timothy McCravy
 * 
 * 
 *         This class - has an image and a tile type int. Both are set when constructed and can be
 *         obtained through get methods.
 * 
 *         Purpose: - represents a single tile out of a given tileset, with a specific image and
 *         what type of tile it is (IE background, blocks player, ladder, ect.)
 */
public class Tile
{
	private BufferedImage tileImage;
	private int type;

	public Tile(BufferedImage image, int blockType)
	{
		tileImage = image;
		type = blockType;
	}

	// CURRENTLY UNUSED
	public void changeTile(BufferedImage image, boolean blocked)
	{
		// this.image = image;
		// this.blocked = blocked;
	}

	public BufferedImage getImage()
	{
		return tileImage;
	}

	public int getType()
	{
		return type;
	}
}
