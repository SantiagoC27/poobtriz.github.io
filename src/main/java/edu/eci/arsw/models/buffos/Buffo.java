package edu.eci.arsw.models.buffos;

import java.util.List;
import java.io.Serializable;

import edu.eci.arsw.models.Tablero;
import lombok.Getter;

@Getter
public abstract class Buffo implements Serializable{
	protected String color;
	private final int x;
	private final int y;

	public Buffo(int[] c) {
		x = c[0];
		y = c[1];
	}

	 /**
	 * Activa el buffo
	 */
	public abstract void activate(List<Tablero> tableros,int idTablero);

	@Override
	public String toString() {
		return String.format("{\"color\": \"%s\", \"x\": %d, \"y\": %d}", color, x, y);
	}
}
