package tileMap;

import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import startUp.GamePanel;
import handlers.SpecialIndexToWordsThing;

public class TileMap extends SpecialIndexToWordsThing
{
	// Position
	private double x;
	private double y;

	// Used for making the screen scroll gradually and smoothly, instead of just plain following the
	// player. Putting it at 1 makes it just follow the player perfectly.
	private double tween = 1;

	// Bounds
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	// Map stuff
	private int[][] tileIDMap;
	private int numberOfColumns;
	private int numberOfRows;

	// Tileset stuff
	private BufferedImage fullTilesetImage;
	private int tileSize;
	private ArrayList<Tile> tilesByID;

	// Drawing
	private int rowOffset;
	private int columnOffset;
	private int numRowsToDraw;
	private int numColumnsToDraw;

	// Reader
	private BufferedReader reader;
	// \\s+ means any white space. \\n is a new line.
	private String delimeter = "\\s+|\\n";

	public TileMap(int tileSize)
	{
		this.tileSize = tileSize;

		// these two exist for efficiency. Only drawing the tiles that the player can see (+2
		// rows/columns for safety) saves a ton of processor speed for large levels.
		numRowsToDraw = (GamePanel.HEIGHT / tileSize) + 2;
		numColumnsToDraw = (GamePanel.WIDTH / tileSize) + 2;
	}

