package edu.eci.arsw.models;

import java.io.Serializable;
import java.util.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.annotations.Expose;
import edu.eci.arsw.models.buffos.Buffo;
import edu.eci.arsw.models.rebordes.Reborde;
import edu.eci.arsw.shared.TetrisException;
import lombok.Getter;


@Getter
public class Tablero implements Serializable{


	private final int filas;
	private final int cols;
	private ConcurrentLinkedQueue<Buffo> buffos;
	public static List<Tablero> tableros = new ArrayList<>();

	private final List<BloqueTetris> bloques;
	@Expose
	public String[][] background;
	@Expose
	public Reborde[][] bgReborde;
	private int bloquesUsados = 0;
	private BloqueTetris block = null;
	private int velocidad;
	private static final int disminucionVel = 100;
	private static boolean uniforme = true;

	private final String bg;
	private final AtomicInteger puntuacion = new AtomicInteger(0);
	private int tiempo = 0;

	private boolean finGame = false;
	public Tablero(boolean uniforme, int vel, String bg, int filas, int cols, List<BloqueTetris> bloques,
				   ConcurrentLinkedQueue<Buffo> buffos) {
		this.bloques = bloques;
		this.filas = filas;
		this.cols = cols;
		this.bg = bg;
		Tablero.uniforme = uniforme;
		velocidad = vel;
		this.background = new String[filas][cols];
		this.bgReborde =  new Reborde[filas][cols];
		tableros.add(this);
		llenarMatriz();
	}

	/**
	 * Llena con el color del tablero todas las casillas
	 */
	private void llenarMatriz() {
		for(int r = 0; r < filas; r++) {
			for(int c = 0; c < cols; c++) {
				background[r][c] = bg;
				}
			}
		
	}

	/**
	 * Genera un buffo aleatorio en una coordenada alearotoria
	 */




	/**
	 * Genera una coordenada aleatoria para el buffo
	 * @return La coordenada
	 */
	private int[] crearCoordenada() {
		Random random = new Random();
		int[] coord = new int[2];
		int n =  random.nextInt(filas-1);
		if(tableros.size() == 1) {
			int[] coords =tableros.get(0).getCoordenadasLibres(n);
			coord[0] = coords[random.nextInt(cols-1)];
			coord[1] = n;
		}else {
			try {
				for(int c :tableros.get(0).getCoordenadasLibres(n)) {
					for(int c2 :tableros.get(1).getCoordenadasLibres(n)) {
						if(c == c2) {
							coord[0] = c;
							coord[1] = n;
						}
					}
				}				
			}catch(Exception e) {return null;}

		}
		return coord;
	}

	
	
	/**
	 * Genera un tetromino aleatorio con color distinto al del fondo del tablero.
	 */
	public void spawnBlock() {
		bloquesUsados++;
		if(bloquesUsados > bloques.size()) bloques.add(BloqueTetris.getRandomBlock(bloquesUsados, bg));
		block = bloques.get(bloquesUsados - 1);
		block.spawn(cols);
	}


	/**
	 * Baja el bloque si es posible
	 * @return si es posible bajar el bloque
	 */
	private boolean moveBlockDown(){
		boolean haBajado = false;
		if (!Colision(1, 0, block)){
			haBajado = true;
			block.moveDown();
		}

		return haBajado;
	}

	/**
	 * Actualiza la variable finGame. La logica es que si no hay un bloque, y si se genera otro que no puede bajar,
	 * finGame = true
	 */

	private void calculateFinGame(){
		BloqueTetris b = BloqueTetris.getRandomBlock(bloquesUsados, bg);
		if(block == null && Colision(1, 0, b)) finGame = true;
	}


	 /**
	 * Valida que el bloque tenga colision con otros bloques
	 * @return si choca con otros bloques
	 */
	public boolean Colision(int y, int x, BloqueTetris b) {
		int[][] coords = b.getCoordenadas();
		for(int[] c : coords) {

			if (c[1] + y < 0) continue;
			// Cuando las siguientes casillas son de otro color, y ese otro color no hace parte del bloque
			if(c[1] + y >= background.length ||
					(!Objects.equals(background[c[1] + y][c[0] + x], bg) &&
					Arrays.stream(coords).noneMatch(co -> co[0] == c[0] + x && co[1] == c[1] + y)))
				return true;
		}
		return false;
	}
	
