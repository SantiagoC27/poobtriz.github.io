package edu.eci.arsw.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import edu.eci.arsw.models.buffos.Buffo;
import edu.eci.arsw.models.buffos.CommonBuffo;
import edu.eci.arsw.models.rebordes.Reborde;
import edu.eci.arsw.shared.TetrisException;
import lombok.Getter;


@Getter
public class Tablero implements Serializable{

	private final int Id;
	private final CommonBuffo buffo;
	private final List<Tablero> tableros;
	private final List<BloqueTetris> bloques;

	private final AtomicInteger sumPuntuacion = new AtomicInteger(10);

	public String[][] background;
	public Reborde[][] bgReborde;
	private int bloquesUsados = 0;
	private BloqueTetris block = null;
	private BloqueTetris nextPiece = null;
	private int velocidad;

	private static final int disminucionVel = 100;
	private static boolean uniforme = true;

	private final String bg;
	private int puntuacion = 0;

	private boolean finGame = false;
	public Tablero(boolean uniforme, int vel, String bg, int filas, int cols, List<BloqueTetris> bloques,
				   CommonBuffo b, List<Tablero> tableros) {
		this.buffo = b;
		this.bloques = bloques;
		this.bg = bg;
		Tablero.uniforme = uniforme;
		velocidad = vel;
		this.background = new String[filas][cols];
		this.bgReborde =  new Reborde[filas][cols];
		this.tableros = tableros;
		this.Id = tableros.size();
		llenarMatriz();
	}

	public Tablero(){
		buffo = new CommonBuffo();
		this.bloques = new ArrayList<>();
		this.bg = "#F0F8FF";
		Tablero.uniforme = true;
		velocidad = 1000;
		this.background = new String[15][10];
		this.bgReborde =  new Reborde[15][10];
		this.tableros = new ArrayList<>();
		this.Id = 0;
		llenarMatriz();
	}

	/**
	 * Llena con el color del tablero todas las casillas
	 */
	private void llenarMatriz() {
		for(int r = 0; r < background.length; r++) {
			for (int c = 0; c < background[0].length; c++) {
				background[r][c] = bg;
			}
		}
	}
	
	
	/**
	 * Genera 2 tetrominos aleatorio con color distinto al del fondo del tablero.
	 */
	public void spawnBlock() {
		if(bloquesUsados +1 >= bloques.size()) {
			bloques.add(BloqueTetris.selectRandomBlock(bg, null));
			bloques.add(BloqueTetris.selectRandomBlock(bg, null));
		}
		block = bloques.get(bloquesUsados).Clone();
		nextPiece = bloques.get(bloquesUsados+1).Clone();
		nextPiece.setPos(0,0);
		block.spawn(background[0].length);
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
		BloqueTetris b = BloqueTetris.selectRandomBlock(bg, null);
		b.spawn(background[0].length);
		if(Colision(1, 0, b) && (block == null || block.getY() < 0)) {
			finGame = true;
		}

	}


	 /**
	 * Valida que el bloque tenga colision con otros bloques
	 * @return si choca con otros bloques
	 */
	public boolean Colision(int y, int x, BloqueTetris b) {
		int[][] coords = b.getCoordenadas();
		boolean colision = false;
		for(int[] c : coords) {
			if (c[1] + y < 0) continue;
			// Cuando las siguientes casillas son de otro color, y ese otro color no hace parte del bloque
			if(c[1] + y >= background.length ||
					(!Objects.equals(background[c[1] + y][c[0] + x], bg) &&
					Arrays.stream(coords).noneMatch(co -> co[0] == c[0] + x && co[1] == c[1] + y))) {
				colision = true;
				break;
			}
		}
		return colision;
	}
	
	/**
	 * Mueve el bloque a la derecha si es posible
	 *
	 */
	private boolean moveBlockRight(){
		if(block.getX() + block.getWidth() < background[0].length && !Colision(0,1, this.block)){
			block.moveRight();
			return true;
		}
		return false;

	}

