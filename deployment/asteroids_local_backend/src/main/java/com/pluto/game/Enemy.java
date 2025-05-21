package com.pluto.game;

/**
 * Abstract Enemy class that represents enemy objects in the game that the
 * player has to defeat.
 */
public abstract class Enemy extends SpawnableEntity {
    /* Type of this enemy. */
    private EnemyType enemyType;

    /* Health of this Enemy object. */
    private int health;

    /**
     * Constructor for the Enemy class. Ensures that all data members are
     * initialized for derived classes.
     */
    public Enemy(Vector2D<Float> position, Vector2D<Float> velocity,
            float orientation, HitBox[] hitbox, EnemyType type, int health) {
        super(position, velocity, orientation, hitbox);
        this.enemyType = type;
        this.health = health;
    }

    /**
     * A method that reduces this Enemy object's health by the specified amount
     * of damage. If damage is negative, then this method does not do anything.
     * The health of this object will not fall below 0.
     *
     * @param damage - the amount of damage to be taken
     */
    protected void takeDamage(int damage) {
        if (damage < 0) {
            return;
        }
        this.health = Math.max(0, this.health - damage);
    }

    /**
     * Returns the health of this Enemy object.
     *
     * @return - the health of this Enemy object.
     */
    protected int getHealth() {
        return this.health;
    }

    /**
     * Sets the health of this Enemy object. If health is negative, the enemy's
     * health is set to 0.
     *
     * @param health - the health of this Enemy object.
     */
    protected void setHealth(int health) {
        health = Math.max(health, 0);
        this.health = health;
    }

    /**
     * Moves the enemy object by timestep dt.
     */
    public abstract void moveObj(float dt);

    /**
     * Returns the type of this enemy object.
     */
    public EnemyType type() {
        return this.enemyType;
    }

    /**
     * Sets the type of this enemy object defined by the EnemyType enum.
     *
     * @param type - the type of this enemy object.
     */
    public void setType(EnemyType type) {
        this.enemyType = type;
    }

    /**
     * Returns a json formatted string representing this enemy object.
     *
     * @return - a json formatted string.
     */
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\"type\": \"");
        json.append(this.type().toString());
        json.append("\", \"position\": ");
        json.append(this.getPosition().toJson());
        json.append(", \"orientation\": ");
        json.append(this.getOrientation());
        json.append(", \"hitbox\": [");
        for (int i = 0; i < this.hitbox.length; i++) {
            json.append(this.hitbox[i].toJson());
        }
        json.append("]");
        json.append("}");
        return json.toString();
    }
}
