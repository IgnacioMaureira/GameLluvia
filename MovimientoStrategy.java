package com.mygame.rain.interfaces;

import com.mygame.rain.entities.GameObject;

public interface MovimientoStrategy {
	void mover(GameObject obj , float deltaTiempo);
}
