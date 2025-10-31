package com.mygame.rain.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygame.rain.entities.GameObject;

/**
 * Manager que gestiona todos los GameObjects
 * PRINCIPIOS APLICADOS:
 * - Single Responsibility: solo maneja ciclo de vida de objetos
 * - Encapsulamiento: lista privada, acceso controlado
 * - Tell, Don't Ask: los objetos se gestionan a sí mismos
 */
public class GameObjectManager {
    // ENCAPSULAMIENTO: atributos private
    private final Array<GameObject> gameObjects;
    private final Array<GameObject> objectsToAdd;
    private final Array<GameObject> objectsToRemove;
    
    public GameObjectManager() {
        this.gameObjects = new Array<>();
        this.objectsToAdd = new Array<>();
        this.objectsToRemove = new Array<>();
    }
    
    /**
     * Agrega un objeto de forma segura
     * PRINCIPIO: No modificar colección durante iteración
     */
    public void addGameObject(GameObject obj) {
        if (obj == null) {
            throw new IllegalArgumentException("GameObject no puede ser null");
        }
        objectsToAdd.add(obj);
    }
    
    /**
     * Marca un objeto para remover
     */
    public void removeGameObject(GameObject obj) {
        if (obj != null) {
            objectsToRemove.add(obj);
        }
    }
    
    /**
     * Actualiza todos los objetos activos
     * PRINCIPIO: Polimorfismo - cada objeto sabe cómo actualizarse
     */
    public void updateAll(float deltaTime) {
        // Procesar adiciones y remociones pendientes
        processPendingChanges();
        
        // Actualizar objetos activos
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
    
    /**
     * Encuentra el primer objeto que colisiona con el área dada
     * @return GameObject que colisiona, o null
     */
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
    
    /**
     * Encuentra todos los objetos que colisionan
     * PRINCIPIO: Separation of Concerns
     */
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
    
    /**
     * Objeto temporal para verificar colisiones
     * PRINCIPIO: Evitar crear múltiples instancias innecesarias
     */
    private GameObject createTempObject(Rectangle bounds) {
        return new GameObject(bounds.x, bounds.y, bounds.width, bounds.height) {
            @Override
            public void update(float deltaTime) {}
            
            @Override
            public void dispose() {}
        };
    }
    
    /**
     * Remueve objetos fuera de los límites
     */
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
    
    /**
     * Remueve objetos inactivos
     */
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
    
    /**
     * Procesa adiciones y remociones pendientes
     * PRIVATE: detalle de implementación interno
     */
    private void processPendingChanges() {
        // Agregar nuevos objetos
        if (objectsToAdd.size > 0) {
            gameObjects.addAll(objectsToAdd);
            objectsToAdd.clear();
        }
        
        // Remover objetos marcados
        if (objectsToRemove.size > 0) {
            gameObjects.removeAll(objectsToRemove, true);
            objectsToRemove.clear();
        }
    }
    
    /**
     * Retorna copia defensiva de la lista
     * ENCAPSULAMIENTO: no exponer colección interna directamente
     */
    public Array<GameObject> getGameObjects() {
        Array<GameObject> copy = new Array<>();
        copy.addAll(gameObjects);
        return copy;
    }
    
    /**
     * Retorna cantidad de objetos activos
     */
    public int getActiveCount() {
        int count = 0;
        for (GameObject obj : gameObjects) {
            if (obj.isActive()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Retorna total de objetos
     */
    public int getTotalCount() {
        return gameObjects.size;
    }
    
    /**
     * Limpia todos los objetos y libera recursos
     */
    public void clear() {
        for (GameObject obj : gameObjects) {
            obj.dispose();
        }
        gameObjects.clear();
        objectsToAdd.clear();
        objectsToRemove.clear();
    }
    
    /**
     * Libera recursos
     */
    public void dispose() {
        clear();
    }
}
