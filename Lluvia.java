package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import com.mygame.rain.entities.*;
import com.mygame.rain.interfaces.Collectable;
import com.mygame.rain.managers.GameObjectManager;
import com.mygame.rain.managers.CollectionManager;

/**
 * Clase principal que gestiona la lluvia de gotas.
 * Carga imágenes desde la carpeta assets/ y controla el ciclo de vida de las gotas.
 */
public class Lluvia {
	// Managers OO
	private GameObjectManager objectManager;
	private CollectionManager collectionManager;
	
	// Texturas
	private Texture gotaBuenaTexture;
	private Texture gotaMalaTexture;
	private Texture gotaLimpiezaTexture;
	private Texture gotaMaldicionTexture;
	
	// Audio
	private Sound dropSound;
	private Music rainMusic;
	
	// Temporización
	private long lastDropTime;
	private long lastSpecialDropTime;
	
	// Estado
	private boolean maldicionActiva;
	private float tiempoMaldicionRestante;
	private int probabilidadGotaMala;
	
	// Configuración
	private static final long INTERVALO_GOTAS = 200000000; // 0.2s
	private static final long INTERVALO_ESPECIALES = 10000000000L; // 10s
	private static final int PROBABILIDAD_NORMAL = 60;
	private static final int PROBABILIDAD_MALDICION = 40;

	/**
	 * Constructor CON gotas especiales (ÚNICO CONSTRUCTOR)
	 */
	public Lluvia(Texture gotaBuena, Texture gotaMala, 
	             Texture gotaLimpieza, Texture gotaMaldicion,
	             Sound ss, Music mm) {
		this.rainMusic = mm;
		this.dropSound = ss;
		this.gotaBuenaTexture = gotaBuena;
		this.gotaMalaTexture = gotaMala;
		this.gotaLimpiezaTexture = gotaLimpieza;
		this.gotaMaldicionTexture = gotaMaldicion;
		this.probabilidadGotaMala = PROBABILIDAD_NORMAL;
		this.maldicionActiva = false;
	}

	public void crear() {
		objectManager = new GameObjectManager();
		collectionManager = new CollectionManager();
		crearGotaDeLluvia();
		
		rainMusic.setLooping(true);
		rainMusic.play();
	}

	/**
	 * ✅ USA GameObject (GM1.4)
	 * Crea gotas normales (buenas o malas)
	 */
	private void crearGotaDeLluvia() {
		float x = MathUtils.random(0, 800-64);
		float y = 480;
		
		GameObject nuevaGota; // Variable tipo abstracto
		
		// Decidir tipo según probabilidad actual
		int random = MathUtils.random(1, 100);
		
		if (random <= probabilidadGotaMala) {
			nuevaGota = new GotaMala(x, y, gotaMalaTexture);
		} else {
			nuevaGota = new GotaBuena(x, y, gotaBuenaTexture);
		}
		
		objectManager.addGameObject(nuevaGota);
		collectionManager.addCollectable((Collectable) nuevaGota);
		
		lastDropTime = TimeUtils.nanoTime();
	}
	
	/**
	 * ✅ USA GameObject (GM1.4)
	 * Crea gotas ESPECIALES (limpieza o maldición)
	 */
	private void crearGotaEspecial() {
		float x = MathUtils.random(0, 800-64);
		float y = 480;
		
		GameObject nuevaGota; // Polimorfismo
		
		// 60% limpieza, 40% maldición
		boolean esLimpieza = MathUtils.random(1, 10) <= 6;
		
		if (esLimpieza) {
			nuevaGota = new GotaLimpieza(x, y, gotaLimpiezaTexture);
			System.out.println("💚 ¡Gota de LIMPIEZA apareció!");
		} else {
			nuevaGota = new GotaMaldicion(x, y, gotaMaldicionTexture);
			System.out.println("💜 ¡Gota de MALDICIÓN apareció!");
		}
		
		objectManager.addGameObject(nuevaGota);
		collectionManager.addCollectable((Collectable) nuevaGota);
		
		lastSpecialDropTime = TimeUtils.nanoTime();
	}

