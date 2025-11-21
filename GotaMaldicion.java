package com.mygame.rain.entities;

import com.mygame.rain.interfaces.MovimientoStrategy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygame.rain.interfaces.Collectable;

public class GotaMaldicion extends GameObject implements Collectable {

    private static final float DURACION_EFECTO = 10.0f;

    private boolean recolectada;

    public GotaMaldicion(float x, float y, Texture textura, MovimientoStrategy estrategia) {
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
            System.out.println("¡MALDICIÓN! Más gotas malas por " + DURACION_EFECTO + " segundos");
        }
    }

    @Override
    public boolean isCollectable() {
        return isActive() && !recolectada;
    }

    public boolean esGotaMaldicion() {
        return true;
    }

    public float getDuracionEfecto() {
        return DURACION_EFECTO;
    }

    @Override
    public void dispose() {}
}
