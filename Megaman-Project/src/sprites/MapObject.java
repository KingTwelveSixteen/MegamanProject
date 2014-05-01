package sprites;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import sprites.Animation;
import startUp.GamePanel;
import tileMap.TileMap;
import handlers.SpecialIndexToWordsThing;

public abstract class MapObject extends SpecialIndexToWordsThing
{
	// All the protected things are only to be used via inheritance

	// tilemap stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xMapPosition;
	protected double yMapPosition;

	// position
	protected double x;
	protected double y;

	// Acceleration/velocity (move this much next frame)
	protected double xMovement;
	protected double yMovement;

	// dimensions
	protected int width;
	protected int height;

	// collision box
	protected int hitboxWidth;
	protected int hitboxHeight;

	// collision detection stuff
	protected int currentRow;
	protected int currentColumn;
	protected double xDestination;
	protected double yDestination;
	protected double tempX;
	protected double tempY;

	protected double halfHitboxWidth;
	protected double halfHitboxHeight;

	protected boolean topLeftBlocked; // left side
	protected boolean middleLeftBlocked;
	protected boolean bottomLeftBlocked;
	protected boolean topRightBlocked; // right side
	protected boolean middleRightBlocked;
	protected boolean bottomRightBlocked;
	protected boolean topMiddleBlocked; // middle two - top and bottom
	protected boolean bottomMiddleBlocked;

	protected boolean againstWall;
	protected boolean againstFloorOrCeiling;

	protected boolean drawCollisionBox = true;

	// Save these tilemap collision things due to falling multiple tiles in one update and making
	// sliding work better (sliding is in the player class)
	protected int rowBelowObject;
	protected int rowAboveObject;
	protected int columnLeftOfObject;
	protected int columnRightOfObject;

	// animation
	protected Animation animation = new Animation();
	// protected int currentAction; // MIGHT NOT BE NECESSARY
	// protected int previousAction; // MIGHT NOT BE NECESSARY
	protected boolean facingRight;
	protected ArrayList<BufferedImage[]> spriteGraphics;
	protected BufferedImage[] currentAnimation;

	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;

	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double gravity = 0.20;
	protected double maxFallSpeed;
	protected double jumpStartPower; // If you hold jump...
	protected double stopJumpSpeed; // ...you jump higher

	/**
	 * The constructor. All things face right by default.
	 * 
	 * @param inThisMap
	 */
	public MapObject(TileMap inThisMap)
	{
		tileMap = inThisMap;
		tileSize = tileMap.getTileSize();
		animation = new Animation();
		facingRight = true;
	}

	public void findHalfHitboxValues()
	{
		// That is, the difference between the center of the hitbox, and the edge. (half the
		// hitboxWidth/Height. Use negative to find the opposite edge.)
		halfHitboxWidth = (hitboxWidth / 2);
		halfHitboxHeight = (hitboxHeight / 2);
	}

