package edu.eci.arsw.models;

import java.io.Serializable;
import java.util.*;

import edu.eci.arsw.shared.Log;
import edu.eci.arsw.models.rebordes.Reborde;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BloqueTetris implements Cloneable, Serializable {
	//C, L, LINE, T, Z
	public static int[][][] formas ={{{1,1}, {1,1}}, {{1,0}, {1,0}, {1,1}}, {{1,1,1,1}},
									{{1,1,1}, {0,1,0}}, {{1,1,0}, {0,1,1}} };
	public static String[] colores= {"#D2691E", "#00FFFF", "#8FBC8F", "#E9967A", "#32CD32"};
	private int[][] shape;
	
	private final String color;
	private int x, y;
	protected int[][][] rotaciones;
	private int currentRotation;
	private final Reborde reborde;

	private int[][] coordenadas = new int[4][];
	@Setter
	private boolean isMobibleDown = true;
	private final int currentForm;

	public BloqueTetris(int[][] shape, Reborde r, String color, int cu) {
		this.reborde = r;
		this.shape = shape;
		this.color = color;
		this.currentForm = cu;
		crearRotaciones();
		calculateCoordenadas();
	}

	/**
	 * Se encarga de hacer apareces los bloques por encima del tablero y en el centro
	 * @param girdWidth anchura del tablero
	 */
	public void spawn(int girdWidth) {
		y = -getHeight();
		x = girdWidth / 2;
		calculateCoordenadas();
	}

	
	public void moveDown() {
		if(isMobibleDown) {
			y++;
			calculateCoordenadas();
		}
	}
	
	public void moveLeft() {
		x--;
		calculateCoordenadas();
	}
	
	public void moveRight() {
		x++;
		calculateCoordenadas();
	}

	public void setPos(int X,int Y) {
		x = X;
		y = Y;
		calculateCoordenadas();
	}

	public int getCurrentRot() {
		 if(currentRotation == 0) return 3;
		return currentRotation-1;
	}
	
	public int[][] getRotation(int pos) {
		if(pos > 3) pos = 0;
		return rotaciones[pos];
	}
	
	public void changeForm(int index, int rotacion) {
		shape = formas[index];
		crearRotaciones();
		currentRotation = rotacion;
		rotar();
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
	 * Calcula las coordenas en las que se encuentra la ficha de la forma x,y
	 */
	public void calculateCoordenadas() {
		int cont = 0;
		coordenadas = new int[4][];

		for(int r = 0; r < getHeight(); r++) {
			for(int c = 0; c < getWidth(); c++) {
				if(shape[r][c] == 1) {
					coordenadas[cont] = new int[]{c + x, r + y};
					cont++;
				}
			}
		}
	}

	 /**
	 * Retorna las coordenadas laterales a la ficha
	 * @return Arreglo de corrednadas cercanas
	 */
	 public List<int[]> getCoordenadasCercanas() {
		 List<int[]> posiciones = new ArrayList<>();
		 for(int[] c : coordenadas) {
			 if (HasNotCoord(c[0] - 1, c[1], posiciones))  posiciones.add(new int[]{c[0]-1, c[1]});
			 if (HasNotCoord(c[0] + 1, c[1], posiciones ))  posiciones.add(new int[]{c[0]+1, c[1]});
			 if (HasNotCoord(c[0], c[1]+1, posiciones))  posiciones.add(new int[]{c[0], c[1]+1 });
			 if (HasNotCoord(c[0], c[1] - 1, posiciones))  posiciones.add(new int[]{c[0], c[1]-1});
			 if (HasNotCoord(c[0] + 1, c[1] + 1, posiciones))  posiciones.add(new int[]{c[0] + 1, c[1]+1});
			 if (HasNotCoord(c[0] - 1, c[1] - 1, posiciones))  posiciones.add(new int[]{c[0]-1, c[1]-1});
			 if (HasNotCoord(c[0] + 1, c[1] - 1, posiciones))  posiciones.add(new int[]{c[0]+1, c[1]-1});
			 if (HasNotCoord(c[0] - 1, c[1] + 1, posiciones))  posiciones.add(new int[]{c[0]-1, c[1]+1});
		 }
		 return posiciones;
	 }

	/**
	 * Verifica que la coordenada x e y no esté en la lista indicada, y tampoco sea parte de las coordenadas del bloque
	 */
	 private boolean HasNotCoord(int x, int y, List<int[]> positions){
		 return Arrays.stream(coordenadas).noneMatch(c -> c[0] == x && c[1] == y) &&
				 positions.stream().noneMatch(co -> co[0] == x && co[1] == y);
	 }

	/**
	 * Genera un bloque de tetris aleatorio en forma color y reborde
	 */
	public static BloqueTetris selectRandomBlock(String bg, Reborde r) {
		int n = new Random().nextInt(formas.length);
		if (Objects.equals(colores[n], bg)) return selectRandomBlock(bg, r);
		if (r == null)  r = Reborde.randomReborde();
		return new BloqueTetris(formas[n],r, colores[n], n);
	}
	
	public void rotar() {
		if(currentRotation > 3) currentRotation = 0;
		shape = rotaciones[currentRotation];
		currentRotation++;
		calculateCoordenadas();
	}


	/**
	 *
	 * @return La posición en Y de la pieza desde su posición superior izquierda
	 */
	public int getHeight() {
		if (shape == null) return 0;
		return shape.length;
	}

	/**
	 * @return La posición en X de la pieza desde su posición superior izquierda
	 */
	public int 	getWidth() {
		if (shape == null) return 0;
		return shape[0].length;
	}


	public boolean modifyShape() {
		return reborde.modifyShape();
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

	@Override
	public String toString() {
		return "{" +
				"\"color\": \"" + color + "\"" +
				", \"reborde\":" + reborde.toString() +
				", \"coordenadas\":" + Arrays.deepToString(coordenadas) +
				", \"x\":" + x +
				", \"y\":" + y +
				", \"height\":" + getHeight() +
				", \"width\":" + getWidth() +
				"}";
	}
}

