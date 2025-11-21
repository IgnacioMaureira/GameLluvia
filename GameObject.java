package com.mygame.rain.entities;

import com.mygame.rain.interfaces.MovimientoStrategy;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
    private float x;
    private float y;
    private float width;
    private float height;
    private Texture texture;
    private Rectangle bounds;
    private boolean active;

    private MovimientoStrategy estrategiaMovimiento;

    public GameObject(float x, float y, float width, float height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width y height deben ser positivos");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
        this.active = true;
    }

    // TEMPLATE METHOD
    public final void update(float deltaTime) {
        if (!active) return;

        beforeUpdate(deltaTime);
        mover(deltaTime);
        updateBounds();
        afterUpdate(deltaTime);
    }

    protected void beforeUpdate(float deltaTime) {}

    protected void afterUpdate(float deltaTime) {}

    protected abstract void mover(float deltaTime);

    // RENDER
    public final void render(SpriteBatch batch) {
        if (active && texture != null) {
            beforeRender(batch);
            batch.draw(texture, x, y, width, height);
            afterRender(batch);
        }
    }

    protected void beforeRender(SpriteBatch batch) {}

    protected void afterRender(SpriteBatch batch) {}

    private void updateBounds() {
        bounds.set(x, y, width, height);
    }

    // GETTERS
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public Rectangle getBounds() { return new Rectangle(bounds); }
    public boolean isActive() { return active; }

    // SETTERS
    public void setX(float x) { this.x = x; updateBounds(); }
    public void setY(float y) { this.y = y; updateBounds(); }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void setActive(boolean active) { this.active = active; }

    protected void setTexture(Texture texture) { this.texture = texture; }
    protected Texture getTexture() { return texture; }

    public boolean collidesWith(GameObject other) {
        if (other == null || !this.active || !other.active) return false;
        return this.bounds.overlaps(other.bounds);
    }

    public boolean isOutOfBounds(float minX, float maxX, float minY, float maxY) {
        return x + width < minX || x > maxX || y + height < minY || y > maxY;
    }

    // STRATEGY
    public void setEstrategiaMovimiento(MovimientoStrategy estrategiaMovimiento) {
        this.estrategiaMovimiento = estrategiaMovimiento;
    }

    public MovimientoStrategy getEstrategiaMovimiento() {
        return estrategiaMovimiento;
    }

    protected void aplicarEstrategiaMovimiento(float deltaTime) {
        if (estrategiaMovimiento != null) {
            estrategiaMovimiento.mover(this, deltaTime);
        }
    }
    public abstract void dispose();
}
