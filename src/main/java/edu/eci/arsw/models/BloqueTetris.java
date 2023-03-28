package edu.eci.arsw.models;

import java.io.Serializable;
import java.util.*;

import edu.eci.arsw.shared.Log;
import edu.eci.arsw.models.rebordes.Reborde;
import lombok.Getter;

@Getter
public class BloqueTetris implements Cloneable, Serializable {
	//C, L, LINE, T, Z
	public static int[][][] formas ={{{1,1}, {1,1}}, {{1,0}, {1,0}, {1,1}}, {{1,1,1,1}},
									{{1,1,1}, {0,1,0}}, {{1,1,0}, {0,1,1}} };
	public static String[] colores= {"yellow", "blue", "pink", "orange", "indigo"};
	private final static List<BloqueTetris> bloquesShared = new ArrayList<>();
	private int[][] shape;
	
	private final String color;
	private int x, y;
	protected int[][][] rotaciones;
	private int currentRotation;
	private final Reborde reborde;
	private boolean isMovible = true;
	private final int currentForm;
	
	
	public BloqueTetris(int[][] shape, Reborde r, String c, int cu) {
		this.reborde = r;
		this.shape = shape;
		this.color = c;
		this.currentForm = cu;
		crearRotaciones();
	}


	public int getForm() {
		return currentForm;
	}

	/**
	 * Se encarga de hacer apareces los bloques por encima del tablero y en el centro
	 * @param girdWidth anchura del tablero
	 */
	public void spawn(int girdWidth) {
		y = -getHeight();
		x = girdWidth / 2;
	}

	
	public void moveDown() {
		if(isMovible ) y++;
	}
	
	public void moveLeft() {
		x--;
	}
	
	public void moveRight() {
		x++;
	}
	
	public void setPos(int X,int Y) {
		x = X;
		y = Y;
	}
	
	public int getCurrentRot() {
		 if(currentRotation == 0) return 3;
		return currentRotation-1;
	}
	
	public int getNextRot() {
		 if(currentRotation > 3) return 0;
		return currentRotation;
	}
	
	public int[][] getRotation(int pos) {
		if(pos > 3) pos = 0;
		return rotaciones[pos];
	}
	
	public void changeForm(int index) {
		shape = formas[index];
		crearRotaciones();
	}

	 /**
	 * Crea las rotaciones posibles para cada bloque
	 */
	private void crearRotaciones() {
		rotaciones = new int[4][][];
		for(int i  = 0;  i < 4; i++) {
			int r = shape[0].length;
			int c = shape.length;
			
			rotaciones[i] = new int[r][c];
			
			for(int y = 0; y < r; y++) {
				for(int x = 0; x < c; x++) {
					rotaciones[i][y][x] = shape[c-x-1][y];		
				}
			}
			currentRotation =i;
			shape = rotaciones[currentRotation];			
		}
		currentRotation++;
		
	}
	
	 /**
	 * Usa alguna de las rotaciones disponibles de la ficha
	 */
	public void rotar(Tablero t) {
		if(isRotable(t)) {
			setShape();
		}
	}

	 /**
	 * Valida si la ficha se peude rotar correctamente con respecto al tablero y a otras fichas
	 */
	private boolean isRotable(Tablero t) {
		int aux;
		if(currentRotation > 3) aux = 0; else aux = currentRotation; 
		int[][] next = rotaciones[aux];
		return x + next[0].length <= t.getCols() && y + next.length < t.getFilas();
	}
	
	public BloqueTetris Clone(){
		try {
			return (BloqueTetris) super.clone();
		} catch (CloneNotSupportedException e) {
			Log.registre(e);
		}
		return null;
	}

	public boolean borrarCercanos() {
		return reborde.borrarCercanos();
	}


	 /**
	 * Retorna las coordenas en las que se encuentra la ficha de la forma x,y
	 * @return las coordenadas
	 */
	public int[][] getCoordenadas() {
		int cont = 0;
		int[][] posiciones = new int[4][];
		int[] aux;	
		for(int r = 0; r < getHeight(); r++) {
			for(int c = 0; c < getWidth(); c++) {
				if(shape[r][c] == 1) {
					aux = new int[2];
					aux[0] = c + x;
					if(r + y < 0) aux[1] = 0; else aux[1] = r + y;
					posiciones[cont] = aux;
					cont++;
				}
			}
		}
		return posiciones;
	}

	 /**
	 * Retorna las coordenadas mas cercanas a la ficha o aun bloque de distancia
	 * @return Arreglo de corrednadas cercanas
	 */
	public int[][] getCoordenadasCercanas() {
		int[][] posiciones = new int[16][2];
		int cont = 0;
		int[] aux;
		for(int[] c :this.getCoordenadas()) {
			aux = new int[2];
			aux[0] = c[0]-1;
			aux[1] = c[1];
			posiciones[cont] = aux;
			cont++;
			aux = new int[2];
			aux[0] = c[0]+1;
			aux[1] = c[1];
			posiciones[cont] = aux;
			cont++;
			aux = new int[2];
			aux[0] = c[0];
			aux[1] = c[1]+1;
			posiciones[cont] = aux;
			cont++;
			aux = new int[2];
			aux[0] = c[0];
			aux[1] = c[1];
			posiciones[cont] = aux;
			cont++;
		}
		return posiciones;
	}

	
	public int getLeftEdge() {return x;}
	public int getRigthEdge() {return x + getWidth();}

