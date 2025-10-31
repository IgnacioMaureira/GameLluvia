package com.mygame.rain.interfaces;

public interface Collectable {
    int getPoints();
    void onCollect();
    boolean isCollectable();
}