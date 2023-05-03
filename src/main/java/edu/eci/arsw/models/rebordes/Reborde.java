package edu.eci.arsw.models.rebordes;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;


public abstract class Reborde implements Cloneable, Serializable{
	protected String color;
	
	public Reborde(String color) {
		this.color = color;
	}
	

	 /**
	 * Genera aleatoriamente un reborde para la ficha dentro de los que existen
	 * @return el reborde que salga
	 */
	public static Reborde randomReborde() {
		List<String> rebordes = new ArrayList<String>();
		rebordes.add("N");
		rebordes.add("P");
		rebordes.add("D");
		rebordes.add("R");
		Random random = new Random(); 
		int n =  random.nextInt(rebordes.size());
		String tipo = rebordes.get(n);
		Reborde b;
		switch (tipo.toUpperCase()) {
		case "N":
			b = new RebordeN();
			break;
		case "P":
			b = new RebordeP();
			break;
		case "D":
			b = new RebordeD();
			break;
		case "R":
			b = new RebordeR();
			break;
		default:
			b = new RebordeR();
		}
		
		return b;
		
	}
	
	public String getColor() {
		return color;
	}


	 /**
	 * Valida que se pueda borrar
	 */
	public abstract boolean isBorrable();


	 /**
	 * Valida que se puedan borrar los cuadros adyacentes
	 */
    public abstract boolean borrarCercanos();

	public Reborde Clone() throws CloneNotSupportedException{
		return (Reborde) super.clone();
	}

	public abstract boolean modifyShape();

	@Override
	public String toString() {
		return "{" +
				String.format("\"color\": \"%s\"", color) +
				"}";
	}
}
