package edu.eci.arsw.models.buffos;

import edu.eci.arsw.models.Tablero;

import java.util.List;
import java.util.stream.Collectors;

public class BuffoStopDiece extends Buffo{
	
	public BuffoStopDiece(int[] c) {
		super(c, "stopDiece");
	}

	@Override
	public void activate(List<Tablero> tableros, int idTablero) {
		List<Tablero> othersTableros = tableros.stream().filter(s -> s.getId() != idTablero).collect(Collectors.toList());
		for (Tablero t: othersTableros) {
			t.setMovilidadBlock(false);
		}
	}

}
