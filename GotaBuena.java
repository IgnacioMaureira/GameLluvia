package com.mygame.rain.entities;

import com.mygame.rain.interfaces.MovimientoStrategy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygame.rain.interfaces.Collectable;

public class GotaBuena extends GameObject implements Collectable {

    private static final int PUNTOS_VALOR = 10;
    private boolean recolectada;
    private final int puntos;

    public GotaBuena(float x, float y, Texture textura, MovimientoStrategy estrategia) {
        super(x, y, 64, 64);
        setTexture(textura);
        this.recolectada = false;
        this.puntos = PUNTOS_VALOR;

        setEstrategiaMovimiento(estrategia);
    }

    @Override
    protected void mover(float deltaTime) {
        aplicarEstrategiaMovimiento(deltaTime);
    }

    @Override
    public int getPoints() {
        return puntos;
    }

    @Override
    public void onCollect() {
        if (isCollectable()) {
            recolectada = true;
            setActive(false);
            System.out.println("¡Gota buena recolectada! +" + puntos + " puntos");
        }
    }

    @Override
    public boolean isCollectable() {
        return isActive() && !recolectada;
    }

    @Override
    public void dispose() {
        // textura compartida, no se elimina aquí
    }
}
