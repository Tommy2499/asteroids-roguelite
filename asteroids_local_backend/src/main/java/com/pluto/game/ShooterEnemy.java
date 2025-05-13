package com.pluto.game;

/**
 * Abstract class to represent enemies that can shoot objects at the player.
 */
public abstract class ShooterEnemy extends Enemy {
    /*
     * The amount of time this ShooterEnemy has been alive for. This determines
     * their location on the path and their ability to shoot.
     */
    private float pathTime;

    /* The path of this ShooterEnemy */
    private Path path;

    /**
     * Shoots a bullet or multiple bullets at the player.
     */
    public abstract Bullet[] shootPlayer(Vector2D<Float> playerLocation);

    /**
     * Constructor for this ShooterEnemy. Initializes the path data member.
     */
    public ShooterEnemy(Vector2D<Float> position, Vector2D<Float> velocity,
            float orientation, HitBox[] hitbox, EnemyType type, int health) {
        super(position, velocity, orientation, hitbox, type, health);
        this.path = createPath();
        this.pathTime = 0;
    }

    /**
     * Moves this shooter enemy along its path data member given the timestep
     * dt.
     *
     * @param dt - the time step to move this shooter enemy.
     */
    @Override
    public void moveObj(float dt) {
        pathTime += dt;
        this.setPosition(path.getLocation(pathTime));
    }

    /**
     * Creates a new path for this ShooterEnemy.
     */
    public void resetPath() {
        pathTime = 0;
        path = createPath();
    }

    /**
     * Creates and updates the path object of this ShooterEnemy.
     */
    public Path createPath() {
        // Generate random numbers in [-350, -25] or [25, 350]
        int a = (int) Math.random() * 650;
        a = (a > 325) ? a - 350 : a - 300;
        int b = (int) Math.random() * 1200;
        b = (b > 325) ? b - 350 : b - 300;
        return new Path(this.getPosition(), a, b);
    }

    /**
     * A class to represent the path object of this ShooterEnemy. The paths are
     * eliptical curves of given radii.
     * 
     * @param a - the horizontal radius of this elipse
     * @param b - the vertical radius of this elipse
     */
    private static class Path {
        /* The radii of the elipse path */
        private int a;
        private int b;

        /* Determines the speed in which the path is traversed */
        private static float OMEGA = 2.50f;

        /* The initial position of the curve */
        private Vector2D<Float> position;

        /**
         * Constructor of a Path object.
         *
         * @param position - the initial position of the path, that is the
         *                 position at time 0.
         * @param a        - the horizontal radius of the eliptical path.
         * @param b        - the vertical radius of the eliptical path.
         */
        public Path(Vector2D<Float> position, int a, int b) {
            this.a = a;
            this.b = b;
            this.position = position;
        }

        /**
         * Returns the x coordinate of the position along the curve at the given
         * time.
         *
         * @param time - time at which to find the x coordinate
         * @return - the x coordinate at the given time
         */
        private float pathFunctionX(float time) {
            return (float) (a * Math.cos(time / OMEGA)) + (position.x - a);
        }

        /**
         * Returns the y coordinate of the position along the curve at the given
         * time.
         *
         * @param time - time at which to find the y coordinate.
         * @return - the y coordinate at the given time.
         */
        private float pathFunctionY(float time) {
            return (float) (b * Math.sin(time / OMEGA)) + position.y;
        }

        /**
         * Returns the position as a Vector2D along the curve at the specified
         * time.
         *
         * @param time - time at which to find the position.
         * @reutnr - the position along the curve at the give time.
         */
        public Vector2D<Float> getLocation(float time) {
            float x = pathFunctionX(time);
            float y = pathFunctionY(time);
            return new Vector2D<Float>(x, y);
        }
    }
}
