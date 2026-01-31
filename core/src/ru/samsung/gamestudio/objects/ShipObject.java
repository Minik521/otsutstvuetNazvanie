package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import ru.samsung.gamestudio.GameSettings;

public class ShipObject extends GameObject {

    long lastShotTime;
    int livesLeft;
    int bulletCount;
    int damageAmount;

    public ShipObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.SHIP_BIT, world);
        body.setLinearDamping(10);
        livesLeft = 3;
        initializeRandomValues();
    }

    private void initializeRandomValues() {
        bulletCount = MathUtils.random(2, 4);

        damageAmount = MathUtils.random(2, 4);
    }

    public int getLiveLeft() {
        return livesLeft;
    }

    @Override
    public void draw(SpriteBatch batch) {
        putInFrame();
        super.draw(batch);
    }

    public void move(Vector3 vector3) {
        body.applyForceToCenter(new Vector2(
                        (vector3.x - getX()) * GameSettings.SHIP_FORCE_RATIO,
                        (vector3.y - getY()) * GameSettings.SHIP_FORCE_RATIO),
                true
        );
    }

    private void putInFrame() {
        if (getY() > (GameSettings.SCREEN_HEIGHT / 2f - height / 2f)) {
            setY((int) (GameSettings.SCREEN_HEIGHT / 2f - height / 2f));
        }
        if (getY() <= (height / 2f)) {
            setY(height / 2);
        }
        if (getX() < (-width / 2f)) {
            setX(GameSettings.SCREEN_WIDTH);
        }
        if (getX() > (GameSettings.SCREEN_WIDTH + width / 2f)) {
            setX(0);
        }
    }

    public boolean needToShoot() {
        if (TimeUtils.millis() - lastShotTime >= GameSettings.SHOOTING_COOL_DOWN) {
            lastShotTime = TimeUtils.millis();
            return true;
        }
        return false;
    }

    @Override
    public void hit() {
        livesLeft -= damageAmount;
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

    public Vector2[] getBulletPositions() {
        Vector2[] positions = new Vector2[bulletCount];
        float spacing = 150f;

        for (int i = 0; i < bulletCount; i++) {
            float offsetX = (i - (bulletCount - 1) / 2f) * spacing;
            positions[i] = new Vector2(
                    getX() + offsetX,
                    getY() + height / 2
            );
        }

        return positions;
    }

    public int getBulletCount() {
        return bulletCount;
    }

    public int getDamageAmount() {
        return damageAmount;
    }

    public void resetRandomValues() {
        initializeRandomValues();
    }
}