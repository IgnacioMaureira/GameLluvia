package com.mygame.rain.managers;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygame.rain.interfaces.Collectable;
import com.mygame.rain.entities.GameObject;

/**
 * Manager para gestionar objetos coleccionables
 * PRINCIPIOS:
 * - Single Responsibility: solo maneja lógica de colección
 * - Dependency Inversion: depende de abstracción (Collectable)
 * - Encapsulamiento: datos privados, comportamiento público
 */
public class CollectionManager {
    // ENCAPSULAMIENTO
    private final Array<Collectable> collectables;
    
    public CollectionManager() {
        this.collectables = new Array<>();
    }
    
    /**
     * Agrega un objeto coleccionable
     */
    public void addCollectable(Collectable item) {
        if (item == null) {
            throw new IllegalArgumentException("Collectable no puede ser null");
        }
        collectables.add(item);
    }
    
    /**
     * Intenta colectar objetos en el área especificada
     * PRINCIPIO: Tell, Don't Ask
     * @return resultado de la colección
     */
    public CollectionResult collectInArea(Rectangle collectorBounds) {
        if (collectorBounds == null) {
            throw new IllegalArgumentException("Bounds no pueden ser null");
        }
        
        int itemsCollectedNow = 0;
        Array<Collectable> collected = new Array<>();
        
        for (Collectable item : collectables) {
            if (item.isCollectable() && item instanceof GameObject) {
                GameObject obj = (GameObject) item;
                
                if (obj.getBounds().overlaps(collectorBounds)) {
                    item.onCollect();
                    itemsCollectedNow++;
                    collected.add(item);
                }
            }
        }
        
        return new CollectionResult(itemsCollectedNow, collected);
    }
    
    /**
     * Remueve objetos ya colectados
     * @return cantidad de objetos removidos
     */
    public int removeCollected() {
        Array<Collectable> toRemove = new Array<>();
        
        for (Collectable item : collectables) {
            if (!item.isCollectable()) {
                toRemove.add(item);
            }
        }
        
        collectables.removeAll(toRemove, true);
        return toRemove.size;
    }
    
    /**
     * Reinicia el manager
     */
    public void reset() {
        collectables.clear();
    }
    
    // ===== CLASE INTERNA (Value Object) =====
    
    /**
     * Resultado de una operación de colección
     * PRINCIPIO: Immutable Value Object
     */
    public static class CollectionResult {
        private final int itemsCollected;
        private final Array<Collectable> collectedItems;
        
        public CollectionResult(int items, Array<Collectable> collected) {
            this.itemsCollected = items;
            this.collectedItems = new Array<>(collected); // copia defensiva
        }
        
        public int getItemsCollected() { return itemsCollected; }
        
        public Array<Collectable> getCollectedItems() { 
            return new Array<>(collectedItems); 
        }
    }
}