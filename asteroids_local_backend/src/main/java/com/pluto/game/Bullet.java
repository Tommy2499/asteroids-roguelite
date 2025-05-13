package com.pluto.game;

import java.lang.Math;

/**
 * A class that represents Asteroid game objects. It contains all the attributes
 * needed to render bullets on the screen, and associated methods to
 * manipulate the objects during the game. Player bullets also inherit from
 * enemy but cannot hit the player.
 */
public class Bullet extends Enemy {
    /* The speed of the bullet per second */
    private static final float SPEED = 500.0f; // Needs playtesting

    /* Health of all bullet objects */
    private static int BULLET_HEALTH = 1;

    /* The time the bullet has been alive */
    private float timeAlive;

    /* The damage this bullet object deals */
    private int damage;

    /**
     * Constructor for the Bullet class.
     * 
     * @param position    - the position of the bullet
     * @param orientation - the orientation of the bullet
     */
    public Bullet(Vector2D<Float> position, float orientation, int damage) {
        super(position, null, orientation, null, EnemyType.BULLET, BULLET_HEALTH);
        this.setVelocity(
                new Vector2D<Float>((float) Math.cos(orientation) * SPEED,
                        (float) Math.sin(orientation) * SPEED));
        this.hitbox = new HitBox[] {
                new HitBox(new Vector2D<Float>(this.getPosition().x,
                        this.getPosition().y), 5.0f) };
        this.timeAlive = 0.0f;
        this.damage = damage;
    }

    /**
     * Constructs a bullet object given the starting position and direction.
     * Note that the speed is still determed by the SPEED constant.
     *
     * @param position  - the starting position of this bullet.
     * @param direction - the direction vector this bullet follows
     * @param damage    - the damage of this bullet.
     */
    public Bullet(Vector2D<Float> position, Vector2D<Float> direction, int damage) {
        // Converts the direction vector into an orientation
        this(position, (float) Math.atan2(direction.y, direction.x), damage);

    }

    /**
     * Gets the damage of this bullet object as an integer.
     *
     * @return - the damage of this bullet.
     */
    public int dealsDamage() {
        return this.damage;
    }

    /**
     * Moves the Bullet object by one time step. It updates position and
     * orientation based on the object's velocity.
     * 
     * @param dt - the amount of time since the last update
     */
    public void moveObj(float dt) {
        Vector2D<Float> newPos = new Vector2D<Float>(
                getPosition().x + getVelocity().x * dt,
                getPosition().y + getVelocity().y * dt);
        this.setPosition(newPos);
        this.timeAlive += dt;
    }

    /**
     * Returns the time the bullet has been alive.
     * 
     * @return the time the bullet has been alive
     */
    public float getTimeAlive() {
        return timeAlive;
    }

    /**
     * Converts the Spaceship object to a JSON string.
     * Needs to include the position, orientation, and hitbox.
     * 
     * @return the JSON string representation of the Spaceship object
     */
    @Override
    public String toJson() {
        return super.toJson();
    }
}