	/**
	 * Mueve el bloque a la derecha si es posible
	 *
	 */
	private boolean moveBlockRight() throws TetrisException{
		if (block == null) throw new TetrisException(TetrisException.BLOCK_NULL);
		if(block.getX() + block.getWidth() < cols && !Colision(0,1, this.block)){
			validateBuffo(1,0);
			block.moveRight();
			return true;
		}
		return false;

	}

	/**
	 * Mueve el bloque y recalcula la puntuación
	 * @param movement dirección del movimiento. ['UP', 'DOWN', 'LEFT', 'RIGHT']
	 * @return Si se movió el bloque o no
	 * @throws TetrisException si el bloque es nulo
	 */
	public synchronized boolean moveBlock(String movement) throws TetrisException{
		// Si no ha acabado el juego, ver si se debe instanciar un bloque
		boolean moved = false;
		if (!finGame){
			if (block == null) spawnBlock();
			removeBlockFromBackground();
			switch (movement.toUpperCase()){
				case "DOWN":
					moved = moveBlockDown();
					if (!moved){
						moveBlockToBackground();
						block = null;
					}
					break;
				case "UP":
					moved = rotarBlock();
					break;
				case "LEFT":
					moved = moveBlockLeft();
					break;
				case "RIGHT":
					moved = moveBlockRight();
					break;
			}
			if (moved) moveBlockToBackground();
			else calculatePuntuacion();
			calculateFinGame();
		}

		return moved;
	}
	
	/**
	 * Mueve el bloque a la izquierda si es posible
	 */
	private boolean moveBlockLeft() throws TetrisException {
		if (block == null) throw new TetrisException(TetrisException.BLOCK_NULL);
		if(block.getX() -1 >= 0 && !Colision(0,-1, this.block)){
			validateBuffo(-1,0);
			block.moveLeft();
			return true;
		}
		return false;

	}
	
	/**
	 * Valida si una linea es borrable o no, y en caso de que si la borra
	 * return total de lineas borradas
	 */
	public int clearLines() {
			boolean lineFilled;
			int linesCleared = 0;
			for(int r = filas - 1; r >= 0; r--) {
				lineFilled = true;
				for(int c = 0; c < cols; c++) {
					if(Objects.equals(background[r][c], bg)) {
						lineFilled = false;
						break;
					}
				}
				if(lineFilled && isBorrable(r)) {
					linesCleared++;
					clearLine(r,0,cols);
					shiftDown(r);
					clearLine(0,0,cols);
					r++;
				}
			}
			return linesCleared;

	}

	/**
	 * Limpia las lineas del tablero, y en base a esto agrega una puntuación.
	 */
	public void calculatePuntuacion(){
		int linesCleared = this.clearLines();
		this.addPuntuacion(linesCleared*10);
	}

	/** 
	 * Borra una linea del tablero
	 * @param r es el numero de la fila
	*/
	private void clearLine(int r, int ini, int fin) {
		for(int i = ini; i < fin; i++) {
			background[r][i] = bg;
			bgReborde[r][i] = null;
		}	
	}
	/** 
	 * Baja todas las filas que esten sobre la indicada
	*/
	private void shiftDown(int r) {
		for(int fila = r; fila > 0; fila--) {
			for(int col = 0; col < cols; col++) {
				background[fila][col]=background[fila - 1][col];
				bgReborde[fila][col]=bgReborde[fila - 1][col];
			}
		}
	}
	/**
	 * Pasa los colores del tetromino al tablero
	*/
	private void moveBlockToBackground() {
			for(int[] co :block.getCoordenadas()) {
				if(co[1] < 0) continue;
				if(co[1] < filas && co[0]< cols) {
					background[co[1]][co[0]] = block.getColor();
					bgReborde[co[1]][co[0]] = block.getReborde();
				}
			}
	}

