package com.mygame.rain.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygame.rain.interfaces.Collectable;

public class GotaMala extends GameObject implements Collectable{
	private static final float velocidad_Caida = 200f;
	private static final int daño = 1; //Quita 1 vida
	
	private float velocidad;
	private final int dano;
	private boolean recolectada;
	
	public GotaMala(float x, float y, Texture texture) {
		super(x,y,64,64);
		setTexture(texture);
		this.velocidad= velocidad_Caida;
		this.dano= daño;
		this.recolectada= false;
	}
	
	@Override
	public void update(float deltaTime) {
		if(isActive() && !recolectada) {
			//caer hacia abajo
			setY(getY()-velocidad * Gdx.graphics.getDeltaTime());
		}
	}
	
	//implementacion de Collectable
	public int getPoints() {
		return 0; // No da puntos, da daño
	}
	
	public void onCollect() {
		if(isCollectable()) {
			recolectada = true;
			setActive(false);
			System.out.println("¡Gota mala! -"+ dano +"vida");
		}
	}
	
	public boolean isCollectable() {
        return isActive() && !recolectada;
    }
    
    // Método específico para gotas malas
    public int getDanio() {
        return dano;
    }
    
    public boolean esMala() {
        return true;
    }
    
    // Getters/Setters
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