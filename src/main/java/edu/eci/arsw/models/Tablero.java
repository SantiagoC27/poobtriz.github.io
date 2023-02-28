package edu.eci.arsw.models;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.eci.arsw.models.buffos.Buffo;
import edu.eci.arsw.models.rebordes.Reborde;
import edu.eci.arsw.Log;
import edu.eci.arsw.TetrisException;

public class Tablero extends Thread implements Serializable{
	public static final int filas = 20;
	public static final int cols = 10;
	private Buffo buffo;
	private int numBuffs;
	public static boolean paused = false;
	public static List<Tablero> tableros = new ArrayList<Tablero>();
	public Color[][] background = new Color[filas][cols];
	public Reborde[][] bgReborde = new Reborde[filas][cols];
	private int bloquesUsados = 0;
	private BloqueTetris block = null;
	private boolean finishGood = false; 
	private int velocidad;
	private static int disminucionVel = 100;
	private static boolean uniforme = true;

	private Color bg;
	private int Puntuacion = 0;
	private int tiempo = 0;
	public Tablero(boolean uniforme, int vel, Color c) {
		bg = c;
		Tablero.uniforme = uniforme;
		velocidad = vel;
		tableros.add(this);
		llenarMatriz();
	}

		
	public boolean getFinishGood() {
		return finishGood;
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
	 * Empieza el juego :)
	 */
	 @Override
	public void run() {
		tableros.add(this);
		setVelYBuffos();
		while(true) {
				if(paused) pausarGame();
				if(block == null) spawnBlock();
				try {
					while(moveBlockDown())
					{
						Thread.sleep(velocidad);
					}
				} catch (Exception e) { Log.registre(e);}
				if(acaboGame()) break;
		}
	}

	/**
	 * Genera un buffo aleatorio en una coordenada alearotoria
	 */

	private static void updateBuffo() {
		for(Tablero t :Tablero.tableros) {
			if(!paused) t.buffo = Buffo.selectRandomBuffo(crearCoordenada());
		}
		
	}


	/**
	 * Genera una coordenada aleatoria para el buffo
	 * @return La coordenada
	 */
	private static int[] crearCoordenada() {
		Random random = new Random();
		int[] coord = new int[2];
		int n =  random.nextInt(Tablero.filas-1);
		if(tableros.size() == 1) {
			int[] coords =tableros.get(0).getCoordenadasLibres(n);
			coord[0] = coords[random.nextInt(Tablero.cols-1)];
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
	 * Da un buffo aleatorio
	 * @return el buffo
	 */
	public Buffo getBuffo() {
		return buffo;
	}
	
	
	/**
	 * Genera un tetromino aleatorio
	 */
	public void spawnBlock() {
		block = BloqueTetris.getRandomBlock(bloquesUsados);
		if(checkFinal() == false) return;
		block.spawn(cols);

	}
	
	public void addBloquesU(int n) {
		bloquesUsados += n;
	}
	
	/**
	 * Valida si el game se debe terminar en base a la posicion del tablero
	 * @return si el game debe terminar
	 */
	public boolean isBlockOutOfBounds() {
		if(block.getY() < 0) {
			block = null;
			return true;
		}
		return false;
	}
	


	/**
	 * Baja el bloque si es posible
	 * @return si es posible bajar el bloque
	 * @throws TetrisException 
	 */
	public boolean moveBlockDown() throws TetrisException {
		finishGood = false;
		boolean haBajado = false;
		
		if(!paused) {
			if(block == null) throw new TetrisException(TetrisException.BLOCK_NULL);
			if(!paused && (checkFinal() == false || Colision(1,0) == false)) haBajado = false;		
			else {
				block.moveDown();
				validateBuffo(0,0);
				haBajado  = true;	
			}
			
		}else {
			pausarGame();
			haBajado = true;
		}
		finishGood = true;
		return haBajado;
}


	

	/**
	 * Detinene momentaneamente todo el game
	 */
	private static void pausarGame() {
		while(paused) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.registre(e);
				e.printStackTrace();
			}
		}

		
	}


