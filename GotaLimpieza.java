package com.mygame.rain.entities;

import com.mygame.rain.interfaces.MovimientoStrategy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygame.rain.interfaces.Collectable;

//Power UP
public class GotaLimpieza extends GameObject implements Collectable {

    private static final int PUNTOS_BONUS = 20;
    private boolean recolectada;

    public GotaLimpieza(float x, float y, Texture textura, MovimientoStrategy estrategia) {
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
        return PUNTOS_BONUS;
    }

    @Override
    public void onCollect() {
        if (isCollectable()) {
            recolectada = true;
            setActive(false);
            System.out.println("¡LIMPIEZA! Todas las gotas malas eliminadas. +" + PUNTOS_BONUS + " puntos");
        }
    }

    @Override
    public boolean isCollectable() {
        return isActive() && !recolectada;
    }

    public boolean esGotaLimpieza() {
        return true;
    }

    @Override
    public void dispose() {}
}
