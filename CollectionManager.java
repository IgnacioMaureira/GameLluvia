package com.mygame.rain.managers;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygame.rain.interfaces.Collectable;
import com.mygame.rain.entities.GameObject;

public class CollectionManager {
    private final Array<Collectable> collectables;
    
    public CollectionManager() {
        this.collectables = new Array<>();
    }
    
    public void addCollectable(Collectable item) {
        if (item == null) {
            throw new IllegalArgumentException("Collectable no puede ser null");
        }
        collectables.add(item);
    }
    

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
    

    public void reset() {
        collectables.clear();
    }
    

    public static class CollectionResult {
        private final int itemsCollected;
        private final Array<Collectable> collectedItems;
        
        public CollectionResult(int items, Array<Collectable> collected) {
            this.itemsCollected = items;
            this.collectedItems = new Array<>(collected);
        }
        
        public int getItemsCollected() { return itemsCollected; }
        
        public Array<Collectable> getCollectedItems() { 
            return new Array<>(collectedItems); 
        }
    }
}