	 /**
	 * Valida que el bloque tenga colision con otros bloques
	 * @return si choca con otros bloques
	 */
	private boolean Colision(int y, int x) {
		for(int[] c : block.getCoordenadas()) {
			if(background[c[1]+y][c[0]+x] != bg ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Crear el schedule para que se actualice la veolicdad, el tiempo y el buffo cada cierto tiempo
	 */
	
	private void setVelYBuffos() {
		final Timer timer = new Timer();
		TimerTask velDown = new TimerTask() {
			public void run() {
			if(tiempo % 10 == 0) {
				alterarVelocidad();
				updateBuffo();				
			}
			if(!paused) tiempo += 1;

			if(!isAlive()) {
				this.cancel();
				timer.cancel();
			}	
			}
		};
		timer.schedule(velDown,1000,1000);
	}

	/**
	 * Valida si la figura ya llego al final del tablero
	 * @return si la figura ya llego al final del tablero
	 */

	private boolean checkFinal() {
		if(block.getFinalTablero() == filas) {
			return false;
		}
		return true;	
	}

	/**
	 * Valida si la figura ya llego a la maxima poscicion de la izquierda del tablero
	 * @return si la figura ya llego a la maxima poscicion de la izquierda del tablero
	 */
	
	private boolean checkLeft() {
		if(block.getLeftEdge() <= 0) return false;
		return true;
	}
	

	/**
	 * Valida si la figura ya llego a la maxima poscicion de la derecha del tablero
	 * @return si la figura ya llego a la maxima poscicion de la derecha del tablero
	 */
	private boolean checkRight() {
		
		if(block.getRigthEdge() >= cols) return false;
		return true;
	}


	
	/**
	 * Mueve el bloque a la derecha si es posible
	 */
	public void moveBlockRight() {
		finishGood = false;
		if(!paused ) {
			if(!checkRight() || Colision(0,1) == false) return;
			
			validateBuffo(1,0);
			block.moveRight();
			finishGood = true;
		}

		
	}
	
	/**
	 * Mueve el bloque a la izquierda si es posible
	 */
	public void moveBlockLeft() {
		finishGood = false;
		if(!paused) {
			if(!checkLeft() || Colision(0,-1) == false) return;
			validateBuffo(-1,0);
			block.moveLeft();
			finishGood = true;
		}

		
		
	}
	
	/**
	 * Valida si una linea es borrable o no, y en caso de que si la borra
	 */
	public int clearLines() {
			finishGood = false;
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
			finishGood = true;
			return linesCleared;

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
	public void moveBlockToBackground() {
			for(int[] co :block.getCoordenadas()) {
				if(co[1] < Tablero.filas && co[0]< Tablero.cols) {
					background[co[1]][co[0]] = block.getColor();
					bgReborde[co[1]][co[0]] = block.getReborde();
				}
			}
			bloquesUsados++;			
			
	}



	/**
	 * Se encarga de rotar la ficha 
	 */
	public void rotarBlock() {
		finishGood = false;
		if(!paused) {
			if(isRotable()) {
				block.rotar();
				validateBuffo(0,0);				
			}
			
		}
		finishGood = true;
	} 
	/**
	 * Valida que el tetromino se pueda rotar
	 * @return si es rotable 
	 */

	private boolean isRotable() {
		// Traer la siguiente rotacion
		// ver si no hay cuadros ocupados en el tablero
		BloqueTetris b = block.Clone();
		b.rotar();
		int oldPos = b.getY();
		for(int[] c :b.getCoordenadas()) {
			
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
	private boolean acaboGame() {
		boolean ok = false;
		try {
			if(block.modifyShape()) block.findIdealForm(block.traducir(background, bg), background, bg);
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
	 * @param acelerado indica si el modod de juego es acelerado  o no
	 */

	private void alterarVelocidad() {
		if(tiempo != 0 && !uniforme && velocidad - disminucionVel > 0) {
			velocidad -= disminucionVel;
		}
	}


	 /**
	 * Termina el juego
	 * @return si se termino el juego
	 */
	public boolean finGame() {	
		return isBlockOutOfBounds();
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

	public void setVelocidad(int v) {
		velocidad = v;
	}

	
	public int getVelocidad() {		
		return velocidad;
	}


	/**
	 * Pausa el juego
	 */
	public static void pausar() {
			paused = true;
	}

	 /**
	 * Despausa el juego
	 */
	public static void despausar() {
		paused = false;	
}
	public static boolean isUniforme() {
		return uniforme;
	}

	
	public void setMovilidadBlock(boolean p) {
		if(block != null) block.setMovilidad(p);
		
	}
	
	public Color getBg() {
		return bg;
	}
	
	public int getPuntuacionBloques() {
		return Puntuacion;
	}

	public BloqueTetris getBlock(){
		return block;
	}
	
	public int getTiempo() {
		return (int) tiempo;
	}


	 /**
	 * Genera una lista de buffos a usar en base al numero de buffos indicado
	 * @param numero de buffos
	 */
	public static void prepareBuffos(int buffs) {
		for(Tablero t :tableros) {
			t.numBuffs = buffs;			
		}
		
		Buffo.prepareBuffos(buffs);
		
	}
	
	public int  getNumBuffs() {
		return numBuffs;
	}
	
	/*
	 * Valida que el bloque cabe el bloque cabe en el tablero dada su posicion
	 */

	public boolean isValidPosition(BloqueTetris bloq) {
		for(int[] c :bloq.getCoordenadas()) {
			try{
				if(c[1] ==  bloq.getY() + bloq.getHeight()-2 && c[1]+1 < Tablero.filas && c[0]+1 < Tablero.cols && quedaraHueco(c, bloq) ) return false;
				if(background[c[1]][c[0]] != bg) return false;					
			}catch (Exception e) {return false;}
		}
		return true;
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
}