	/**
	 * Mueve el bloque y recalcula la puntuación
	 * @param movement Dirección del movimiento. ['UP', 'DOWN', 'LEFT', 'RIGHT']
	 * @return Si se movió el bloque o no
	 * @throws TetrisException si el bloque es nulo
	 */
	public synchronized boolean moveBlock(String movement) throws TetrisException{
		boolean moved = false;
		if (!finGame){
			if (block == null) spawnBlock();
			removeBlockFromBackground();
			switch (movement.toUpperCase()){
				case "DOWN":
					moved = moveBlockDown();
					if (!moved) {
						moveBlockToBackground();
						if(block.borrarCercanos()) borrarCercanos();
						else if(block.modifyShape()) setBlockToIdealForm();
						block = null;
						calculatePuntuacion();
						bloquesUsados++;
						calculateFinGame();
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
			validateBuffo();
			moveBlockToBackground();
		}

		return moved;
	}

	private void borrarCercanos(){
		removeBlockFromBackground();
		updateCuadrosLaterales();
	}
	
	/**
	 * Mueve el bloque a la izquierda si es posible
	 */
	private boolean moveBlockLeft() {
		if(block.getX() -1 >= 0 && !Colision(0,-1, this.block)){
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
			for(int r = background.length - 1; r >= 0; r--) {
				lineFilled = true;
				for(int c = 0; c < background[0].length; c++) {
					if(Objects.equals(background[r][c], bg)) {
						lineFilled = false;
						break;
					}
				}
				if(lineFilled && isBorrable(r)) {
					linesCleared++;
					clearLine(r,background[0].length);
					shiftDown(r);
					r++;
				}
			}
			return linesCleared;

	}

	/**
	 * Limpia las lineas del tablero, y en base a esto agrega una puntuación.
	 */
	public void calculatePuntuacion(){
		puntuacion += this.clearLines()*sumPuntuacion.get();
	}

	/** 
	 * Borra una linea del tablero
	 * @param r es el numero de la fila
	*/
	private void clearLine(int r, int fin) {
		for(int i = 0; i < fin; i++) {
			background[r][i] = bg;
			bgReborde[r][i] = null;
		}	
	}
	/** 
	 * Baja todas las filas que esten sobre la indicada
	*/
	private void shiftDown(int r) {
		for(int fila = r; fila > 0; fila--) {
			for(int col = 0; col < background[0].length; col++) {
				background[fila][col]=background[fila - 1][col];
				bgReborde[fila][col]=bgReborde[fila - 1][col];
			}
		}
	}
	/**
	 * Pasa los colores del tetromino al tablero
	*/
	private void moveBlockToBackground() {
			if (block != null){
				for(int[] co :block.getCoordenadas()) {
					if(co[1] < 0) continue;
					if(co[1] < background.length && co[0]< background[0].length) {
						background[co[1]][co[0]] = block.getColor();
						bgReborde[co[1]][co[0]] = block.getReborde();
					}
				}
			}

	}

	/**
	 * Remueve los colores y rebordes del bloque del tablero
	 */
	private void removeBlockFromBackground() {
		for(int[] co :block.getCoordenadas()) {
			if (co[1]  < 0) continue;
			if(co[1] < background.length && co[0]< background[0].length) {
				background[co[1]][co[0]] = bg;
				bgReborde[co[1]][co[0]] = null;
			}
		}
	}

	/**
	 * Se encarga de rotar la ficha 
	 */
	public boolean rotarBlock() {
		if(isRotable()) {
			block.rotar();
			return true;
		}
		return false;
	} 
	/**
	 * Valida que el tetromino se pueda rotar simulando la rotación. el bloque debe ser removido del background
	 * @return si es rotable
	 */

	private boolean isRotable() {
		BloqueTetris b = block.Clone();
		b.rotar();
		for(int[] c :b.getCoordenadas()) {
			if (c[1] < 0 || c[1] >= background.length || c[0] < 0 || c[0] >= background[0].length
			|| !Objects.equals(background[c[1]][c[0]], bg)) return false;
		}

		return true;
	}



	/**
	 * Valida si la el reborde permite borrar la linea fil
	 */
	private boolean isBorrable(int fil) {
		boolean borrable = true; 
		for(int i = 0; i < background[0].length; i++) {
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
			if (coord[1] < background.length && coord[1] >= 0 &&
					coord[0] < background[0].length && coord[0] >= 0 ){
				background[coord[1]][coord[0]] = bg;
				bgReborde[coord[1]][coord[0]] = null;
			}
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
		int[] nums = new int[background[0].length];
		int pos = 0;
		for(int j = 0; j < background[0].length; j++)
			if(Objects.equals(background[n][j], bg)) {
				nums[pos] = j;
				pos++;
			}
		return nums;
	}

	 /**
	 * Si el bloque está sobre la posición, lo activa
	 */
	public void validateBuffo() {
		if(buffo.get() != null && block != null) {
			synchronized (buffo.get()){
				for(int[] c :block.getCoordenadas()) {
					if(c[1] == buffo.getY() && c[0] == buffo.getX()) {
						System.out.println(this.Id + " activa el buffo");
						buffo.activate(tableros, this.Id);
						buffo.set(null);
						break;
					}
				}
		}
		}
	}
	public void setMovilidadBlock(boolean p) {
		if(block != null) block.setMobibleDown(p);
	}


	public int[] getPositionBlock(){
		int[] position = null;
		if (block != null)
			position = new int[]{block.getX(), block.getY()};
		return position;
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

	public Buffo getBuffo(){
		return buffo.get();
	}

	public void setBuffo(Buffo b){
		this.buffo.set(b);
	}

	/*
	 * Traduce una matriz de colores a una matriz de ceros y unos
	 * 1 si ahi puede caber una ficha, 0 si no.
	 */

	public int[][] toBinaryMatrix() {
		int[][] rta = new int[5][];
		int cont = 0;
		for(int i = block.getY(); i < background.length && i < block.getY() + 4; i++) {
			int[] aux = new int[background[0].length-block.getX()];
			for(int j =block.getX(); j < background[0].length && j < block.getX() + 4; j++) {
				if(i >= 0 && Objects.equals(background[i][j], bg))
					aux[j-block.getX()] = 1;  else aux[j-block.getX()] = 0;
			}
			rta[cont] = aux;
			cont++;
		}
		return rta;
	}

	public void setBlockToIdealForm() {
		removeBlockFromBackground();
		int[][] data = toBinaryMatrix();
		int indx = 0;
		double[][] dataForms = new double[BloqueTetris.formas.length*4][3];

		for(int i = 0; i < BloqueTetris.formas.length; i++) {
			BloqueTetris b = new BloqueTetris(BloqueTetris.formas[i], null, BloqueTetris.colores[i], i);
			for(int j = 0; j < b.getRotaciones().length; j++) {
				double[] aux = {i, j, puntuacion(data, b.getRotation(j))};
				dataForms[indx] = aux;
				indx++;
			}
		}
		calculateBestRotacion(dataForms);
		while(moveBlockDown()) continue;
		moveBlockToBackground();
	}

	private void calculateBestRotacion(double[][] dataForms) {
		int form = 0,rot = 0;
		double max = 0;
		for (double[] dataForm : dataForms) {
			if (dataForm[2] > max) {
				max = dataForm[2];
				form = (int) dataForm[0];
				rot = (int) dataForm[1];
			}
		}
		block.changeForm(form, rot);
	}

	private double puntuacion(int[][] data, int[][] rot) {
		int pos = 0;
		double[] puntuaciones = new double[100];
		for(int fil = rot.length-1; fil > 0;fil--) {
			for(int fil2 = data.length-1; fil2 >= 0; fil2--) {
				if(data[fil2] != null) {
					for(int X = 0; X <= data[fil2].length - rot[fil].length; X++)
						if(isValid(rot[fil], Arrays.copyOfRange(data[fil2],X,X+rot[fil].length))) {
							puntuaciones[pos] = calcularPuntuacion(rot, data, X, fil2);
							pos++;
						}
				}
			}

		}
		DoubleSummaryStatistics stat = Arrays.stream(puntuaciones).summaryStatistics();
		return stat.getMax();
	}

	/*
	 * Retorna si los arreglos son validos
	 */
	private boolean isValid(int[] arr, int[] arr2) {
		for(int i = 0; i < arr.length; i++) if((arr[i] == 0 || arr2[i] == 0) && arr[i] != arr2[i]) return false;
		return true;
	}

	/*
	 * Calcula Una puntuacion que determinara que tan buena es la rotacion
	 */
	private int calcularPuntuacion(int[][] rot, int[][] data, int col, int fil) {//================ ARREGLAR==============================
		//Retornar un porcentaje de parecido, la que tenga un mayor porcentaje de parecido es la indicada
		int puntuacion = 0;
		for(int i = rot.length-1; i >= 0; i--) {
			if(fil >= 0 && col+rot[i].length <= rot[i].length)
			{
				puntuacion += areEquals(rot[i], Arrays.copyOfRange(data[fil], col, col+rot[i].length), fil+1);
			}
			fil--;
		}
		return puntuacion;
	}

	private int areEquals(int[] arr, int[] arr2, int suma) {
		int cont = 0;
		for(int i = 0; i < arr.length; i++) if(arr[i] == arr2[i] && arr[i] == 1) cont += suma;
		return cont;
	}




	@Override
	public String toString() {
		StringBuilder sRta = new StringBuilder();
		sRta.append("{\"background\": [");

		for (int j = 0; j < background.length; j++) {
			sRta.append(String.format("[%s]", String.join(", ",
					Arrays.stream(background[j]).map(s -> String.format("\"%s\"", s)).toArray(String[]::new))));
			if (j != background.length -1) sRta.append(",");
		}
		sRta.append("]");
		sRta.append(",\"bgReborde\": [");
		for (int i = 0; i < bgReborde.length; i++) {
			sRta.append(String.format("[%s]", String.join(", ",
					Arrays.stream(bgReborde[i]).map(r -> r== null ? null: r.toString()).toArray(String[]::new))));
			if (i != bgReborde.length -1) sRta.append(",");
		}
		sRta.append("]");
		if (buffo.get() != null) sRta.append(", \"buffo\": ").append(buffo.get().toString());
		if (nextPiece != null) sRta.append(", \"nextPiece\": ").append(nextPiece.toString());
		sRta.append(String.format(",\"puntuacion\": %d", puntuacion));
		sRta.append("}");
		return sRta.toString();

	}
}