	/**
	 * Remueve los colores y rebordes del bloque del tablero
	 */
	private void removeBlockFromBackground() {
		for(int[] co :block.getCoordenadas()) {
			if (co[1]  < 0) continue;
			if(co[1] < filas && co[0]< cols) {
				background[co[1]][co[0]] = bg;
				bgReborde[co[1]][co[0]] = null;
			}
		}
	}

	/**
	 * Se encarga de rotar la ficha 
	 */
	public boolean rotarBlock() throws TetrisException {

		if (block == null) throw new TetrisException(TetrisException.BLOCK_NULL);
		if(isRotable()) {
			block.rotar(this);
			validateBuffo(0,0);
			return true;
		}
		return false;
	} 
	/**
	 * Valida que el tetromino se pueda rotar
	 * @return si es rotable 
	 */

	private boolean isRotable() {
		// Traer la siguiente rotacion
		// ver si no hay cuadros ocupados en el tablero
		BloqueTetris b = block.Clone();
		b.rotar(this);
		int oldPos = b.getY();
		for(int[] c :b.getCoordenadas()) {
			if (c[1] < 0) continue;
			if(oldPos == b.getY() && !Objects.equals(background[c[1]][c[0]], bg)) return false;
			if(oldPos != b.getY() && !Objects.equals(background[c[1] + 1][c[0]], bg)) return false;
		}
	
		return true;
	}



	/**
	 * Valida si la el reborde permite borrar la linea donde esta
	 */

	private boolean isBorrable(int fil) {
		boolean borrable = true; 
		for(int i = 0; i < cols; i++) {
			if(bgReborde[fil][i] != null && !bgReborde[fil][i].isBorrable()) {
				borrable = false;
				break;
			}
		}
		return borrable;
	}


	/**
	 * Borra los cuadros cercanos al bloque, incluyendo a este
	 */
	private void updateCuadrosLaterales(){
		for(int[] coord :block.getCoordenadasCercanas()){
			try {
				background[coord[1]][coord[0]] = bg;
				bgReborde[coord[1]][coord[0]] = null;
			}catch(Exception ignored) {}
		}							
	}
	
	public void addPuntuacion(int linesCleared) {
		puntuacion.set(puntuacion.get() + (linesCleared*10));
	}


	 /**
	 * Valida si el juego terminó
	 */
	public boolean hasFinished() {
		return finGame;

	}


	 /**
	 * Retorna las espacios no ocupados en el tablero en la fila indicada
	 * @return Los espacios libres
	 */
	public int[] getCoordenadasLibres(int n) {
		int[] nums = new int[cols];
		int pos = 0;
		for(int j = 0; j < cols; j++)
			if(Objects.equals(background[n][j], bg)) {
				nums[pos] = j;
				pos++;
			}
		return nums;
	}

	 /**
	 * Valida que el buffo se active en la posicion indicada
	 */

	public void validateBuffo(int x, int y) {
		Buffo buffo = buffos.peek();
		if(buffo != null) {
		 			//get alturas, retorna la altura de cada
		 			for(int[] c :block.getCoordenadas()) {
		 				if(c[1]+y == buffo.getY() && c[0] == buffo.getX()+x) {
							 buffo.activate(this);
							 buffos.poll();
							 break;

						 }
					 }
		 		}

	}
	public void setMovilidadBlock(boolean p) {
		if(block != null) block.setMovilidad(p);
	}
	
	public int getPuntuacionBloques() {
		return puntuacion.get();
	}

	public int[] getPositionBlock(){
		return new int[]{block.getX(), block.getY()};
	}

	public int getTiempo() {
		return tiempo;
	}

	/*
	 * Validar que no queden huecos debajo de la penultima fila
	 */
	private boolean quedaraHueco(int[] c, BloqueTetris bloq) {
		if(!Objects.equals(background[c[1] + 1][c[0] + 1], bg) && Objects.equals(background[c[1] + 1][c[0]], bg)) {
			for(int[] co :bloq.getCoordenadas()) {
				if(co[0] == c[0] && co[1] == c[1]+1) return false;
			}
			return true;	
		}
		else return false;
		
	}

	public void setVelocidad(int v) {
		velocidad = v;
	}
}

