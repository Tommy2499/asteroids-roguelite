package com.pluto.game;

/**
 * A class that represents Asteroid game objects. It contains all the attributes
 * needed to render asteroids on the screen, and associated methods to
 * manipulate the objects during the game.
 */
public class Asteroid extends Enemy {
    /*
     * The starting health of an asteroid depending on type.
     */
    private static final int COMET_HEALTH = 10;
    private static final int LARGE_ASTROID_HEALTH = 10;
    private static final int MEDIUM_ASTROID_HEALTH = 5;
    private static final int SMALL_ASTROID_HEALTH = 1;

    /* These enums determine the sizes of the asteroid objects */
    static enum AsteroidSize {
        SMALL, MEDIUM, LARGE, COMET
    }

    /* Determines this asteroids size */
    public AsteroidSize size;

    /*
     * Determines the speed at which this Asteroid rotates in radians per
     * second. Positive is clockwise, negative is counterclockwise.
     */
    public float rotVelocity;

    /**
     * Constructor for the Asteroid class.
     * 
     * @param position    - the position of the asteroid
     * @param orientation - the orientation of the asteroid
     * @param velocity    - the velocity of the asteroid
     * @param size        - the size of the asteroid
     * @param rotVelocity - the rotation velocity of the asteroid
     */
    public Asteroid(Vector2D<Float> position, Vector2D<Float> velocity,
            float orientation, AsteroidSize size, float rotVelocity) {
        super(position, velocity, orientation, null, EnemyType.ASTEROID, 0);
        this.size = size;
        this.rotVelocity = rotVelocity;

        if (size == AsteroidSize.SMALL) {
            this.hitbox = new HitBox[] {
                    new HitBox(this.getPosition().x, this.getPosition().y, 25.0f)
            };
            setHealth(SMALL_ASTROID_HEALTH);
        } else if (size == AsteroidSize.MEDIUM) {
            this.hitbox = new HitBox[] {
                    new HitBox(this.getPosition().x, this.getPosition().y, 50.0f)
            };
            setHealth(MEDIUM_ASTROID_HEALTH);
        } else if (size == AsteroidSize.LARGE) {
            this.hitbox = new HitBox[] {
                    new HitBox(this.getPosition().x, this.getPosition().y, 100.0f)
            };
            setHealth(LARGE_ASTROID_HEALTH);
        } else {
            this.hitbox = new HitBox[] {
                    new HitBox(this.getPosition().x, this.getPosition().y, 60.0f)
            };
            setType(EnemyType.COMET); // Set this type to a comet now
            setHealth(COMET_HEALTH);
        }
    }

    /**
     * Moves the Asteroid object by one time step. It updates position and
     * orientation based on the object's velocity and rotation velocity.
     * 
     * @param dt - the amount of time since the last update
     */
    @Override
    public void moveObj(float dt) {
        Vector2D<Float> newPos = new Vector2D<Float>(getPosition().x + getVelocity().x * dt,
                getPosition().y + getVelocity().y * dt);
        this.setPosition(newPos);
        this.rotate(rotVelocity * dt);
    }

    /**
     * Converts the Asteroid object to a JSON string.
     * Needs to return the position, orientation, hitbox, and size of the asteroid.
     *
     * @return - A json formatted string representing this Asteroid object
     */
    @Override
    public String toJson() {
        String parentJson = super.toJson();
        StringBuilder json = new StringBuilder();
        json.append(parentJson.substring(0, parentJson.length() - 1));
        json.append(", \"size\": ");
        json.append("\"" + this.size.toString() + "\"");
        json.append("}");
        return json.toString();
    }
}
