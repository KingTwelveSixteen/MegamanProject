package handlers;

/**
 * Lots of things extend this. They only do this to gain easy access to the values in it.
 */
public class SpecialIndexToWordsThing
{
	// key inputs
	public static int key_UP = 0;
	public static int key_LEFT = 1;
	public static int key_DOWN = 2;
	public static int key_RIGHT = 3;
	public static int key_JUMP = 4;
	public static int key_SHOOT = 5;
	public static int key_CHANGEWEAPONR = 6;
	public static int key_CHANGEWEAPONL = 7;
	public static int key_ENTER = 8;
	public static int key_ESCAPE = 9;

	// animation locations for player
	public static final int p_IDLE = 0;
	public static final int p_WALKING = 1;
	public static final int p_JUMPING = 2;
	public static final int p_SHOOTING = 3;
	public static final int p_WALKINGSHOOTING = 4;
	public static final int p_JUMPINGSHOOTING = 5;
	public static final int p_SLIDING = 6;
	public static final int p_FLINCH = 7;
	public static final int p_TAKEONESTEP = 8;
	public static final int p_JUMPINGFLINCH = 9;
	public static final int p_TELEPORTING = 10;
	public static final int p_TELEPORTLANDING = 11;
	public static final int p_TELEPORTJUMPING = 12;

	// player weapon types
	public static final int w_MEGABUSTER = 0;
	public static final int w_BLINKWISH = 1;

	// item types
	public static final int i_HEALTH = 0;
	public static final int i_AMMO = 1;
	public static final int i_LIFE = 2;

	// tile types
	public static final int t_BACKGROUND = 0;
	public static final int t_BLOCKED = 1;

}