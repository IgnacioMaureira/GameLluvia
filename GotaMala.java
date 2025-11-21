package com.mygame.rain.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygame.rain.interfaces.Collectable;
import com.mygame.rain.interfaces.MovimientoStrategy;

public class GotaMala extends GameObject implements Collectable {

    private static final int DANO = 1;
    private boolean recolectada;

    public GotaMala(float x, float y, Texture textura, MovimientoStrategy estrategia) {
        super(x, y, 64, 64);
        setTexture(textura);
        this.recolectada = false;

        setEstrategiaMovimiento(estrategia);
    }

    @Override
    protected void mover(float deltaTime) {
        aplicarEstrategiaMovimiento(deltaTime);
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public void onCollect() {
        if (isCollectable()) {
            recolectada = true;
            setActive(false);
            System.out.println("¡Gota mala! -" + DANO + " vida");
        }
    }

    @Override
    public boolean isCollectable() {
        return isActive() && !recolectada;
    }

    public int getDanio() {
        return DANO;
    }

    @Override
    public void dispose() {}
}
