package edu.eci.arsw.models.buffos;

import edu.eci.arsw.models.Tablero;

import java.util.List;
import java.util.stream.Collectors;

public class BuffoSD extends Buffo{
	
	public BuffoSD(int[] c) {
		super(c);
		super.color = "magenta";
	}

	@Override
	public void activate(List<Tablero> tableros, int idTablero) {
		Tablero t = tableros.stream().filter(s -> s.getId() == idTablero).collect(Collectors.toList()).get(0);
		t.setMovilidadBlock(false);
	}

}
