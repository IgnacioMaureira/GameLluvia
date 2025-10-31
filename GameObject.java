package com.mygame.rain.entities;

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
    private boolean active; // Estado del objeto
    
    
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
    
    // MÉTODO ABSTRACTO: cada hijo lo implementa según su comportamiento
    public abstract void update(float deltaTime);
    
    /**
     * Renderiza el objeto si está activo
     * PRINCIPIO: Template Method Pattern
     */
    public final void render(SpriteBatch batch) {
        if (active && texture != null) {
            beforeRender(batch);
            batch.draw(texture, x, y, width, height);
            afterRender(batch);
        }
    }
    
    // Hook methods para que las subclases extiendan funcionalidad
    protected void beforeRender(SpriteBatch batch) {
        // Puede ser sobrescrito por subclases
    }
    
    protected void afterRender(SpriteBatch batch) {
        // Puede ser sobrescrito por subclases
    }
    
    /**
     * Actualiza el rectángulo de colisión
     * PRIVATE: lógica interna, llamado automáticamente
     */
    private void updateBounds() {
        bounds.set(x, y, width, height);
    }
    
    // GETTERS - Acceso de solo lectura
    public float getX() { 
        return x; 
    }
    
    public float getY() { 
        return y; 
    }
    
    public float getWidth() { 
        return width; 
    }
    
    public float getHeight() { 
        return height; 
    }
    
    public Rectangle getBounds() { 
        return new Rectangle(bounds); // Retorna copia defensiva
    }
    
    public boolean isActive() {
        return active;
    }
    
    // SETTERS - Con validación y actualización automática de bounds
    public void setX(float x) { 
        this.x = x; 
        updateBounds();
    }
    
    public void setY(float y) { 
        this.y = y; 
        updateBounds();
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    // PROTECTED: subclases pueden acceder para modificar textura
    protected void setTexture(Texture texture) {
        this.texture = texture;
    }
    
    protected Texture getTexture() {
        return texture;
    }
    
    /**
     * Verifica colisión con otro objeto
     * PRINCIPIO: Tell, Don't Ask
     */
    public boolean collidesWith(GameObject other) {
        if (other == null || !this.active || !other.active) {
            return false;
        }
        return this.bounds.overlaps(other.bounds);
    }
    
    /**
     * Verifica si el objeto está fuera de los límites
     */
    public boolean isOutOfBounds(float minX, float maxX, float minY, float maxY) {
        return x + width < minX || x > maxX || y + height < minY || y > maxY;
    }
    
    // MÉTODO ABSTRACTO para liberar recursos
    public abstract void dispose();
}