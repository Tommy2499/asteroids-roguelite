package com.pluto.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A class that represents the game manager. It contains all the attributes
 * needed to render the game on the screen, and associated methods to
 * manipulate the objects during the game.
 */
public class GameManager {

    /* The player object */
    private Spaceship player;

    /* The list of asteroids */
    private ArrayList<Enemy> enemies;

    /* The list of bullets */
    private ArrayList<Bullet> playerBullets;

    /* The time in seconds the game has been running */
    private float time;

    /* The current score */
    private int score;

    /* The current level */
    private int level;

    /* Contains data per each level */
    private JSONObject levelData;

    /* Whether or not the game is running */
    public boolean is_running;

    /* The difficulty of the game */
    private Difficulty difficulty;

    /*
     * Width and height of the screen. These are units that can be scaled to fit
     * window
     */
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;

    /* The maximum number of bullets allowed at a time */
    private static final int MAX_BULLETS = 10;

    /* The amount of time a bullet lasts on the screen in seconds */
    private static final float BULLET_LIFETIME = 2.0f;

    /* How far the asteroids spawn away from the player */
    private static final int PROTECTED_DISTANCE = 300;

    /* Maximum speed of the asteroids */
    private static final int MAX_ASTEROID_SPEED = 100;

    /* How much score destroying asteroids gives */
    private static final int SCORE_PER_ASTEROID = 10;

    /* How much score destroying aliens gives */
    private static final int SCORE_PER_ALIEN = 50;

    /* How much score for completing a level */
    private static final int SCORE_PER_LEVEL = 100;

    /* Default path to level data */
    private static final String LEVEL_DATA_PATH = 
        "src/main/java/com/pluto/game/LevelData.json";

    /**
     * Constructor for the GameManager class. Initializes the player, enemies, and
     * bullets.
     * Spawns the starting asteroids and sets the game to running.
     */
    public GameManager(Difficulty difficulty) {
        this.player = new Spaceship();
        this.enemies = new ArrayList<Enemy>();
        this.playerBullets = new ArrayList<Bullet>();
        this.time = 0.0f;
        this.score = 0;
        this.level = 1;
        this.difficulty = difficulty;
        is_running = true;
        readLevelData(LEVEL_DATA_PATH);
        startCurrentLevel();
    }

    /**
     * Updates the game by one frame. It updates the player, enemies, and bullets.
     * Checks for and handles collisions and updates the score and time.
     * 
     * @param dt    - the amount of time in seconds since the last update
     * @param input - the player inputs
     */
    public void update(float dt, Spaceship.Input[] input) {
        if (dt == 0) {
            return;
        }

        // Move all objects
        player.moveObj(dt, input);
        for (Enemy enemy : enemies) {
            enemy.moveObj(dt * difficulty.getEnemySpeedMultiplier());
        }
        for (Bullet bullet : playerBullets) {
            bullet.moveObj(dt);
        }

        // Handle enemy shooting
        ListIterator<Enemy> enemyIterator = enemies.listIterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (!(enemy instanceof ShooterEnemy))
                continue;
            Bullet[] enemyBullets = ((ShooterEnemy) enemy).shootPlayer(player.getPosition());
            for (Bullet bullet : enemyBullets) {
                enemyIterator.add(bullet);
            }
        }

        // Shoot bullets
        for (Spaceship.Input i : input) {
            if (i == Spaceship.Input.SHOOT) {
                playerShoot();
            }
        }

        // Check for collisions
        checkAndHandleCollisions();

        // Despawn old bullets
        despawnBullets();

        // Check if all enemies are destroyed and if so, handle new level
        if (enemies.size() == 0) {
            playerBullets.clear();
            score += SCORE_PER_LEVEL * level * difficulty.getScoreMultiplier();
            level++;
            startCurrentLevel();
        }