	/**
	 * Genera un bloque de tetris aleatorio en forma color y reborde
	 */
	private static BloqueTetris selectRandomBlock(String bg) {
		int n = new Random().nextInt(formas.length);
		if (Objects.equals(colores[n], bg)) return selectRandomBlock(bg);
		Reborde r = Reborde.randomReborde();
		return new BloqueTetris(formas[n],r, colores[n], n);
	}


	 /**
	 * Toma un bloque del arreglo de bloques
	 * @param pos de la cualse quiere tomar
	  * @param bg Color que no puede ser tomado
	 * @return el bloque
	 */
	public static BloqueTetris getRandomBlock(int pos, String bg) {//======>==================================== meter la secuencia q me salio
			while(pos >= bloquesShared.size()) {
				BloqueTetris bloqueAleatorio = selectRandomBlock(bg);
				bloquesShared.add(bloqueAleatorio);				
			}
		return  bloquesShared.get(pos).Clone();
	}
	
	private void setShape() {
		if(currentRotation > 3) currentRotation = 0;
		shape = rotaciones[currentRotation];
		currentRotation++;
	}
	
	public void setShape(int pos) {
		if(pos < 4) {
			shape = rotaciones[pos];	
			currentRotation = pos+1;			
		}		
	}


	/**
	 *
	 * @return La posición en Y de la pieza desde su posición superior izquierda
	 */
	public int getHeight() {
		return shape.length;
	}

	/**
	 *
	 * @return La posición en X de la pieza desde su posición superior izquierda
	 */
	public int 	getWidth() {
		return shape[0].length;
	}


	public boolean modifyShape() {
		return reborde.modifyShape();
	}


	public void setMovilidad(boolean p) {
		this.isMovible = p;
	}



	public int countCuadrosFila(int pos) {
		int cont = 0;
		for(int n :getShape()[pos]) {
			if(n == 1) cont++;
		}
		return cont;
	}
	
	public int countCuadrosCol(int pos) {
		int cont = 0;
		for(int[] n :getShape()) {
			if(n[pos] == 1) cont++;
		}
		return cont;
	}



	public int[][][] getRotaciones() {
		return this.rotaciones;
	}
	
	
	public void findIdealForm(int[][] data, Tablero t, String col) {
		int indx = 0;
		double[][] dataForms = new double[formas.length*4][3];
		
		for(int i = 0; i < formas.length; i++) {	
			BloqueTetris b = new BloqueTetris(formas[i], null, colores[i], i);
			for(int j = 0; j < b.getRotaciones().length; j++) {
				double[] aux = {i, j, puntuacion(data, b.getRotation(j))}; 
				dataForms[indx] = aux;
				indx++;
			}
		}
		calculateBestRotacion(dataForms);
		while(isBajable(t, col)) moveDown();
		
		
		
	}

	private boolean isBajable(Tablero t, String col) {//get downest squares, por cada coord de ellos, si hay algo all�
		for(int[] co :this.getDownsetSquares(t)) {
			try {
				//Si abajo esta vacio, mover abajo
				if(!Objects.equals(t.getBackground()[co[1] + 1][co[0]], col)) return false;
			}catch(Exception e) {
				return false;
				}
		}
		return true;
	}

	private int[][] getDownsetSquares(Tablero t) {
		int[][] coords = new int[getWidth()][2];
		int pos = 0;
		for(int c = 0; c < getWidth(); c++) {
			for(int r = getHeight()-1; r >= 0 ; r--) {
				if(shape[r][c] == 1 && r+ y < t.getFilas() && x + c< t.getCols()) {
					int[] aux = {c+x,r+y};
					coords[pos] =  aux;
					pos++;
					break;				
				}
			}
		}
		return coords;
	}



	private void calculateBestRotacion(double[][] dataForms) {
		int form = 0,rot = 0;
		double max = 0;
		for(int i = 0; i < dataForms.length; i++) {
			if(dataForms[i][2] > max) {
				max =  dataForms[i][2];
				form = (int) dataForms[i][0];
				rot =  (int) dataForms[i][1];
			}
		}
		this.changeForm(form);
		this.setShape(rot);
		
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
					}					}

			}
			DoubleSummaryStatistics stat = Arrays.stream(puntuaciones).summaryStatistics();
		return stat.getMax();
	}



	private int areEquals(int[] arr, int[] arr2, int suma) {
		int cont = 0; 
		for(int i = 0; i < arr.length; i++) if(arr[i] == arr2[i] && arr[i] == 1) cont += suma;
		return cont;
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

	/*
	 * Traduce una matriz de colores a una matriz de ceros y unos
	 * 1 si ahi puede caber una ficha, 0 si no.
	 */

	public int[][] traducir(Tablero t, String col) {
		int[][] rta = new int[5][];
		int cont = 0;
		for(int i = y; i < t.getFilas() && i < y + 4; i++) {
			int[] aux = new int[t.getCols()-x];
			for(int j =x; j < t.getCols() && j <x + 4; j++) {
				if(Objects.equals(t.getBackground()[i][j], col)) aux[j-x] = 1;  else aux[j-x] = 0;
			}
			rta[cont] = aux;
			cont++;
		}
		return rta;
	}
	
	
	
}

