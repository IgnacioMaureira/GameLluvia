package com.mygame.rain.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygame.rain.interfaces.Collectable;

public class GotaBuena extends GameObject implements Collectable {
	private static final float velocidad_Caida = 300f;
    private static final int puntos_Valor = 10;
    
    private float velocidad;
    private final int puntos;
    private boolean recolectada;
    
    public GotaBuena(float x, float y, Texture texture) {
        super(x, y, 64, 64);
        setTexture(texture);
        this.velocidad = velocidad_Caida;
        this.puntos = puntos_Valor;
        this.recolectada = false;
    }
    
    @Override
    public void update(float deltaTime) {
        if (isActive() && !recolectada) {
            // Caer hacia abajo
            setY(getY() - velocidad * Gdx.graphics.getDeltaTime());
        }
    }
    
    // Implementación de Collectable
    public int getPoints() {
        return puntos;
    }
    
    public void onCollect() {
        if (isCollectable()) {
            recolectada = true;
            setActive(false);
            System.out.println("¡Gota buena recolectada! +" + puntos + " puntos");
        }
    }
    
    public boolean isCollectable() {
        return isActive() && !recolectada;
    }
    
    // Getters
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
        // Textura compartida, no se dispone aquí
    }
}
