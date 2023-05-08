package edu.eci.arsw.models.buffos;

import java.io.Serializable;
import java.util.List;

import edu.eci.arsw.models.Tablero;
import lombok.Getter;

@Getter
public abstract class Buffo implements Serializable{
	private final int x;
	private final int y;

	private final String tipo;

	public Buffo(int[] c, String tipo) {
		x = c[0];
		y = c[1];
		this.tipo = tipo;
	}

	 /**
	 * Activa el buffo
	 */
	public abstract void activate(List<Tablero> tableros,int idTablero);

	@Override
	public String toString() {
		return String.format("{\"tipo\": \"%s\", \"x\": %d, \"y\": %d}", tipo, x, y);
	}
}

