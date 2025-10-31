package com.mygame.rain.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygame.rain.interfaces.Collectable;

public class GotaMaldicion extends GameObject implements Collectable {
	private static final float VELOCIDAD_CAIDA = 250f;
    private static final float DURACION_EFECTO = 10.0f; // 10 segundos
    
    private float velocidad;
    private boolean recolectada;
    
    public GotaMaldicion(float x, float y, Texture texture) {
        super(x, y, 64, 64);
        setTexture(texture);
        this.velocidad = VELOCIDAD_CAIDA;
        this.recolectada = false;
    }
    
    @Override
    public void update(float deltaTime) {
        if (isActive() && !recolectada) {
            setY(getY() - velocidad * Gdx.graphics.getDeltaTime());
        }
    }
    
    @Override
    public int getPoints() {
        return 0; // No da puntos
    }
    
    @Override
    public void onCollect() {
        if (isCollectable()) {
            recolectada = true;
            setActive(false);
            System.out.println("¡MALDICIÓN! Más gotas malas por " + DURACION_EFECTO + " segundos");
        }
    }
    
    @Override
    public boolean isCollectable() {
        return isActive() && !recolectada;
    }
    
    // Identifica que esta es una gota de maldición
    public boolean esGotaMaldicion() {
        return true;
    }
    
    // Duración del efecto de maldición
    public float getDuracionEfecto() {
        return DURACION_EFECTO;
    }
    
    public float getVelocidad() {
        return velocidad;
    }
    
    public void setVelocidad(float velocidad) {
        if (velocidad < 0) {
            throw new IllegalArgumentException("Velocidad debe ser positiva");
        }
        this.velocidad = velocidad;
    }
    
    @Override
    public void dispose() {
        // No hay recursos adicionales que liberar
    }
}
