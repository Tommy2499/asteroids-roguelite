package com.pluto.game;

public enum Difficulty {
    EASY(0.5F, 4, 0.75f),
    MEDIUM(1.0F, 2, 1f),
    HARD(2.0F, 1, 1.5f);

    private final float scoreMultiplier;
    private final int bulletDamage;
    private final float enemySpeedMultiplier;

    Difficulty(float scoreMultiplier, int bulletDamage, float enemySpeedMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
        this.bulletDamage = bulletDamage;
        this.enemySpeedMultiplier = enemySpeedMultiplier;
    }

    public float getScoreMultiplier() {
        return scoreMultiplier;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public float getEnemySpeedMultiplier() {
        return enemySpeedMultiplier;
    }
}