	/**
	 * ✅ USA GameObject y Collectable (GM1.4 + GM1.5)
	 */
	public boolean actualizarMovimiento(Tarro tarro) {
	    float deltaTime = Gdx.graphics.getDeltaTime();

	    // Actualizar maldición
	    if (maldicionActiva) {
	        tiempoMaldicionRestante -= deltaTime;
	        if (tiempoMaldicionRestante <= 0) {
	            maldicionActiva = false;
	            probabilidadGotaMala = PROBABILIDAD_NORMAL;
	            System.out.println("✅ Maldición terminada");
	        }
	    }

	    // Generar gotas normales cada 0.2s
	    if (TimeUtils.nanoTime() - lastDropTime > INTERVALO_GOTAS) {
	        if (maldicionActiva) {
	            // 💀 Durante la maldición, se triplica la cantidad de gotas
	            for (int i = 0; i < 2; i++) {
	                crearGotaDeLluvia();
	            }
	        } else {
	            crearGotaDeLluvia();
	        }
	    }

	    // Generar gotas especiales cada 10s
	    if (TimeUtils.nanoTime() - lastSpecialDropTime > INTERVALO_ESPECIALES) {
	        crearGotaEspecial();
	    }

	    // Actualizar movimiento
	    objectManager.updateAll(deltaTime);

	    // Detección de colisiones / recolecciones
	    CollectionManager.CollectionResult result =
	            collectionManager.collectInArea(tarro.getArea());

	    if (result.getItemsCollected() > 0) {
	        Array<Collectable> itemsColectados = result.getCollectedItems();

	        for (Collectable item : itemsColectados) {
	            if (item instanceof GotaMala) {
	                tarro.dañar();
	                if (tarro.getVidas() <= 0) return false;
	            } else if (item instanceof GotaBuena) {
	                tarro.sumarPuntos(10);
	                dropSound.play();
	            } else if (item instanceof GotaLimpieza) {
	                tarro.sumarPuntos(20);
	                eliminarTodasGotasMalas();
	                dropSound.play();
	            } else if (item instanceof GotaMaldicion) {
	                activarMaldicion(5f); // 💀 activa efecto por 5s
	                dropSound.play();
	            }
	        }
	    }

	    collectionManager.removeCollected();
	    objectManager.removeOutOfBounds(0, 800, -64, 480);

	    return true;
	}

	
	/**
	 * Elimina todas las gotas malas activas
	 */
	private void eliminarTodasGotasMalas() {
		Array<GameObject> objetos = objectManager.getGameObjects();
		int eliminadas = 0;
		
		for (GameObject obj : objetos) {
			if (obj instanceof GotaMala && obj.isActive()) {
				obj.setActive(false);
				eliminadas++;
			}
		}
		
		System.out.println("💥 ¡" + eliminadas + " gotas malas eliminadas!");
	}
	
	/**
	 * Activa efecto de maldición
	 */
	private void activarMaldicion(float duracion) {
	    maldicionActiva = true;
	    tiempoMaldicionRestante = duracion;
	    probabilidadGotaMala = 100; // todas las gotas que salgan serán malas
	    System.out.println("💀 ¡MALDICIÓN ACTIVADA! Las gotas malas se triplican por " + duracion + "s");
	}



	public void actualizarDibujoLluvia(SpriteBatch batch) {
		objectManager.renderAll(batch);
	}
	
	// ===== GETTERS =====
	
	public int getCantidadGotasActivas() {
		return objectManager.getActiveCount();
	}
	
	public boolean estaMaldicionActiva() {
		return maldicionActiva;
	}
	
	public float getTiempoMaldicionRestante() {
		return tiempoMaldicionRestante;
	}
	
	public int getProbabilidadGotaMala() {
		return probabilidadGotaMala;
	}
	
	public GameObjectManager getObjectManager() {
		return objectManager;
	}
	
	public CollectionManager getCollectionManager() {
		return collectionManager;
	}
	
	// ===== MÉTODOS DE CICLO DE VIDA =====
	
	public void destruir() {
		dropSound.dispose();
		rainMusic.dispose();
		if (objectManager != null) objectManager.dispose();
		if (collectionManager != null) collectionManager.reset();
	}
	
	public void pausar() {
		rainMusic.stop();
	}
	
	public void continuar() {
		rainMusic.play();
	}
}