        // Update time
        if (is_running) {
            time += dt;
        }
    }

    /**
     * Despawns old Bullet objects. A Bullet is considered old if its
     * time alive is greater than BULLET_LIFETIME.
     */
    private void despawnBullets() {
        // Despawn player bullets, uses iterator to avoid concurrent modification
        // exception
        Iterator<Bullet> bulletIterator = playerBullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (bullet.getTimeAlive() > BULLET_LIFETIME) {
                bulletIterator.remove();
            }
        }

        // Despawn enemy bullets
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy instanceof Bullet
                    && ((Bullet) enemy).getTimeAlive() > BULLET_LIFETIME)
                enemyIterator.remove();
        }
    }

    /**
     * Checks for collisions between the player, enemies, and bullets.
     * If a collision is detected, the appropriate action is taken.
     */
    private void checkAndHandleCollisions() {
        // Check for collisions between bullets and asteroids
        // Uses iterators to avoid concurrent modification exception
        Iterator<Bullet> bulletIterator = playerBullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Enemy> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                if (!bullet.collidesWith(enemy)) {
                    continue;
                }

                // Hit enemy
                enemy.takeDamage(bullet.dealsDamage() * difficulty.getBulletDamage());
                if (enemy.getHealth() == 0)
                    destroyEnemy(enemy);

                bulletIterator.remove();
                break;
            }
        }

        // Check for collisions between player and asteroids
        for (Enemy enemy : enemies) {
            if (player.collidesWith(enemy)) {
                player.hit();
                if (player.getLives() == 0) {
                    gameOver();
                }
            }
        }
    }

    /**
     * Spawns a new bullet at the player's location. Will not spawn a bullet
     * if the maximum number of bullets has been reached.
     */
    private void playerShoot() {
        if (playerBullets.size() < MAX_BULLETS) {
            Bullet bullet = player.shootBullet();
            playerBullets.add(bullet);
        }
    }

    /**
     * Spawns enemies according to current level.
     */
    private void startCurrentLevel() {
        // First check if there is level data
        int numAsteroids;
        int numComets;
        int numAliens;
        if (this.level > 22 || levelData == null) {
            numAsteroids = this.level;
            numComets = this.level / 5;
            numAliens = this.level / 15;
        } else {
            String lvl = this.level + "";
            JSONObject currentLevelJson = this.levelData.getJSONObject(lvl);
            // Get the number of enemies from the json file
            numAsteroids = currentLevelJson.getInt("ASTEROID");
            numComets = currentLevelJson.getInt("COMET");
            numAliens = currentLevelJson.getInt("ALIEN");
        }
        for (int i = 0; i < numAsteroids; i++) {
            spawnEnemy(EnemyType.ASTEROID);
        }
        for (int i = 0; i < numComets; i++) {
            spawnEnemy(EnemyType.COMET);
        }
        for (int i = 0; i < numAliens; i++) {
            spawnEnemy(EnemyType.ALIEN);
        }
    }

    /**
     * Spawns a new asteroid at a random location on the screen.
     * The asteroid will always spawn at least PROTECTED_DISTANCE units away from
     * the player.
     *
     * @param type - the EnemyType of the enemy to spawn.
     */
    private void spawnEnemy(EnemyType type) {
        // Picks a random location on the screen, checks if it is at least
        // PROTECTED_DISTANCE units away from the player
        // If not, it will try again up to 1000 times before giving up. The odds of this
        // happening are very very low.
        float x = (float) (Math.random() * SCREEN_WIDTH);
        float y = (float) (Math.random() * SCREEN_HEIGHT);
        int maxAttempts = 1000;
        int attempts = 0;
        while (attempts < maxAttempts) {
            x = (float) (Math.random() * SCREEN_WIDTH);
            y = (float) (Math.random() * SCREEN_HEIGHT);
            if (Math.sqrt(Math.pow(x - player.getPosition().x, 2)
                    + Math.pow(y - player.getPosition().y, 2)) > PROTECTED_DISTANCE) {
                break;
            }
            attempts++;
        }
        float orientation = (float) (Math.random() * 2 * Math.PI);
        float rotVelocity = (float) Math.random();
        Vector2D<Float> pos = new Vector2D<Float>(x, y);

        Enemy enemy;
        if (type == EnemyType.ASTEROID) {
            // Math.random() returns a value between 0 and 1, so we multiply by 2 and
            // subtract 1 to get a value between -1 and 1
            Vector2D<Float> velocity = new Vector2D<Float>(
                    ((float) (Math.random() * 2) - 1) * MAX_ASTEROID_SPEED,
                    ((float) (Math.random() * 2) - 1) * MAX_ASTEROID_SPEED);
            enemy = new Asteroid(
                    pos, velocity, orientation, Asteroid.AsteroidSize.LARGE, rotVelocity);

        } else if (type == EnemyType.COMET) {
            // Comets may be twice as fast as asteroids
            Vector2D<Float> velocity = new Vector2D<Float>(
                    ((float) (Math.random() * 2) - 1) * MAX_ASTEROID_SPEED * 2,
                    ((float) (Math.random() * 2) - 1) * MAX_ASTEROID_SPEED * 2);
            enemy = new Asteroid(
                    pos, velocity, orientation, Asteroid.AsteroidSize.COMET, rotVelocity);

        } else if (type == EnemyType.ALIEN) {
            // Aliens have built in velocity.
            enemy = new Alien(pos, null);
        } else {
            return;
        }

        enemies.add(enemy);
    }

    /**
     * Destroys an enemy and may spawn additional enemies in its place.
     *
     * @param enemy - the Enemy object to be destroyed
     */
    private void destroyEnemy(Enemy enemy) {
        switch (enemy.type()) {
            case ASTEROID:
                score += SCORE_PER_ASTEROID * level * difficulty.getScoreMultiplier();
                destroyAsteroid((Asteroid) enemy);
                break;
            case COMET:
                // Destroyed comet, spawn Alien in its place
                score += SCORE_PER_ASTEROID * level * difficulty.getScoreMultiplier();
                Alien alien = new Alien(enemy.getPosition(), null);
                enemies.remove(enemy);
                enemies.add(alien);
                break;
            case ALIEN:
                score += SCORE_PER_ALIEN * level * difficulty.getScoreMultiplier();
                destroyAlien((Alien) enemy);
                break;
            case BULLET:
                enemies.remove(enemy);
                break;
            default:
                return;
        }
    }

    /**
     * Destroys an asteroid and may spawn smaller asteroids in its place and
     * updates the player score.
     *
     * @param asteroid - the asteroid object to be destroyed
     */
    private void destroyAsteroid(Asteroid asteroid) {
        Asteroid.AsteroidSize new_size = Asteroid.AsteroidSize.MEDIUM;
        if (asteroid.size == Asteroid.AsteroidSize.MEDIUM) {
            new_size = Asteroid.AsteroidSize.SMALL;
        } else if (asteroid.size == Asteroid.AsteroidSize.SMALL) {
            enemies.remove(asteroid);
            return;
        }

        // Spawn children asteroids
        for (int i = 0; i < 2; i++) {
            // Adds random velocity to the destroyed asteroid's velocity, so smaller
            // asteroids can be faster
            Vector2D<Float> velocity = new Vector2D<Float>(
                    asteroid.getVelocity().x +
                            ((float) (Math.random() * 2) - 1) * MAX_ASTEROID_SPEED,
                    asteroid.getVelocity().y +
                            ((float) (Math.random() * 2) - 1) * MAX_ASTEROID_SPEED);

            enemies.add(new Asteroid(asteroid.getPosition(),
                    velocity,
                    asteroid.getOrientation(),
                    new_size,
                    (float) Math.random()));
        }

        enemies.remove(asteroid);
    }

    /**
     * Destroys an Alien object.
     */
    private void destroyAlien(Alien alien) {
        enemies.remove(alien);
    }

    /**
     * Reads a .json file into memory and creates a levelData json object.
     */
    private void readLevelData(String path) {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(path)));
            levelData = new JSONObject(jsonString).getJSONObject("levels");
        } catch (IOException e) {
            levelData = null;
        } catch (JSONException e) {
            levelData = null;
        }
    }

    /**
     * Is called when the game is over.
     */
    private void gameOver() {
        is_running = false;
    }

    /**
     * Converts the game state to Json format for the frontend.
     * Returns the game state of the player, asteroids, and bullets.
     * Also includes the current score, current level, time, and whether the game is
     * running.
     *
     * @return - a json formatted string representing this GameManager
     */
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"player\":");
        json.append(player.toJson());
        json.append(",\"enemies\":[");
        for (int i = 0; i < enemies.size(); i++) {
            json.append(enemies.get(i).toJson());
            if (i < enemies.size() - 1) {
                json.append(",");
            }
        }
        json.append("],\"bullets\":[");
        for (int i = 0; i < playerBullets.size(); i++) {
            json.append(playerBullets.get(i).toJson());
            if (i < playerBullets.size() - 1) {
                json.append(",");
            }
        }
        json.append("],\"score\":");
        json.append(score);
        json.append(",\"level\":");
        json.append(level);
        json.append(",\"time\":");
        json.append(time);
        json.append(",\"is_running\":");
        json.append(is_running);
        json.append("}");
        return json.toString();
    }
}