	// for collision detection with other MapObjects
	public boolean intersects(MapObject otherObject)
	{
		Rectangle thisThingsRectangle = this.getRectangle();
		Rectangle otherThingsRectangle = otherObject.getRectangle();

		if(thisThingsRectangle.intersects(otherThingsRectangle))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Rectangle getRectangle()
	{
		// X and Y are in the middle of the hitbox, so place the top-left of the rectangle at half
		// the size of the hitbox to the left and up of them.
		return new Rectangle((int) (x - halfHitboxWidth), (int) (y - halfHitboxHeight),
				hitboxWidth, hitboxHeight);
	}

	public void calculateHitboxPointsAtThisPoint(double X, double Y)
	{
		// Do this one first. OR ELSE.
		calculateColumnsAndRowsAroundHitboxAtThisPoint(X, Y);

		// Calculate all 8 points used for tilemap collision detection
		calculateHitboxCornersAtThisPoint(); // the four corners
		calculateHitboxMiddlesAtThisPoint(); // middles of each of the four sides
	}

	public void calculateColumnsAndRowsAroundHitboxAtThisPoint(double X, double Y)
	{
		// find all the columns and rows directly adjacent to the object
		columnLeftOfObject = (int) (X - halfHitboxWidth) / tileSize;
		columnRightOfObject = (int) (X + halfHitboxWidth) / tileSize;
		rowAboveObject = (int) (Y - halfHitboxHeight) / tileSize;
		rowBelowObject = (int) (Y + halfHitboxHeight) / tileSize;
	}

	public void calculateHitboxCornersAtThisPoint()
	{
		// getTileType takes a row, then a column.
		int topLeftTileType = tileMap.getTileType(rowAboveObject, columnLeftOfObject);
		int topRightTileType = tileMap.getTileType(rowAboveObject, columnRightOfObject);
		int bottomLeftTileType = tileMap.getTileType(rowBelowObject, columnLeftOfObject);
		int bottomRightTileType = tileMap.getTileType(rowBelowObject, columnRightOfObject);

		// These each equal the boolean of 'is LOCATION tile blocked?'
		topLeftBlocked = (topLeftTileType == t_BLOCKED);
		topRightBlocked = (topRightTileType == t_BLOCKED);
		bottomLeftBlocked = (bottomLeftTileType == t_BLOCKED);
		bottomRightBlocked = (bottomRightTileType == t_BLOCKED);
	}

	public void calculateHitboxMiddlesAtThisPoint()
	{
		// getTileType takes a row, then a column.
		int rightTileType = tileMap.getTileType(currentRow, columnRightOfObject);
		int leftTileType = tileMap.getTileType(currentRow, columnLeftOfObject);
		int topTileType = tileMap.getTileType(rowAboveObject, currentColumn);
		int bottomTileType = tileMap.getTileType(rowBelowObject, currentColumn);

		// These each equal the boolean of 'is LOCATION tile blocked?'
		middleRightBlocked = (rightTileType == t_BLOCKED);
		middleLeftBlocked = (leftTileType == t_BLOCKED);
		topMiddleBlocked = (topTileType == t_BLOCKED);
		bottomMiddleBlocked = (bottomTileType == t_BLOCKED);
	}

	public void checkTilemapCollisions()
	{
		currentColumn = (int) x / tileSize;
		currentRow = (int) y / tileSize;

		xDestination = x + xMovement;
		yDestination = y + yMovement;

		tempX = x;
		tempY = y;

		// Y movement time

		calculateHitboxPointsAtThisPoint(x, yDestination);
		if(yMovement < 0) // going up
		{
			if(topLeftBlocked || topRightBlocked || topMiddleBlocked)
			{
				againstFloorOrCeiling = true;
				yMovement = 0;
				tempY = (currentRow * tileSize) + halfHitboxHeight;
			}
			else
			{
				againstFloorOrCeiling = false;
				tempY += yMovement;
			}
		}
		if(yMovement > 0) // going down
		{
			if(bottomLeftBlocked || bottomRightBlocked || bottomMiddleBlocked)
			{
				againstFloorOrCeiling = true;
				yMovement = 0;
				falling = false;
				// The negative one at the start is because otherwise the MapObject moves one pixel
				// into the floor when landing
				tempY = -1 + (rowBelowObject * tileSize) - halfHitboxHeight;
			}
			else
			{
				againstFloorOrCeiling = false;
				tempY += yMovement;
			}
		}

		// X movement time

		calculateHitboxPointsAtThisPoint(xDestination, y);
		if(xMovement < 0) // going left
		{
			if(topLeftBlocked || bottomLeftBlocked || middleLeftBlocked)
			{
				againstWall = true;
				xMovement = 0;
				tempX = (currentColumn * tileSize) + halfHitboxWidth;
			}
			else
			{
				againstWall = false;
				tempX += xMovement;
			}
		}
		if(xMovement > 0) // going right
		{
			if(topRightBlocked || bottomRightBlocked || middleRightBlocked)
			{
				againstWall = true;
				xMovement = 0;
				// column or row * tilesize gives the TOP and LEFT point of the tile. So add one to
				// column/row before multiplying to get one pixel past the right/bottom edge. Thats
				// what the negative one is there to counteract.
				tempX = -1 + ((currentColumn + 1) * tileSize) - halfHitboxWidth;
			}
			else
			{
				againstWall = false;
				tempX += xMovement;
			}
		}

		if(!falling)
		{
			calculateHitboxPointsAtThisPoint(x, yDestination + 1);
			if(!bottomLeftBlocked && !bottomRightBlocked && !bottomMiddleBlocked)
			{
				falling = true;
			}
		}
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getHitboxWidth()
	{
		return hitboxWidth;
	}

	public int getHitboxHeight()
	{
		return hitboxHeight;
	}

	/**
	 * 
	 * @return x as an int
	 */
	public double getX()
	{
		return (int) x;
	}

	/**
	 * 
	 * @return y as an int
	 */
	public double getY()
	{
		return (int) y;
	}

	public void setPosition(double X, double Y)
	{
		x = X;
		y = Y;
	}

	/**
	 * setVector is the same thing as 'setMovement', or 'moveThisMuch'.
	 */
	public void setVector(double xMovement, double yMovement)
	{
		this.xMovement = xMovement;
		this.yMovement = yMovement;
	}

	public void setMapPosition()
	{
		xMapPosition = tileMap.getX();
		yMapPosition = tileMap.getY();
	}

	public void setLeft(boolean isDoingThis)
	{
		left = isDoingThis;
	}

	public void setRight(boolean isDoingThis)
	{
		right = isDoingThis;
	}

	public void setUp(boolean isDoingThis)
	{
		up = isDoingThis;
	}

	public void setDown(boolean isDoingThis)
	{
		down = isDoingThis;
	}

	public void setJump(boolean isDoingThis)
	{
		jumping = isDoingThis;
	}

	public boolean onScreen()
	{
		// If the object is past the edge of the screen in any of the four possible directions
		if(x + xMapPosition + hitboxWidth < 0 || x + xMapPosition - hitboxWidth > GamePanel.WIDTH
				|| y + yMapPosition + hitboxHeight < 0 || y + yMapPosition - hitboxHeight > GamePanel.HEIGHT)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public void update()
	{
		// Every frame do the following
		getNextPosition();
		checkTilemapCollisions();
		setPosition(tempX, tempY);

		animateSprite();
	}

	public void draw(Graphics2D graphics)
	{
		setMapPosition();

		// If not on screen don't bother
		if(!onScreen())
		{
			return; // RETURN
		}

		/**
		 * drawImage takes the image to draw, the x position to draw it, the y position to draw it,
		 * and then may-or-may-not take transform info, followed by an ImageObserver. I don't know
		 * what an ImageObserver does, so that's null.
		 */
		if(facingRight)
		{
			graphics.drawImage(animation.getImage(), (int) (x + xMapPosition - (width / 2)),
					(int) (y + yMapPosition - (height / 2)), null);
		}
		else
		{
			graphics.drawImage(animation.getImage(),
					(int) (x + xMapPosition - (width / 2) + width),
					(int) (y + yMapPosition - (height / 2)), -width, height, null);
		}

		// draw collision box (CURRENTLY BROKEN)
		if(drawCollisionBox)
		{
			Rectangle r = getRectangle();
			r.x += xMapPosition;
			r.y += yMapPosition;
			graphics.draw(r);
		}
	}

	protected abstract void getNextPosition();

	protected abstract void animateSprite();

}
