package com.pluto.game;

import java.lang.Math;

/**
 * A class that represents the player game objects. It contains all the
 * attributes
 * needed to render the player on the screen, and associated methods to
 * manipulate the objects during the game.
 */
public class Spaceship extends SpawnableEntity {
    /* These enums are the different player inputs */
    public static enum Input {
        UP, LEFT, RIGHT, SHOOT
    }

    /* How much speed increases per second when moving forward */
    private static final float ACCEL = 500; // Needs playtesting

    /* How much the velocity decreases each second */
    private static final float DRAG = 1f; // Needs playtesting

    /* How fast the player rotates in radians per second */
    private static final float ROT_SPEED = 5f; // Needs playtesting

    /* The bullet damage this spaceship deals */
    private static final int BULLET_DAMAGE = 1;

    /* Number of lives the player has */
    private int lives;

    /* Timer of invicibility when hit, 0 if not invincible */
    private float invincibleTimer = 0.0f;

    /**
     * Constructor for the Spaceship class. Player spawns at the center of the
     * screen
     * with 3 lives. Hitbox will be a circle with radius 25 for now.
     */
    public Spaceship() {
        super(new Vector2D<Float>(500.0f, 500.0f), // Starting position
                new Vector2D<Float>(0.0f, 0.0f), // Starting velocity
                0.0f, // Starting orientation
                new HitBox[] {
                        new HitBox(new Vector2D<Float>(500.f, 500.f), 15.0f),
                        new HitBox(new Vector2D<Float>(500.f - 10, 500.f + 10), 15.0f),
                        new HitBox(new Vector2D<Float>(500.f - 10, 500.f - 10), 15.0f),
                        new HitBox(new Vector2D<Float>(500.f - 23, 500.f - 23), 3.0f),
                        new HitBox(new Vector2D<Float>(500.f - 23, 500.f + 23), 3.0f),
                        new HitBox(new Vector2D<Float>(500.f + 20, 500.f), 3.0f),
                });
        this.lives = 3;
    }

    /**
     * Moves the player object by one frame. It updates position and
     * orientation based on the object's velocity and rotation velocity and player
     * inputs.
     * 
     * @param dt    - the amount of time in seconds since the last update
     * @param input - the player inputs
     */
    public void moveObj(float dt, Input[] input) {
        Vector2D<Float> newVel = new Vector2D<Float>(getVelocity().x, getVelocity().y);
        for (Input i : input) {
            switch (i) {
                case UP:
                    newVel = new Vector2D<Float>(
                            (newVel.x + ACCEL * dt * (float) Math.cos(getOrientation())),
                            (newVel.y + ACCEL * dt * (float) Math.sin(getOrientation())));
                    this.setVelocity(newVel);
                    break;
                case LEFT:
                    this.rotate(-ROT_SPEED * dt);
                    break;
                case RIGHT:
                    this.rotate(ROT_SPEED * dt);
                    break;
                case SHOOT: // Game manager handles shooting
                    break;
            }
        }
        newVel.x = newVel.x * (1 - DRAG * dt);
        newVel.y = newVel.y * (1 - DRAG * dt);
        this.setVelocity(newVel);
        Vector2D<Float> newPos = new Vector2D<Float>(getPosition().x + getVelocity().x * dt,
                getPosition().y + getVelocity().y * dt);
        this.setPosition(newPos);
        invincibleTimer -= dt;
        if (invincibleTimer < 0) {
            invincibleTimer = 0;
        }
    }

    /**
     * Getter for the number of lives the player has
     * 
     * @return the number of lives the player has
     */
    public int getLives() {
        return lives;
    }

    /**
     * Call when player gets hit by an asteroid. Decreases number of lives
     * by 1 and updates the player's position to the center of the screen.
     */
    public void hit() {
        if (invincibleTimer > 0) {
            return;
        }
        lives--;
        if (lives != 0) {
            invincibleTimer = 3.0f;
        }
    }

    /**
     * Shoots a bullet object with the specified damage.
     *
     * @return - A bullet object the player has shot.
     */
    public Bullet shootBullet() {
        Vector2D<Float> pos = new Vector2D<Float>(getPosition().x, getPosition().y);
        float orientation = getOrientation();
        Bullet bullet = new Bullet(pos, orientation, BULLET_DAMAGE);

        return bullet;
    }

    /**
     * Converts the Spaceship object to a JSON string.
     * Needs to include the position, orientation, hitboxes, number of lives, and if
     * invincible.
     * 
     * @return the JSON string representation of the Spaceship object
     */
    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\"position\": ");
        json.append(this.getPosition().toJson());
        json.append(", \"orientation\": ");
        json.append(this.getOrientation());
        json.append(", \"hitbox\": ");
        json.append("[");
        for (int i = 0; i < hitbox.length; i++) {
            json.append(hitbox[i].toJson());
            if (i < hitbox.length - 1) {
                json.append(",");
            }
        }
        json.append("], \"lives\": ");
        json.append(this.lives);
        json.append(", \"is_invincible\": ");
        json.append(this.invincibleTimer > 0);
        json.append("}");
        return json.toString();
    }
}