	public void loadTiles(String tileSetName, int pixelOffset)
	{
		int actualPixelOffset = pixelOffset * 2;
		try
		{
			fullTilesetImage = ImageIO.read(getClass().getResourceAsStream(
					"/Tilesets/" + tileSetName));

			// read how big the tileSet is
			int numTilesAcross = fullTilesetImage.getWidth() / (tileSize + actualPixelOffset);
			int numTilesDown = fullTilesetImage.getHeight() / (tileSize + actualPixelOffset);

			// Woo, initialize some stuff for the following loops
			tilesByID = new ArrayList<Tile>();
			BufferedImage currentTileImage;

			for(int row = 0; row < numTilesDown; row++)
			{
				for(int column = 0; column < numTilesAcross; column++)
				{
					// Grabbing a single tile's image from the main image. Pixel offset is used to
					// ignore the extra pixels.
					currentTileImage = fullTilesetImage.getSubimage((column * tileSize)
							+ (actualPixelOffset * column), (row * tileSize)
							+ (actualPixelOffset * row), tileSize, tileSize);

					// The prototype tileset stuff is set up so that all the blocked things are on a
					// row other than the first. The top row contains all background elements
					// (currently).
					if(row == 0)
					{
						Tile currentTile = new Tile(currentTileImage, t_BACKGROUND);
						tilesByID.add(currentTile);
					}
					else
					{
						Tile currentTile = new Tile(currentTileImage, t_BLOCKED);
						tilesByID.add(currentTile);
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void loadMap(String tileMapName)
	{
		try
		{
			InputStream inputStream = getClass().getResourceAsStream(
					"/Maps/" + tileMapName + ".txt");
			reader = new BufferedReader(new InputStreamReader(inputStream));

			// The first two lines of the file MUST be the WIDTH, then HEIGHT of the map. The actual
			// map comes afterwards.
			numberOfColumns = Integer.parseInt(reader.readLine());
			numberOfRows = Integer.parseInt(reader.readLine());

			// minX = GamePanel.WIDTH - (mapWidth * tileSize);
			// minY = GamePanel.HEIGHT - (mapHeight * tileSize);

			tileIDMap = new int[numberOfRows][numberOfColumns]; // Y, then X

			// Set the mins and maxes.
			/**
			 * THIS IS CONFUSING. What you need to remember is that positive x is right, negative x
			 * is left. Same with y. The tilemap starts with its top-left corner at 0,0 with the
			 * position value of 0,0. To see/reach the right side of the tilemap it must move to the
			 * left - which means going into negative x values. Therfor, the max x value of the
			 * tilemap is 0, wherin the left-side of the tilemap and screen are at the same place.
			 * Same thing with y values.
			 */
			minX = GamePanel.WIDTH - (numberOfColumns * tileSize);
			maxX = 0;
			minY = GamePanel.HEIGHT - (numberOfRows * tileSize);
			maxY = 0;

			/**
			 * This next bit is more complicated then it needs to be because SCREW trying to format
			 * the stupid txt files that the map-making program I use creates. This works as long as
			 * there is a new line after each row ends, but allows any number of lines per row if
			 * needed. And they ARE needed.
			 */
			for(int currentRow = 0; currentRow < numberOfRows; currentRow++)
			{
				int currentColumn = 0;
				while(currentColumn < numberOfColumns)
				{
					// Read one line from file...
					String currentLine = reader.readLine();

					// Split it by the delimeter to get the individual tile IDs as string...
					String[] aLineOfTileIDs = currentLine.split(delimeter);

					int currentIDFromCurrentLine = 0;
					while(aLineOfTileIDs.length > currentIDFromCurrentLine)
					{
						// Put those numbers in the tileIDMap as ints
						tileIDMap[currentRow][currentColumn] = Integer
								.parseInt(aLineOfTileIDs[currentIDFromCurrentLine]);

						// Increment what column the current tile ID is on.
						currentColumn++;

						// Increment what tile ID from the current line is being used.
						currentIDFromCurrentLine++;
					}
					// When done with all things from current line, check to see if that line
					// actually contained everything from the row. If not, repeat everything after
					// currentColumn = 0.
				}
			}
		} catch (FileNotFoundException e)
		{
			System.out.println("Tilemap not found exception in map loader.");
		} catch (NumberFormatException e)
		{
			System.out.println("Number format exception in map loader");
		} catch (IOException e)
		{
			System.out.println("IO Exceeption in map loader");
		}
	}

	public int getTileSize()
	{
		return tileSize;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public int getTileIDAt(int row, int column)
	{
		return tileIDMap[row][column];
	}

	public int getMinX()
	{
		return minX;
	}

	public int getMaxX()
	{
		return maxX;
	}

	/*
	 * public boolean isBlocked(int yLocation, int xLocation) { int checkingThisTileID =
	 * tileIDMap[yLocation][xLocation]; return tilesByID.get(checkingThisTileID).isBlocked(); }
	 */

	public int getTileType(int row, int column)
	{
		// Grab the tile ID at row and column, then enter it into the tilesByID list to see what
		// tile it is, then use that tile's getType method to get the type.
		return tilesByID.get(tileIDMap[row][column]).getType();
	}

	public void setPosition(double newX, double newY)
	{
		/**
		 * x is now x + the difference between current x and newX multiplied by the tween. Tween is
		 * usually a percentage, using decimal to represent that. This is used for smooth map
		 * scrolling instead of following the player absolutely. If tween = 1 then the map instantly
		 * moves to the new position, because that is 100% movement per frame.
		 * 
		 */
		x += (newX - x) * tween;
		y += (newY - y) * tween;
		fixBounds();
		columnOffset = (int) -x / tileSize;
		rowOffset = (int) -y / tileSize;
	}

	public void fixBounds()
	{
		if(x > maxX)
			x = maxX;
		if(x < minX)
			x = minX;

		if(y > maxY)
			y = maxY;
		if(y < minY)
			y = minY;
	}

	// //////////////////////////////////////////////////////////

	public void update()
	{}

	public void draw(Graphics2D graphics)
	{
		/*
		 * // Drawing all the tiles to their locations. for(int row = 0; row < mapHeight; row++) {
		 * for(int column = 0; column < mapWidth; column++) { int currentTileID =
		 * tileIDMap[row][column];
		 * 
		 * graphics.drawImage(tilesByID.get(currentTileID).getImage(), x + column * tileSize, y +
		 * row * tileSize, null); } }
		 */

		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++)
		{
			// If trying to draw out of bounds, stop.
			if(row >= numberOfRows)
			{
				break;
			}

			for(int column = columnOffset; column < columnOffset + numColumnsToDraw; column++)
			{
				// If trying to draw out of bounds, stop.
				if(column >= numberOfColumns)
				{
					break;
				}

				int currentTileID = tileIDMap[row][column];

				// For efficiencies sake, don't bother trying to draw the tile that means 'there is
				// no tile here'. (that one is always the very top left one, which is ID'ed to 0)
				if(currentTileID == 0)
				{
					continue;
				}

				graphics.drawImage(tilesByID.get(currentTileID).getImage(), (int) x + column
						* tileSize, (int) y + row * tileSize, null);
			}
		}
	}
}