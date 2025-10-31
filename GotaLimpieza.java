package com.mygame.rain.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygame.rain.interfaces.Collectable;

//Power UP
public class GotaLimpieza extends GameObject implements Collectable{
	private static final float velocidad_Caida = 150f; // Más lenta (más fácil de atrapar)
    private static final int puntos_Bonus = 20;
    
    private float velocidad;
    private final int puntos;
    private boolean recolectada;
    
    public GotaLimpieza(float x, float y, Texture texture) {
        super(x, y, 64, 64);
        setTexture(texture);
        this.velocidad = velocidad_Caida;
        this.puntos = puntos_Bonus;
        this.recolectada = false;
    }
    
    @Override
    public void update(float deltaTime) {
        if (isActive() && !recolectada) {
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
            System.out.println("¡LIMPIEZA! Todas las gotas malas eliminadas. +" + puntos + " puntos");
        }
    }
    
    public boolean isCollectable() {
        return isActive() && !recolectada;
    }
    
    
     //Identifica que esta es una gota de limpieza 
    public boolean esGotaLimpieza() {
        return true;
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
    }
}
