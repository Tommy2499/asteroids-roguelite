package com.pluto.game;

/**
 * Class to represent 2-dimensional vectors in Euclidean space.
 */
public class Vector2D<E extends Number> {
    /* The first coordinate of the vector */
    public E x;

    /* The second coordinate of the vector */
    public E y;

    /**
     * Constructor for the Vector2D class.
     * 
     * @param x - the x coordinate of the vector
     * @param y - the y coordinate of the vector
     */
    public Vector2D(E x, E y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Converts the Vector2D object to a JSON string.
     * 
     * @return the JSON string representation of the Vector2D object
     */
    public String toJson() {
        return "{\"x\": " + this.x + ", \"y\": " + this.y + "}";
    }
}
