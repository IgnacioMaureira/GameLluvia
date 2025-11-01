package com.mygame.rain.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygame.rain.entities.GameObject;

public class GameObjectManager {
    private final Array<GameObject> gameObjects;
    private final Array<GameObject> objectsToAdd;
    private final Array<GameObject> objectsToRemove;
    
    public GameObjectManager() {
        this.gameObjects = new Array<>();
        this.objectsToAdd = new Array<>();
        this.objectsToRemove = new Array<>();
    }
    
    public void addGameObject(GameObject obj) {
        if (obj == null) {
            throw new IllegalArgumentException("GameObject no puede ser null");
        }
        objectsToAdd.add(obj);
    }
    

    public void removeGameObject(GameObject obj) {
        if (obj != null) {
            objectsToRemove.add(obj);
        }
    }
    

    public void updateAll(float deltaTime) {
        processPendingChanges();
        
        for (GameObject obj : gameObjects) {
            if (obj.isActive()) {
                obj.update(deltaTime);
            }
        }
    }
    
    /**
     * Renderiza todos los objetos activos
     */
    public void renderAll(SpriteBatch batch) {
        for (GameObject obj : gameObjects) {
            if (obj.isActive()) {
                obj.render(batch);
            }
        }
    }
    

    public GameObject findCollision(Rectangle bounds) {
        if (bounds == null) {
            return null;
        }
        
        for (GameObject obj : gameObjects) {
            if (obj.isActive() && obj.collidesWith(createTempObject(bounds))) {
                return obj;
            }
        }
        return null;
    }
    

    public Array<GameObject> findAllCollisions(Rectangle bounds) {
        Array<GameObject> collisions = new Array<>();
        
        if (bounds == null) {
            return collisions;
        }
        
        for (GameObject obj : gameObjects) {
            if (obj.isActive() && obj.collidesWith(createTempObject(bounds))) {
                collisions.add(obj);
            }
        }
        return collisions;
    }
    

    private GameObject createTempObject(Rectangle bounds) {
        return new GameObject(bounds.x, bounds.y, bounds.width, bounds.height) {
            @Override
            public void update(float deltaTime) {}
            
            @Override
            public void dispose() {}
        };
    }
    

    public int removeOutOfBounds(float minX, float maxX, float minY, float maxY) {
        int removedCount = 0;
        
        for (GameObject obj : gameObjects) {
            if (obj.isOutOfBounds(minX, maxX, minY, maxY)) {
                removeGameObject(obj);
                removedCount++;
            }
        }
        
        return removedCount;
    }
    

    public int removeInactive() {
        int removedCount = 0;
        
        for (GameObject obj : gameObjects) {
            if (!obj.isActive()) {
                removeGameObject(obj);
                removedCount++;
            }
        }
        
        return removedCount;
    }
    
    private void processPendingChanges() {
        if (objectsToAdd.size > 0) {
            gameObjects.addAll(objectsToAdd);
            objectsToAdd.clear();
        }
        
        if (objectsToRemove.size > 0) {
            gameObjects.removeAll(objectsToRemove, true);
            objectsToRemove.clear();
        }
    }
    

    public Array<GameObject> getGameObjects() {
        Array<GameObject> copy = new Array<>();
        copy.addAll(gameObjects);
        return copy;
    }
    

    public int getActiveCount() {
        int count = 0;
        for (GameObject obj : gameObjects) {
            if (obj.isActive()) {
                count++;
            }
        }
        return count;
    }
    

    public int getTotalCount() {
        return gameObjects.size;
    }
    

    public void clear() {
        for (GameObject obj : gameObjects) {
            obj.dispose();
        }
        gameObjects.clear();
        objectsToAdd.clear();
        objectsToRemove.clear();
    }
    

    public void dispose() {
        clear();
    }
}
