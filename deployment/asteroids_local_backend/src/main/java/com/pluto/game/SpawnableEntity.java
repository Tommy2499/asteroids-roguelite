package com.pluto.game;

import java.lang.Math;

/**
 * An abstract class that represents entities that the game can spawn. It
 * contains all attributes that a game object needs, and the associated methods
 * the game needs to manipulate them and render them.
 */
public abstract class SpawnableEntity {

    /*
     * The width and height of the screen. This is used to wrap the object's
     * position around the screen if it goes out of bounds.
     */
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;

    /*
     * An object's location on the screen. It must be within a predetermined
     * range.
     */
    private Vector2D<Float> position;

    /*
     * This object's hitbox for determining collisions. It contains a list of
     * circles that collectively form the object's hitbox. Each circle is
     * represented by the hibox class, which contains the circle's position and
     * radius.
     */
    protected HitBox[] hitbox;

    /*
     * The orientation of this spawnable entity for drawing on screen. It is
     * an angle measured in radians. We use the natural convention to
     * represent angles.
     */
    private float orientation;

    /*
     * The velocity of this spawnable entity. It is represented by a vector.
     * Note that the velocity is independent from the object's orientation.
     */
    private Vector2D<Float> velocity;

    /**
     * Constructor for the SpawnableEntity. Ensures that all derived classes
     * initialize SpawnableEntity data members.
     */
    public SpawnableEntity(Vector2D<Float> position, Vector2D<Float> velocity,
            float orientation, HitBox[] hitbox) {
        this.position = position;
        this.velocity = velocity;
        this.orientation = orientation;
        this.hitbox = hitbox;
    }

    /**
     * This method rotates the spawnable entity by an angle in radians. It
     * updates this object's orientation and hitbox accordingly.
     *
     * @param radians - the amount to rotate this object by in radians
     */
    public void rotate(float radians) {
        // First update the orientation
        setOrientation(radians + getOrientation());

        // holds the local coordinates of a hitbox circle
        float localX;
        float localY;
        // holds the rotated coordinates of a hitebox circle
        float rotX;
        float rotY;
        for (HitBox circle : hitbox) {
            // Convert the circle location to local object coordinates
            localX = circle.position.x - position.x;
            localY = circle.position.y - position.y;

            // Rotate local coordinates by radians
            rotX = (float) (localX * Math.cos(radians) - localY * Math.sin(radians));
            rotY = (float) (localX * Math.sin(radians) + localY * Math.cos(radians));

            // Convert back to global coordinates
            circle.position.x = rotX + position.x;
            circle.position.y = rotY + position.y;
        }
    }

    /**
     * This method checks if this spawnable entity has collided with another
     * spawnable entity.
     * 
     * @return - true if this object has collided with another object, false
     *         otherwise
     */
    public boolean collidesWith(SpawnableEntity other) {
        for (HitBox circle : hitbox) {
            for (HitBox otherCircle : other.hitbox) {
                if (Math.pow(circle.position.x - otherCircle.position.x, 2)
                        + Math.pow(circle.position.y - otherCircle.position.y, 2) < Math
                                .pow(circle.radius + otherCircle.radius, 2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method converts the spawnable entity to a json formatted string.
     *
     * @return - a json formatted string describing this object
     */
    public abstract String toJson();

    /**
     * This method gets the object's position.
     *
     * @return - the position of this SpawnableEntity
     */
    public Vector2D<Float> getPosition() {
        return position;
    }

    /**
     * This method sets the SpawnableEntity's position. Wraps the position
     * around the screen if it goes out of bounds.
     *
     * @param position - the position to set this object to
     */
    public void setPosition(Vector2D<Float> position) {
        if (this.position == null) {
            this.position = position;
            return;
        }
        
        // We cannot simply wrap hitboxes around the screen, or they will be
        // disconnected from the center of the object during rotation. 
        // We must make them relative to the new position, possibly allowing 
        // them to be negative.
        float oldX = this.position.x; 
        float oldY = this.position.y; 
        this.position = position;
        this.position.x = (this.position.x + SCREEN_WIDTH) % SCREEN_WIDTH;
        this.position.y = (this.position.y + SCREEN_HEIGHT) % SCREEN_HEIGHT;
        
        float dx = this.position.x - oldX;
        float dy = this.position.y - oldY;
        for (HitBox circle : hitbox) {
            circle.position.x += dx;
            circle.position.y += dy;
        }
    }

    /**
     * This method gets the spawnable entity's velocity.
     *
     * @return - the velocity of this SpawnableEntity
     */
    protected Vector2D<Float> getVelocity() {
        return velocity;
    }

    /**
     * This method sets the SpawnableEntity's velocity.
     *
     * @param velocity - the velocity of this spawnable entity
     */
    protected void setVelocity(Vector2D<Float> velocity) {
        this.velocity = velocity;
    }

    /**
     * This method gets the SpawnableEntity's orientation.
     *
     * @return - the orientation of this SpawnableEntity
     */
    protected float getOrientation() {
        return orientation;
    }

    /**
     * This method sets the SpawnableEntity's orientation. It enforces the
     * orientation angle to be within the range [0, 2pi].
     *
     * @param orientation - the angle in radians to set this objects orientation
     */
    protected void setOrientation(float orientation) {
        orientation = (orientation + (float) (2 * Math.PI)) % (float) (2 * Math.PI);
        this.orientation = orientation;
    }
}
