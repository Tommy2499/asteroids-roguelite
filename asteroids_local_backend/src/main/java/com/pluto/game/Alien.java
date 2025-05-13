package com.pluto.game;

/**
 * Class to represent Alien objects.
 */
public class Alien extends ShooterEnemy {
    /* How often an Alien may shoot a bullet */
    private static int SHOOT_TIME = 3;

    /* How often the Alien may explode */
    private static int EXPLODE_TIME = 15;

    /* How long the alien should charge up for an explosive attack */
    private static int TOTAL_CHARGE_TIME = 3;

    /* Time since this alien has last shot */
    private float timeLastShot;

    /* Time this alien has been charging up for */
    private float chargingTime;

    /*
     * Time since this alien has last performed an explosive attack
     */
    private float timeLastExplode;

    /* Determines whether this alien is charging up for an attack */
    private boolean isCharging = false;

    /* Health of an Alien object */
    private static int ALIEN_HEALTH = 15;

    /**
     * Constructor for the Alien.
     *
     * @param position - the position to spawn the Alien
     * @param velocity - the velocity of this Alien, which is unused
     */
    public Alien(Vector2D<Float> position, Vector2D<Float> velocity) {
        super(position, velocity, 0.0f, null, EnemyType.ALIEN, ALIEN_HEALTH);
        this.hitbox = new HitBox[] { new HitBox(position.x, position.y, 25) };
        isCharging = false;
    }

    /**
     * Moves the Alien along its path given the timestep. It updates internal
     * timers of this Alien which determine its mechanics.
     *
     * @param dt - the timestep at which to move this Alien by.
     */
    @Override
    public void moveObj(float dt) {
        if (isCharging) {
            chargingTime += dt;
            return;
        }
        super.moveObj(dt);
        timeLastShot += dt;
        timeLastExplode += dt;
    }

    /**
     * Explodes and shoots 8 bullets in the cardinal directions, if the Alien
     * is able to. It otherwise returns an empty array.
     *
     * @return - An array of bullets or an empty array, depending on the Alien's
     *         internal time
     */
    private Bullet[] explode() {
        // First check if we can explode
        if (chargingTime >= TOTAL_CHARGE_TIME) {
            // Reset timers
            chargingTime = 0;
            timeLastExplode = 0;
            timeLastShot = 0;
            isCharging = false;
            // Give the alien a new path after exploding
            resetPath();

            // Shoot in 8 directions
            Bullet[] bullets = new Bullet[8];
            Vector2D<Float> n, s, e, w, ne, nw, se, sw;
            n = new Vector2D<Float>(0.0f, 1.0f);
            s = new Vector2D<Float>(0.0f, -1.0f);
            e = new Vector2D<Float>(1.0f, 0.0f);
            w = new Vector2D<Float>(-1.0f, 0.0f);
            ne = new Vector2D<Float>(1.0f, 1.0f);
            nw = new Vector2D<Float>(-1.0f, 1.0f);
            se = new Vector2D<Float>(1.0f, -1.0f);
            sw = new Vector2D<Float>(-1.0f, -1.0f);

            bullets[0] = new Bullet(getPosition(), n, 1);
            bullets[1] = new Bullet(getPosition(), s, 1);
            bullets[2] = new Bullet(getPosition(), e, 1);
            bullets[3] = new Bullet(getPosition(), w, 1);
            bullets[4] = new Bullet(getPosition(), ne, 1);
            bullets[5] = new Bullet(getPosition(), nw, 1);
            bullets[6] = new Bullet(getPosition(), se, 1);
            bullets[7] = new Bullet(getPosition(), sw, 1);
            return bullets;
        }

        // Cannot explode yet. Return an empty array
        return new Bullet[0];
    }

    /**
     * Is called to shoot at the Player. It may either shoot a single bullet,
     * shoot several, or none at all, depending on the internal timers of this
     * Alien object.
     *
     * @param playerPosition - the position of the player to shoot at.
     * @return - an array of bullets to be shot, which may be empty.
     */
    @Override
    public Bullet[] shootPlayer(Vector2D<Float> playerPosition) {
        // First check if we are charging
        if (isCharging) {
            // Try to explode. Will return an empty array if we cannot
            return explode();
        }

        // If we are not charging, try to shoot
        if (timeLastShot >= SHOOT_TIME) {
            Bullet[] bullets = new Bullet[1];
            // x and y are coordinates of the direction vector from
            // the alien to the player
            float x = playerPosition.x - getPosition().x;
            float y = playerPosition.y - getPosition().y;
            Vector2D<Float> direction = new Vector2D<>(x, y);
            bullets[0] = new Bullet(getPosition(), direction, 1);

            // Reset time since last shot
            timeLastShot = 0;
            return bullets;
        }

        // Now check if we can charge up
        if (timeLastExplode >= EXPLODE_TIME)
            isCharging = true;

        // Cannot shoot yet, return no bullets
        return new Bullet[0];
    }
}
