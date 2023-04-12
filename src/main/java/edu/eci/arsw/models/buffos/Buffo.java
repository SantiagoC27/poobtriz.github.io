package edu.eci.arsw.models.buffos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.Serializable;

import edu.eci.arsw.models.Tablero;
import lombok.Getter;

@Getter
public abstract class Buffo implements Serializable{
	protected String color;
	private int x;
	private int y;
	private static List<String> buffos = llenarPosibleBuffos();
	private static final List<String> posiblesBuffos = new ArrayList<String>();

	
	public Buffo(int[] c) {
		x = c[0];
		y = c[1];
	}
	
	

	 /**
	 * Crea la lista con los buffos pedidos
	 * @param x de buffos
	 */
	public static void prepareBuffos(int x) {
		buffos = llenarPosibleBuffos();
		if(x > buffos.size()) x = buffos.size();
		List<Integer> nums = new ArrayList<Integer>();
		Random random = new Random(); 
		
		
		while(nums.size() != x) {
			int n =  random.nextInt(buffos.size());
			if(isValidNum(nums, n)) { 
				nums.add(n);
			}
		}
		
		for(int x1 :nums) {
			posiblesBuffos.add(buffos.get(x1));
		}
		
	}
	
	
	
	 /**
	 * Valida que un numero este dentro de una lista
	 * @param nums que contiene los numeros
	 * @param n numero a buscar
	 * @return si esta o no esta
	 */
	private static boolean isValidNum(List<Integer> nums, int n) {
		for (Integer num : nums) {
			if (num == n) return false;
		}
		return true;
		
	}


	 /**
	 * Crea un arreglo con los diferetnes tipos de buffos
	 * @return la lista con los buffos
	 */
	private static List<String> llenarPosibleBuffos() {
		List<String> buffos = new ArrayList<String>();
		buffos.add("ST");
		buffos.add("SD");
		buffos.add("S");
		buffos.add("X");
		return buffos;
	}

	 /**
	 * Activa el buffo
	 */
	public abstract void activate(Tablero t);



	/**
	 * Selecciona un buffo aleatorio en una lista que los contenga
	 * @param c lista con los buffos
	 * @return el buffo aleatorio
	 */
	public static Buffo selectRandomBuffo(int[] c) {
		Buffo b = null;
		if(posiblesBuffos.size() != 0 && c != null) {
			Random random = new Random(); 
			int n =  random.nextInt(posiblesBuffos.size());   
			String tipo = posiblesBuffos.get(n);
			switch (tipo.toUpperCase()) {
				case "SD":
					b = new BuffoSD(c);
					break;
				case "ST":
					b = new BuffoST(c);
					break;
				case "X":
					b = new BuffoX(c);
					break;
				default:
					b = new BuffoS(c);
					break;
			}
		}
		return b;
		
	}

	
}
