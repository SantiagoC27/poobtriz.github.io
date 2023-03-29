package edu.eci.arsw.models;

import java.io.Serializable;
import java.util.*;

import com.google.gson.annotations.Expose;
import edu.eci.arsw.models.buffos.Buffo;
import edu.eci.arsw.models.rebordes.Reborde;
import edu.eci.arsw.shared.Log;
import edu.eci.arsw.shared.TetrisException;
import lombok.Getter;


@Getter
public class Tablero implements Serializable{


	private final int filas;
	private final int cols;
	private Buffo buffo;
	private int numBuffs;
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
	private int Puntuacion = 0;
	private int tiempo = 0;

	private boolean finGame = false;
	public Tablero(boolean uniforme, int vel, String bg, int filas, int cols, List<BloqueTetris> bloques) {
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
	 * Llena con el color de ltablero todas las casillas
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

	private void updateBuffo() {
		for(Tablero t :Tablero.tableros) {
			t.buffo = Buffo.selectRandomBuffo(crearCoordenada());
		}
		
	}


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
	
	public void addBloquesU(int n) {
		bloquesUsados += n;
	}
	



	/**
	 * Baja el bloque si es posible
	 * @return si es posible bajar el bloque
	 */
	public boolean moveBlockDown(){
		boolean haBajado = true;
		boolean wasNull = false;
		if(block == null) {
			spawnBlock();
			wasNull = true;
		}
		if(isFinalTablero() || Colision(1,0)){
			haBajado = false;
			block = null;
			if (wasNull) finGame = true;
		}
		else {
			removeBlockFromBackground();
			block.moveDown();
			moveBlockToBackground();
			validateBuffo(0,0);
		}
		return haBajado;
	}

	 /**
	 * Valida que el bloque tenga colision con otros bloques
	 * @return si choca con otros bloques
	 */
	private boolean Colision(int y, int x) {
		int[][] coords = block.getCoordenadas();
		for(int[] c : coords) {

			if (c[1] + y < 0) continue;
			// Cuando las siguientes casillas son de otro color, y ese otro color no hace parte del bloque
			if(!Objects.equals(background[c[1] + y][c[0] + x], bg) &&
					Arrays.stream(coords).noneMatch(co -> co[0] == c[0] + x && co[1] == c[1] + y))
				return true;
		}
		return false;
	}

	/**
	 * Crear el schedule para que se actualice la velocidad, el tiempo y el buffo cada cierto tiempo
	 */
	public void setVelYBuffos() {
		final Timer timer = new Timer();
		TimerTask velDown = new TimerTask() {
			public void run() {
			if(tiempo % 10 == 0) {
				alterarVelocidad();
				updateBuffo();				
			}
			}
		};
		timer.schedule(velDown,1000,1000);
	}

	/**
	 * Valida si la figura ya llego al final del tablero
	 * @return si la figura ya llego al final del tablero
	 */
	private boolean isFinalTablero() {
		return block.getY() + block.getHeight() == filas;
	}
	
	/**
	 * Mueve el bloque a la derecha si es posible
	 *
	 */
	public boolean moveBlockRight() throws TetrisException{
		if (block == null) throw new TetrisException(TetrisException.BLOCK_NULL);
		if(block.getX() + block.getWidth() < cols && !Colision(0,1)){
			validateBuffo(1,0);
			block.moveRight();
			return true;
		}
		return false;

	}
	
	/**
	 * Mueve el bloque a la izquierda si es posible
	 *
	 */
	public boolean moveBlockLeft() throws TetrisException {
		if (block == null) throw new TetrisException(TetrisException.BLOCK_NULL);
			if(block.getX() -1 >= 0 && !Colision(0,-1)){
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
					if(background[r][c] == bg) {
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
	public void rotarBlock() throws TetrisException {
		if (block == null) throw new TetrisException(TetrisException.BLOCK_NULL);
			if(isRotable()) {
				block.rotar(this);
				validateBuffo(0,0);				
			}
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
			if(oldPos == b.getY() && background[c[1]][c[0]] != bg) return false;
			if(oldPos != b.getY() && background[c[1]+1][c[0]] != bg) return false;			
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
			}catch(Exception e) {
				continue;
			}
		}							
	}



	 /**
	 * Realiza las operaciones pertinentes leugo de que cae el tetromino
	 * @return si se termino el juego
	 */
	public boolean acaboGame() {
		boolean ok = false;
		try {
			if(block.modifyShape()) block.findIdealForm(block.traducir(this, bg), this, bg);
			moveBlockToBackground();
			if(block.borrarCercanos()) updateCuadrosLaterales();
		}catch(Exception e) {ok = true;  Log.registre(e);}
		Puntuacion += clearLines();	
		block = null;
		return ok;
	}
	
	
	public void addPuntuacion(int c) {
		Puntuacion += c; 
	}
	
	/** Modifica la velocidad de caida de los bloques si el modo de juego es acelerado
	 *
	 */

	private void alterarVelocidad() {
		if(tiempo != 0 && !uniforme && velocidad - disminucionVel > 0) {
			velocidad -= disminucionVel;
		}
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
			if(background[n][j] == bg) {
				nums[pos] = j;
				pos++;
			}
		return nums;
	}

 /**
 * Valida que el buffo se active en la posicion indicada
 */

	public void validateBuffo(int x, int y) {
		// Ver la posicion del buffo
		if(buffo != null) {
			//get alturas, retorna la altura de cada 
			for(int[] c :block.getCoordenadas()) {
				if(c[1]+y == buffo.getY() && c[0] == buffo.getX()+x) {
					buffo.activate(this);
					buffo = null;
					break;
				}
			}			
		}
	}


	public static boolean isUniforme() {
		return uniforme;
	}

	
	public void setMovilidadBlock(boolean p) {
		if(block != null) block.setMovilidad(p);
	}
	
	public int getPuntuacionBloques() {
		return Puntuacion;
	}

	public int[] getPositionBlock(){
		return new int[]{block.getX(), block.getY()};
	}

	public int getTiempo() {
		return (int) tiempo;
	}


	 /**
	 * Genera una lista de buffos a usar en base al numero de buffos indicado
	 * @param buffs numero de buffos
	 */
	public static void prepareBuffos(int buffs) {
		for(Tablero t :tableros) {
			t.numBuffs = buffs;			
		}
		Buffo.prepareBuffos(buffs);
		
	}

	/*
	 * Validar que no queden huecos debajo de la penultima fila
	 */
	private boolean quedaraHueco(int[] c, BloqueTetris bloq) {
		if(background[c[1]+1][c[0]+1] != bg && background[c[1]+1][c[0]] == bg) {
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

